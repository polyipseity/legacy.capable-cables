package $group__.$modId__.utilities.constructs.interfaces.basic;

import $group__.$modId__.utilities.helpers.Throwables;

import javax.annotation.Nullable;
import java.util.Optional;

import static $group__.$modId__.utilities.helpers.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.Throwables.unexpected;
import static $group__.$modId__.utilities.helpers.Throwables.wrapCheckedThrowable;

public interface IThrowableCatcher {
	/* SECTION methods */

	Optional<Throwable> getCaughtThrowable();

	@Nullable
	default Throwable getCaughtThrowableUnboxed() { return unboxOptional(getCaughtThrowable()); }

	default Throwable getCaughtThrowableUnboxedNonnull() { return getCaughtThrowable().orElseThrow(Throwables::unexpected); }


	void clearCaughtThrowable();


	default void rethrowCaughtThrowable(boolean nullable) {
		if (nullable) getCaughtThrowable().ifPresent(t -> { throw wrapCheckedThrowable(t); });
		else throw wrapCheckedThrowable(getCaughtThrowable().orElseThrow(Throwables::unexpected));
	}

	default RuntimeException rethrowCaughtThrowable() {
		rethrowCaughtThrowable(false);
		throw unexpected();
	}
}
