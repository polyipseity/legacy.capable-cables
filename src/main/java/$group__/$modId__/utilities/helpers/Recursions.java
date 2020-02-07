package $group__.$modId__.utilities.helpers;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Function;

import static $group__.$modId__.utilities.variables.Constants.INITIAL_SIZE_MEDIUM;

public enum Recursions {
	/* MARK empty */;


	/* SECTION static methods */

	public static <T, R> Optional<R> recurseAsDepthFirstLoop(T obj, Function<? super T, ? extends R> function, Function<? super T, ? extends Iterable<? extends T>> splitter, @Nullable Function<? super Deque<? extends R>, ? extends R> combiner) {
		ArrayDeque<T> stackT = new ArrayDeque<>(INITIAL_SIZE_MEDIUM);
		@Nullable ArrayDeque<R> listR = combiner == null ? null : new ArrayDeque<>(INITIAL_SIZE_MEDIUM);
		stackT.push(obj);

		HashSet<T> discovered = new HashSet<>(INITIAL_SIZE_MEDIUM);
		while (!stackT.isEmpty()) {
			T t = stackT.pop();
			if (discovered.add(t)) {
				R r = function.apply(t);
				if (listR != null) listR.add(r);
				splitter.apply(t).forEach(stackT::push);
			}
		}

		return listR == null ? Optional.empty() : Optional.ofNullable(combiner.apply(listR));
	}

	public static <T, R> Optional<R> recurseAsBreadthFirstLoop(T obj, Function<? super T, ? extends R> function, Function<? super T, ? extends Iterable<? extends T>> splitter, @Nullable Function<? super Deque<? extends R>, ? extends R> combiner) {
		ArrayDeque<T> stackT = new ArrayDeque<>(INITIAL_SIZE_MEDIUM);
		@Nullable ArrayDeque<R> listR = combiner == null ? null : new ArrayDeque<>(INITIAL_SIZE_MEDIUM);
		stackT.add(obj);

		HashSet<T> discovered = new HashSet<>(INITIAL_SIZE_MEDIUM);
		while (!stackT.isEmpty()) {
			T t = stackT.poll();
			if (discovered.add(t)) {
				R r = function.apply(t);
				if (listR != null) listR.addLast(r);
				splitter.apply(t).forEach(stackT::addLast);
			}
		}

		return listR == null ? Optional.empty() : Optional.ofNullable(combiner.apply(listR));
	}
}
