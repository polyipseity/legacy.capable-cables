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
		Stack<Iterator<? extends T>> stackSplitter = new Stack<>();
		Optional<Stack<Deque<R>>> stackCombiner = Optional.ofNullable(combiner).map(c -> new Stack<>());
		Set<T> visited = new HashSet<>(CapacityUtilities.INITIAL_CAPACITY_LARGE);

		stackSplitter.push(Iterators.forArray(obj));
		stackCombiner.ifPresent(s -> s.push(new ArrayDeque<>(CapacityUtilities.INITIAL_CAPACITY_SMALL)));

		while (!stackSplitter.isEmpty()) {
			Iterator<? extends T> frameSplitter = stackSplitter.peek();
			Optional<Deque<R>> frameCombiner = stackCombiner.map(Stack::peek);
			if (frameSplitter.hasNext()) {
				T current = frameSplitter.next();
				if (!visited.add(current)) {
					if (repeater != null)
						repeater.accept(current);
				} else {
					R currentRet = function.apply(current);
					frameCombiner.ifPresent(f -> {
						f.add(currentRet);
						stackCombiner.orElseThrow(InternalError::new).push(new ArrayDeque<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM));
					});
					stackSplitter.push(splitter.apply(current).iterator());
				}
			} else {
				stackSplitter.pop();
				stackCombiner.ifPresent(s -> {
					Deque<R> ret = s.pop();
					if (s.isEmpty()) {
						s.push(ret);
						return;
					}
					Deque<R> frameCombinerParent = s.peek();
					frameCombinerParent.addLast(combiner.apply(frameCombinerParent.removeLast(), frameCombiner.orElseThrow(InternalError::new)));
				});
			}
		}
		return stackCombiner.map(Stack::peek).map(Deque::getFirst);
	}

	@SuppressWarnings("ObjectAllocationInLoop")
	public static <T, R> Optional<R> visitNodesBreadthFirst(T obj,
	                                                        Function<? super T, ? extends R> function,
	                                                        Function<? super T, ? extends Iterable<? extends T>> splitter,
	                                                        @Nullable BiFunction<? super R, ? super Iterable<R>, ? extends R> combiner,
	                                                        @Nullable Consumer<? super T> repeater) {
		List<T>
				frameSplitter = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM),
				frameSplitterNext = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM);
		Optional<Stack<List<R>>> stackCombiner = Optional.ofNullable(combiner).map(c -> new Stack<>());
		Set<T> visited = new HashSet<>(CapacityUtilities.INITIAL_CAPACITY_LARGE);
		Optional<List<Runnable>>
				combinerActions = Optional.ofNullable(combiner).map(c -> new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_LARGE)),
				frameCombinerActions = Optional.ofNullable(combiner).map(c -> new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM));

		frameSplitter.add(obj);
		stackCombiner.ifPresent(s -> s.push(new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM)));

		while (!frameSplitter.isEmpty()) {
			Optional<List<R>> frameCombiner = stackCombiner.map(Stack::peek);
			stackCombiner.ifPresent(s -> s.push(new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM)));
			int nodeIndex = 0, childrenFlatIndex = 0;
			for (T n : frameSplitter) {
				R currentRet = function.apply(n);
				frameCombiner.ifPresent(f -> f.add(currentRet));
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
				stackCombiner.ifPresent(s -> {
					List<R> frameCombinerChildren = s.peek(),
							frameCombinerUnboxed = frameCombiner.orElseThrow(InternalError::new);
					frameCombinerActions.orElseThrow(InternalError::new).add(0, () ->
							frameCombinerUnboxed.add(combiner.apply(frameCombinerUnboxed.remove(nodeIndexCopy),
									frameCombinerChildren.subList(childrenFlatIndexStart, childrenFlatIndexCopy))));
				});
				++nodeIndex;
			}
			frameSplitter = MiscellaneousUtilities.k(frameSplitterNext, frameSplitterNext = frameSplitter);
			frameSplitterNext.clear();
			frameCombinerActions.ifPresent(c -> {
				combinerActions.orElseThrow(InternalError::new).addAll(c);
				c.clear();
			});
		}
		combinerActions.ifPresent(c -> Lists.reverse(c).forEach(Runnable::run));
		return stackCombiner.map(Stack::peek).map(l -> l.get(0));
	}

	public enum EnumStrategy {
		DEPTH_FIRST,
		BREADTH_FIRST,
	}
}
