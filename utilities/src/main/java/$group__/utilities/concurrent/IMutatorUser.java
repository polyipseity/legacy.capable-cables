package $group__.utilities.concurrent;

import $group__.utilities.helpers.specific.Throwables;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface IMutatorUser<T extends IMutator> extends IMutator {
	/* SECTION getters & setters */

	T getMutator();

	default void setMutator(T mutator) throws UnsupportedOperationException { Throwables.rejectUnsupportedOperationIf(trySetMutator(mutator)); }

	boolean trySetMutator(T mutator);

	default Optional<? extends T> tryGetMutator() { return Optional.ofNullable(getMutator()); }

	@Override
	default <T> T mutate(Supplier<T> action, boolean initialize) throws UnsupportedOperationException { return mutate(action, initialize); }

	@Override
	default <T> boolean trySet(Consumer<T> setter, @Nullable T target, boolean initialize) {
		return trySet(setter,
				target, initialize);
	}
}
