package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;

@FunctionalInterface
public interface IThrowingRunnable<TH extends Throwable> {
	@SuppressWarnings("RedundantThrows")
	static <TH extends Throwable> Runnable execute(IThrowingRunnable<TH> lambda)
			throws TH {
		return () -> {
			try {
				lambda.run();
			} catch (Throwable th) {
				throw ThrowableUtilities.propagateUnverified(th);
			}
		};
	}

	void run() throws TH;
}
