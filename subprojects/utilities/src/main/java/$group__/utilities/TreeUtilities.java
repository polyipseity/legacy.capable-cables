package $group__.utilities;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public enum TreeUtilities {
	/* MARK empty */;

	public static <T, R> Optional<R> visitNodes(EnumStrategy strategy, T obj,
	                                            Function<? super T, ? extends R> function,
	                                            Function<? super T, ? extends Iterable<? extends T>> splitter,
	                                            @Nullable BiFunction<? super R, ? super Iterable<R>, ? extends R> combiner,
	                                            @Nullable Consumer<? super T> repeater) {
		switch (strategy) {
			case DEPTH_FIRST:
				return visitNodesDepthFirst(obj, function, splitter, combiner, repeater);
			case BREADTH_FIRST:
				return visitNodesBreadthFirst(obj, function, splitter, combiner, repeater);
			default:
				throw new InternalError();
		}
	}

	@SuppressWarnings("ObjectAllocationInLoop")
	public static <T, R> Optional<R> visitNodesDepthFirst(T obj,
	                                                      Function<? super T, ? extends R> function,
	                                                      Function<? super T, ? extends Iterable<? extends T>> splitter,
	                                                      @Nullable BiFunction<? super R, ? super Iterable<R>, ? extends R> combiner,
	                                                      @Nullable Consumer<? super T> repeater) {
		boolean shouldCombine = combiner != null;
		Stack<Iterator<? extends T>> stackSplitter = new Stack<>();
		@Nullable Stack<Deque<R>> stackCombiner = shouldCombine ? new Stack<>() : null;
		Set<T> visited = new HashSet<>(CapacityUtilities.INITIAL_CAPACITY_LARGE);

		stackSplitter.push(Iterators.forArray(obj));
		if (shouldCombine)
			stackCombiner.push(new ArrayDeque<>(CapacityUtilities.INITIAL_CAPACITY_SMALL));

		while (!stackSplitter.isEmpty()) {
			Iterator<? extends T> frameSplitter = AssertionUtilities.assertNonnull(stackSplitter.peek());
			@Nullable Deque<R> frameCombiner = shouldCombine ? stackCombiner.peek() : null;
			assert !shouldCombine || frameCombiner != null;
			if (frameSplitter.hasNext()) {
				T current = frameSplitter.next();
				if (!visited.add(current)) {
					if (repeater != null)
						repeater.accept(current);
				} else {
					R currentRet = function.apply(current);
					if (shouldCombine) {
						frameCombiner.add(currentRet);
						stackCombiner.push(new ArrayDeque<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM));
					}
					stackSplitter.push(splitter.apply(current).iterator());
				}
			} else {
				stackSplitter.pop();
				if (shouldCombine) {
					Deque<R> ret = stackCombiner.pop();
					if (stackCombiner.isEmpty())
						stackCombiner.push(ret);
					else {
						Deque<R> frameCombinerParent = AssertionUtilities.assertNonnull(stackCombiner.peek());
						frameCombinerParent.addLast(combiner.apply(frameCombinerParent.removeLast(), frameCombiner));
					}
				}
			}
		}
		return Optional.ofNullable(stackCombiner)
				.map(Stack::peek)
				.map(Deque::getFirst);
	}

	@SuppressWarnings("ObjectAllocationInLoop")
	public static <T, R> Optional<R> visitNodesBreadthFirst(T obj,
	                                                        Function<? super T, ? extends R> function,
	                                                        Function<? super T, ? extends Iterable<? extends T>> splitter,
	                                                        @Nullable BiFunction<? super R, ? super Iterable<R>, ? extends R> combiner,
	                                                        @Nullable Consumer<? super T> repeater) {
		boolean shouldCombine = combiner != null;
		List<T>
				frameSplitter = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM),
				frameSplitterNext = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM);
		@Nullable Stack<List<R>> stackCombiner = shouldCombine ? new Stack<>() : null;
		Set<T> visited = new HashSet<>(CapacityUtilities.INITIAL_CAPACITY_LARGE);
		@Nullable List<Runnable>
				combinerActions = shouldCombine ? new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_LARGE) : null,
				frameCombinerActions = shouldCombine ? new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_LARGE) : null;

		frameSplitter.add(obj);
		if (shouldCombine)
			stackCombiner.push(new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM));

		while (!frameSplitter.isEmpty()) {
			@Nullable List<R> frameCombiner = shouldCombine ? stackCombiner.peek() : null;
			assert !shouldCombine || frameCombiner != null;
			if (shouldCombine)
				stackCombiner.push(new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM));
			int nodeIndex = 0, childrenFlatIndex = 0;
			for (T n : frameSplitter) {
				R currentRet = function.apply(n);
				if (shouldCombine)
					frameCombiner.add(currentRet);
				int childrenFlatIndexStart = childrenFlatIndex;
				for (Iterator<? extends T> iterator = splitter.apply(n).iterator(); iterator.hasNext(); ++childrenFlatIndex) {
					T c = iterator.next();
					if (!visited.add(c)) {
						if (repeater != null)
							repeater.accept(c);
					} else
						frameSplitterNext.add(c);
				}

				final int nodeIndexCopy = nodeIndex, childrenFlatIndexCopy = childrenFlatIndex;
				if (shouldCombine) {
					List<R> frameCombinerChildren = AssertionUtilities.assertNonnull(stackCombiner.peek());
					frameCombinerActions.add(0, () ->
							frameCombiner.add(combiner.apply(frameCombiner.remove(nodeIndexCopy),
									frameCombinerChildren.subList(childrenFlatIndexStart, childrenFlatIndexCopy))));
				}
				++nodeIndex;
			}
			frameSplitter = MiscellaneousUtilities.k(frameSplitterNext, frameSplitterNext = frameSplitter);
			frameSplitterNext.clear();
			if (shouldCombine) {
				combinerActions.addAll(frameCombinerActions);
				frameCombinerActions.clear();
			}
		}
		if (shouldCombine)
			Lists.reverse(combinerActions).forEach(Runnable::run);
		return Optional.ofNullable(stackCombiner)
				.map(Stack::peek)
				.map(l -> l.get(0));
	}

	public enum EnumStrategy {
		DEPTH_FIRST,
		BREADTH_FIRST,
	}
}
