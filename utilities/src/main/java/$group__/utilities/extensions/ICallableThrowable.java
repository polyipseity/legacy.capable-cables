package $group__.utilities.extensions;

import $group__.utilities.helpers.specific.Throwables;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;

@FunctionalInterface
public interface ICallableThrowable<V> extends Callable<V> {
	/* SECTION methods */

	@Nullable
	V callT() throws Throwable;

	@Override
	@Nullable
	@Deprecated
	default V call() throws Exception {
		try {
			return callT();
		} catch (Exception e) {
			throw e;
		} catch (Throwable t) {
			throw Throwables.wrapThrowable(t);
		}
	}
}
