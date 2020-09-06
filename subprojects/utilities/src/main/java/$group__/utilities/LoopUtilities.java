package $group__.utilities;

import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public enum LoopUtilities {
	;

	public static void doNTimes(int n, Runnable action) {
		doNTimes(n, i -> action.run());
	}

	public static void doNTimes(int n, IntConsumer action) {
		IntStream.range(0, n)
				.forEachOrdered(action);
	}
}
