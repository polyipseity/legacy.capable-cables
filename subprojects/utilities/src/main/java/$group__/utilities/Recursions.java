package $group__.utilities;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public enum Recursions {
	/* MARK empty */;

	@SuppressWarnings("UnusedReturnValue")
	public static <T, R> Optional<R> recurseAsDepthFirstLoop(T obj, Function<? super T, ? extends R> function,
	                                                         Function<? super T, ? extends Iterable<? extends T>> splitter, @Nullable Consumer<? super T> rediscoverEr, @Nullable Function<? super Deque<R>, ? extends R> combiner) {
		ArrayDeque<T> stackT = new ArrayDeque<>(Capacities.INITIAL_CAPACITY_3);
		@Nullable ArrayDeque<R> listR = combiner == null ? null : new ArrayDeque<>(Capacities.INITIAL_CAPACITY_3);
		stackT.push(obj);

		HashSet<T> discovered = new HashSet<>(Capacities.INITIAL_CAPACITY_3);
		while (!stackT.isEmpty()) {
			T t = stackT.pop();
			if (discovered.add(t)) {
				R r = function.apply(t);
				if (listR != null) listR.add(r);
				splitter.apply(t).forEach(stackT::push);
			} else if (rediscoverEr != null)
				rediscoverEr.accept(t);
		}

		return listR == null ? Optional.empty() : Optional.ofNullable(combiner.apply(listR));
	}


	@SuppressWarnings("UnusedReturnValue")
	public static <T, R> Optional<R> recurseAsBreadthFirstLoop(T obj, Function<? super T, ? extends R> function,
	                                                           Function<? super T, ? extends Iterable<? extends T>> splitter, @Nullable Consumer<? super T> rediscoverEr, @Nullable Function<? super Deque<R>, ? extends R> combiner) {
		ArrayDeque<T> stackT = new ArrayDeque<>(Capacities.INITIAL_CAPACITY_3);
		@Nullable ArrayDeque<R> listR = combiner == null ? null : new ArrayDeque<>(Capacities.INITIAL_CAPACITY_3);
		stackT.add(obj);

		HashSet<T> discovered = new HashSet<>(Capacities.INITIAL_CAPACITY_3);
		while (!stackT.isEmpty()) {
			T t = stackT.poll();
			if (discovered.add(t)) {
				R r = function.apply(t);
				if (listR != null) listR.add(r);
				splitter.apply(t).forEach(stackT::addLast);
			} else if (rediscoverEr != null)
				rediscoverEr.accept(t);
		}

		return listR == null ? Optional.empty() : Optional.ofNullable(combiner.apply(listR));
	}
}
