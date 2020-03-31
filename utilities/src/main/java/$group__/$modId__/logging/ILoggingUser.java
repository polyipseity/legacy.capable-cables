package $group__.$modId__.logging;

import javax.annotation.Nullable;
import java.util.Optional;

import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectUnsupportedOperationIf;

public interface ILoggingUser<T extends ILogging<L>, L> extends ILogging<L> {
	/* SECTION getters & setters */

	T getLogging();

	boolean trySetLogging(T logging);


	default Optional<T> tryGetLogging() { return Optional.of(getLogging()); }

	default void setLogging(T logging) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetLogging(logging)); }


	@Nullable
	@Override
	default L getLogger() { return getLogging().getLogger(); }

	@Override
	default boolean trySetLogger(@Nullable L logger) { return getLogging().trySetLogger(logger); }
}
