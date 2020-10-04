package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public enum TreeUtilities {
	;

	public static <T, R> Optional<R> visitNodes(EnumStrategy strategy,
	                                            T obj,
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
	private static <T, R> Optional<R> visitNodesDepthFirst(T obj,
	                                                       Function<? super T, ? extends R> function,
	                                                       Function<? super T, ? extends Iterable<? extends T>> splitter,
	                                                       @Nullable BiFunction<? super R, ? super Iterable<R>, ? extends R> combiner,
	                                                       @Nullable Consumer<? super T> repeater) {
		boolean shouldCombine = combiner != null;
		Deque<Iterator<? extends T>> stackSplitter = new ArrayDeque<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);
		@Nullable Deque<Deque<R>> stackCombiner = shouldCombine ? new ArrayDeque<>(CapacityUtilities.INITIAL_CAPACITY_SMALL) : null;
		Set<T> visited = new HashSet<>(CapacityUtilities.INITIAL_CAPACITY_LARGE);

		stackSplitter.push(Iterators.forArray(obj));
		if (shouldCombine)
			stackCombiner.push(new ArrayDeque<>(CapacityUtilities.INITIAL_CAPACITY_SMALL));

		while (!stackSplitter.isEmpty()) {
			Iterator<? extends T> frameSplitter = AssertionUtilities.assertNonnull(stackSplitter.element());
			@Nullable Deque<R> frameCombiner = shouldCombine ? stackCombiner.element() : null;
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
						Deque<R> frameCombinerParent = AssertionUtilities.assertNonnull(stackCombiner.element());
						frameCombinerParent.addLast(combiner.apply(frameCombinerParent.removeLast(), frameCombiner));
					}
				}
			}
		}
		return Optional.ofNullable(stackCombiner)
				.map(Queue::element)
				.map(Deque::getFirst);
	}

	@SuppressWarnings("ObjectAllocationInLoop")
	private static <T, R> Optional<R> visitNodesBreadthFirst(T obj,
	                                                         Function<? super T, ? extends R> function,
	                                                         Function<? super T, ? extends Iterable<? extends T>> splitter,
	                                                         @Nullable BiFunction<? super R, ? super Iterable<R>, ? extends R> combiner,
	                                                         @Nullable Consumer<? super T> repeater) {
		boolean shouldCombine = combiner != null;
		List<T>
				frameSplitter = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM),
				frameSplitterNext = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM);
		@Nullable Deque<List<R>> stackCombiner = shouldCombine ? new ArrayDeque<>(CapacityUtilities.INITIAL_CAPACITY_SMALL) : null;
		Set<T> visited = new HashSet<>(CapacityUtilities.INITIAL_CAPACITY_LARGE);
		@Nullable List<Runnable>
				combinerActions = shouldCombine ? new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_LARGE) : null,
				frameCombinerActions = shouldCombine ? new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_LARGE) : null;

		frameSplitter.add(obj);
		if (shouldCombine)
			stackCombiner.push(new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM));

		while (!frameSplitter.isEmpty()) {
			@Nullable List<R> frameCombiner = shouldCombine ? stackCombiner.element() : null;
			if (shouldCombine)
				stackCombiner.push(new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM));
			final int[] nodeIndex = {0};
			final int[] childrenFlatIndex = {0};
			List<T> finalFrameSplitterNext = frameSplitterNext;
			frameSplitter.forEach(node -> {
				R currentRet = AssertionUtilities.assertNonnull(function.apply(node));
				if (shouldCombine)
					frameCombiner.add(currentRet);
				int childrenFlatIndexStart = childrenFlatIndex[0];
				AssertionUtilities.assertNonnull(splitter.apply(node)).forEach(child -> {
					if (!visited.add(child)) {
						if (repeater != null)
							repeater.accept(child);
					} else
						finalFrameSplitterNext.add(child);
					++childrenFlatIndex[0];
				});

				final int nodeIndexCopy = nodeIndex[0], childrenFlatIndexCopy = childrenFlatIndex[0];
				if (shouldCombine) {
					List<R> frameCombinerChildren = AssertionUtilities.assertNonnull(stackCombiner.element());
					frameCombinerActions.add(0, () ->
							frameCombiner.add(combiner.apply(frameCombiner.remove(nodeIndexCopy),
									frameCombinerChildren.subList(childrenFlatIndexStart, childrenFlatIndexCopy))));
				}
				++nodeIndex[0];
			});
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
				.map(Queue::element)
				.map(l -> l.get(0));
	}

	public enum EnumStrategy {
		DEPTH_FIRST,
		BREADTH_FIRST,
	}
}
