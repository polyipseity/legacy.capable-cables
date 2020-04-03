package $group__.$modId__.utilities.extensions;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;

import static $group__.$modId__.utilities.helpers.specific.Throwables.wrapThrowable;

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
			throw wrapThrowable(t);
		}
	}
}
