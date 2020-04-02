package $group__.$modId__.logging;

import $group__.$modId__.utilities.concurrent.IMutatorImmutablizable;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;

import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectUnsupportedOperationIf;

public interface ILogging<T> {
	/* SECTION static methods */

	static <T extends Logger> ILogging<T> of(@Nullable T logger, IMutatorImmutablizable<?, ?> mutator) { return logger == null ? LoggingNull.getInstance() : new LoggingLog4J<>(logger, mutator); }


	/* SECTION getters & setters */

	@Nullable
	T getLogger();

	default void setLogger(@Nullable T logger) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetLogger(logger)); }

	boolean trySetLogger(@Nullable T logger);

	default Optional<? extends T> tryGetLogger() { return Optional.ofNullable(getLogger()); }
}
