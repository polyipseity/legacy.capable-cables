package $group__.logging;

import $group__.utilities.concurrent.IMutatorImmutablizable;
import $group__.utilities.helpers.specific.Throwables;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;

public interface ILogging<T> {
	static <T extends Logger> ILogging<T> of(@Nullable T logger, IMutatorImmutablizable<?, ?> mutator) { return logger == null ? LoggingNull.getInstance() : new LoggingLog4J<>(logger, mutator); }


	@Nullable
	T getLogger();

	default void setLogger(@Nullable T logger) throws UnsupportedOperationException { Throwables.rejectUnsupportedOperationIf(!trySetLogger(logger)); }

	boolean trySetLogger(@Nullable T logger);

	default Optional<? extends T> tryGetLogger() { return Optional.ofNullable(getLogger()); }
}
