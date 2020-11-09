package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.stream.LongStream;

public enum LoopUtilities {
	;

	public static void doNTimes(long n, Runnable action) {
		doNTimes(n, i -> action.run());
	}

	public static void doNTimes(long n, LongConsumer action) {
		LongStream.range(0, n)
				.forEachOrdered(action);
	}

	public static void doNTimesNested(Consumer<? super long[]> action, long... ns) {
		if (ArrayUtilities.isEmpty(ns))
			return;
		long[] indexes = new long[ns.length];
		int active = 0;
		while (true) {
			if (indexes[active] < ns[active]) {
				// COMMENT advance
				++active;
				assert active <= ns.length;
				if (active == ns.length) {
					action.accept(indexes.clone());
					++indexes[--active]; // COMMENT decrement active first
				}
			} else {
				// COMMENT retreat
				indexes[active--] = 0L; // COMMENT decrement active last
				if (active < 0)
					return;
				++indexes[active];
			}
		}
	}
}
