package $group__.utilities.throwables;

import $group__.utilities.helpers.specific.Throwables;

import javax.annotation.Nullable;
import java.util.Optional;

import static $group__.utilities.helpers.specific.Throwables.throwThrowable;
import static $group__.utilities.helpers.specific.Throwables.unexpected;

public interface IThrowableCatcher {
	@Nullable
	Throwable getCaughtThrowable();

	default Optional<Throwable> tryGetCaughtThrowable() { return Optional.ofNullable(getCaughtThrowable()); }

	default Throwable getCaughtThrowableNonnull() { return tryGetCaughtThrowable().orElseThrow(Throwables::unexpected); }

	void clearCaughtThrowable();

	default boolean caughtThrowable() { return getCaughtThrowable() != null; }

	default void rethrowCaughtThrowable(boolean nullable) throws RuntimeException {
		if (nullable) tryGetCaughtThrowable().ifPresent(Throwables::throwThrowable);
		else throw throwThrowable(getCaughtThrowableNonnull());
	}

	default RuntimeException rethrowCaughtThrowable() throws RuntimeException {
		rethrowCaughtThrowable(false);
		throw unexpected();
	}
}
