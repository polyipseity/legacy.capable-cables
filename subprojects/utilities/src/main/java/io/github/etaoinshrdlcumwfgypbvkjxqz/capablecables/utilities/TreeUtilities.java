package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public enum TreeUtilities {
	;

	public static <T, R> Optional<R> visitNodes(EnumStrategy strategy,
	                                            T obj,
	                                            Function<@Nonnull ? super T, @Nullable ? extends R> function,
	                                            Function<@Nonnull ? super T, @Nonnull ? extends Iterable<? extends T>> splitter,
	                                            @Nullable BiFunction<@Nonnull ? super R, @Nonnull ? super Iterable<R>, @Nonnull ? extends R> combiner,
	                                            @Nullable Consumer<@Nonnull ? super T> repeater) {
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
	                                                       Function<@Nonnull ? super T, @Nullable ? extends R> function,
	                                                       Function<@Nonnull ? super T, @Nonnull ? extends Iterable<? extends T>> splitter,
	                                                       @Nullable BiFunction<@Nonnull ? super R, @Nonnull ? super Iterable<R>, @Nonnull ? extends R> combiner,
	                                                       @Nullable Consumer<@Nonnull ? super T> repeater) {
		boolean shouldCombine = combiner != null;
		Deque<Iterator<? extends T>> stackSplitter = new ArrayDeque<>(CapacityUtilities.getInitialCapacitySmall());
		@Nullable Deque<Deque<R>> stackCombiner = shouldCombine ? new ArrayDeque<>(CapacityUtilities.getInitialCapacitySmall()) : null;
		Set<T> visited = new HashSet<>(CapacityUtilities.getInitialCapacityLarge());

		stackSplitter.push(Iterators.singletonIterator(obj));
		if (shouldCombine)
			stackCombiner.push(new ArrayDeque<>(CapacityUtilities.getInitialCapacitySmall()));

		while (!stackSplitter.isEmpty()) {
			Iterator<? extends T> frameSplitter = AssertionUtilities.assertNonnull(stackSplitter.element());
			@Nullable Deque<R> frameCombiner = shouldCombine ? stackCombiner.element() : null;
			if (frameSplitter.hasNext()) {
				T current = frameSplitter.next();
				if (!visited.add(current)) {
					if (repeater != null)
						repeater.accept(current);
				} else {
					@Nullable R currentRet = function.apply(current);
					if (shouldCombine) {
						frameCombiner.add(currentRet);
						stackCombiner.push(new ArrayDeque<>(CapacityUtilities.getInitialCapacityMedium()));
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
						Deque<R> frameCombinerParent = stackCombiner.element();
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
	                                                         Function<@Nonnull ? super T, @Nullable ? extends R> function,
	                                                         Function<@Nonnull ? super T, @Nonnull ? extends Iterable<? extends T>> splitter,
	                                                         @Nullable BiFunction<@Nonnull ? super R, @Nonnull ? super Iterable<R>, @Nonnull ? extends R> combiner,
	                                                         @Nullable Consumer<@Nonnull ? super T> repeater) {
		boolean shouldCombine = combiner != null;
		List<T>
				frameSplitter = new ArrayList<>(CapacityUtilities.getInitialCapacityMedium()),
				frameSplitterNext = new ArrayList<>(CapacityUtilities.getInitialCapacityMedium());
		@Nullable Deque<List<R>> stackCombiner = shouldCombine ? new ArrayDeque<>(CapacityUtilities.getInitialCapacitySmall()) : null;
		Set<T> visited = new HashSet<>(CapacityUtilities.getInitialCapacityLarge());
		@Nullable List<Runnable>
				combinerActions = shouldCombine ? new ArrayList<>(CapacityUtilities.getInitialCapacityLarge()) : null,
				frameCombinerActions = shouldCombine ? new ArrayList<>(CapacityUtilities.getInitialCapacityLarge()) : null;

		frameSplitter.add(obj);
		if (shouldCombine)
			stackCombiner.push(new ArrayList<>(CapacityUtilities.getInitialCapacityMedium()));

		while (!frameSplitter.isEmpty()) {
			@Nullable List<R> frameCombiner = shouldCombine ? stackCombiner.element() : null;
			if (shouldCombine)
				stackCombiner.push(new ArrayList<>(CapacityUtilities.getInitialCapacityMedium()));
			final int[] nodeIndex = {0};
			final int[] childrenFlatIndex = {0};
			List<T> finalFrameSplitterNext = frameSplitterNext;
			frameSplitter.forEach(node -> {
				@Nullable R currentRet = function.apply(node);
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
