package $group__.utilities.extensions;

import $group__.utilities.helpers.specific.Throwables;

@FunctionalInterface
public interface IRunnableThrowable extends Runnable {
	void runT() throws Throwable;

	@Override
	@Deprecated
	default void run() throws RuntimeException {
		try {
			runT();
		} catch (Throwable t) {
			throw Throwables.wrapThrowable(t);
		}
	}
}
