package $group__.$modId__.utilities.constructs.interfaces.basic;

import $group__.$modId__.utilities.helpers.Throwables;

import javax.annotation.Nullable;
import java.util.Optional;

import static $group__.$modId__.utilities.helpers.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.Throwables.throwThrowable;
import static $group__.$modId__.utilities.helpers.Throwables.unexpected;

public interface IThrowableCatcher {
	/* SECTION methods */

	@Nullable
	default Throwable getCaughtThrowableUnboxed() { return unboxOptional(getCaughtThrowable()); }

	Optional<Throwable> getCaughtThrowable();

	default Throwable getCaughtThrowableUnboxedNonnull() { return getCaughtThrowable().orElseThrow(Throwables::unexpected); }

	default boolean caughtThrowable() { return getCaughtThrowable().isPresent(); }


	void clearCaughtThrowable();

	default RuntimeException rethrowCaughtThrowable() throws RuntimeException {
		rethrowCaughtThrowable(false);
		throw unexpected();
	}

	default void rethrowCaughtThrowable(boolean nullable) throws RuntimeException {
		if (nullable) getCaughtThrowable().ifPresent(Throwables::throwThrowable);
		else throw throwThrowable(getCaughtThrowable().orElseThrow(Throwables::unexpected));
	}
}
