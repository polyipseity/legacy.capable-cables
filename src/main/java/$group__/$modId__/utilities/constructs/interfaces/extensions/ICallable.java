package $group__.$modId__.utilities.constructs.interfaces.extensions;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;

import static $group__.$modId__.utilities.helpers.Throwables.wrapCheckedThrowable;

public interface ICallable<V> extends Callable<V> {
	/* SECTION methods */

	@Nullable
	V callT() throws Throwable;

	@Override
	@Nullable
	default V call() throws Exception {
		try {
			return callT();
		} catch (Exception e) {
			throw e;
		} catch (Throwable throwable) {
			throw wrapCheckedThrowable(throwable);
		}
	}
}
