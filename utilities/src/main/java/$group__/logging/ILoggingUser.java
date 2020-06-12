package $group__.logging;

import $group__.utilities.helpers.specific.Throwables;

import javax.annotation.Nullable;
import java.util.Optional;

public interface ILoggingUser<T extends ILogging<L>, L> extends ILogging<L> {
	/* SECTION getters & setters */

	T getLogging();

	default void setLogging(T logging) throws UnsupportedOperationException { Throwables.rejectUnsupportedOperationIf(!trySetLogging(logging)); }

	boolean trySetLogging(T logging);

	default Optional<T> tryGetLogging() { return Optional.of(getLogging()); }

	@Nullable
	@Override
	default L getLogger() { return getLogging().getLogger(); }

	@Override
	default boolean trySetLogger(@Nullable L logger) { return getLogging().trySetLogger(logger); }
}
