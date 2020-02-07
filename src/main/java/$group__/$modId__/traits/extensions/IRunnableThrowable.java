package $group__.$modId__.traits.extensions;

import static $group__.$modId__.utilities.helpers.specific.Throwables.wrapThrowable;

@FunctionalInterface
public interface IRunnableThrowable extends Runnable {
	/* SECTION methods */

	void runT() throws Throwable;

	@Override
	@Deprecated
	default void run() throws RuntimeException {
		try {
			runT();
		} catch (Throwable t) {
			throw wrapThrowable(t);
		}
	}
}
