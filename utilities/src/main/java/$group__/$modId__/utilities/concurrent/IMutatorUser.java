package $group__.$modId__.utilities.concurrent;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectUnsupportedOperationIf;

public interface IMutatorUser<T extends IMutator> extends IMutator {
	/* SECTION getters & setters */

	T getMutator();

	default void setMutator(T mutator) throws UnsupportedOperationException { rejectUnsupportedOperationIf(trySetMutator(mutator)); }

	boolean trySetMutator(T mutator);

	default Optional<? extends T> tryGetMutator() { return Optional.ofNullable(getMutator()); }

	@Override
	default <T> T mutate(Supplier<T> action, boolean initialize) throws UnsupportedOperationException { return mutate(action, initialize); }

	@Override
	default <T> boolean trySet(Consumer<T> setter, @Nullable T target, boolean initialize) { return trySet(setter,
			target, initialize); }
}
