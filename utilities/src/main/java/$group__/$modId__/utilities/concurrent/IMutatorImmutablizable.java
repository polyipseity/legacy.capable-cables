package $group__.$modId__.utilities.concurrent;

import $group__.$modId__.annotations.InterfaceIntersection;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;

@SuppressWarnings("SpellCheckingInspection")
@InterfaceIntersection
public interface IMutatorImmutablizable<T extends IMutator & IImmutablizable<I>,
		I extends IMutator & IImmutablizable<I>> extends IMutator, IImmutablizable<I> {
	/* SECTION static methods */

	@InterfaceIntersection.InstanceOfMethod
	static boolean instanceof_(Object obj) { return obj instanceof IMutator && obj instanceof IImmutablizable<?>; }

	@InterfaceIntersection.ConversionMethod
	static <T extends IMutator & IImmutablizable<I>, I extends IMutator & IImmutablizable<I>> IMutatorImmutablizable<T
			, I> of(T obj) {
		return obj instanceof IMutatorImmutablizable<?, ?> ? castUncheckedUnboxedNonnull(obj) :
				new IMutatorImmutablizable<T, I>() {
					/* SECTION methods */

					@Override
					public I toImmutable() { return obj.toImmutable(); }

					@Override
					public <O> boolean trySet(Consumer<O> setter, @Nullable O target, boolean initialize) { return obj.trySet(setter, target, initialize); }

					@Override
					public <O> O mutate(Supplier<O> action, boolean initialize) throws UnsupportedOperationException { return obj.mutate(action, initialize); }
				};
	}
}
