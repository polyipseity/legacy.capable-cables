package $group__.$modId__.utilities.constructs.interfaces.basic;

import $group__.$modId__.utilities.helpers.Throwables;

import javax.annotation.Nullable;
import java.util.Optional;

import static $group__.$modId__.utilities.helpers.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.Throwables.throwThrowable;
import static $group__.$modId__.utilities.helpers.Throwables.unexpected;

public interface IThrowableCatcher {
	/* SECTION methods */

	Optional<Throwable> getCaughtThrowable();

	@Nullable
	default Throwable getCaughtThrowableUnboxed() { return unboxOptional(getCaughtThrowable()); }

	default Throwable getCaughtThrowableUnboxedNonnull() { return getCaughtThrowable().orElseThrow(Throwables::unexpected); }


	void clearCaughtThrowable();


	default void rethrowCaughtThrowable(boolean nullable) throws RuntimeException {
		if (nullable) getCaughtThrowable().ifPresent(Throwables::throwThrowable);
		else throw throwThrowable(getCaughtThrowable().orElseThrow(Throwables::unexpected));
	}

	default RuntimeException rethrowCaughtThrowable() throws RuntimeException {
		rethrowCaughtThrowable(false);
		throw unexpected();
	}
}
