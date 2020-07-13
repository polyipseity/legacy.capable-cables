package $group__.utilities.concurrent;

import $group__.utilities.helpers.specific.Throwables;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface IMutatorUser<T extends IMutator> extends IMutator {
	T getMutator();

	default void setMutator(T mutator) throws UnsupportedOperationException { Throwables.rejectUnsupportedOperationIf(trySetMutator(mutator)); }

	boolean trySetMutator(T mutator);

	default Optional<? extends T> tryGetMutator() { return Optional.of(getMutator()); }

	@Override
	default <A> A mutate(Supplier<A> action, boolean initialize) throws UnsupportedOperationException { return getMutator().mutate(action, initialize); }

	@Override
	default <A> boolean trySet(Consumer<A> setter, @Nullable A target, boolean initialize) { return getMutator().trySet(setter, target, initialize); }
}
