package $group__.$modId__.traits.basic;

import javax.annotation.Nullable;
import java.util.Optional;

@FunctionalInterface
public interface ILogging<T> {
	/* SECTION methods */

	Optional<T> getLogger();

	default boolean setLogger(@SuppressWarnings("unused") @Nullable T logger) { return false; }
}
