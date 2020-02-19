package $group__.$modId__.concurrent;

import $group__.$modId__.utilities.extensions.IStrictObject;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static $group__.$modId__.utilities.extensions.IStrictEquals.areEqual;
import static $group__.$modId__.utilities.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.PreconditionsExtension.requireRunOnceOnly;
import static $group__.$modId__.utilities.variables.Globals.LOGGER;

@Immutable
public class MutatorMutable<T extends MutatorMutable<T, I>, I extends MutatorImmutable<I>> implements Serializable, IStrictObject, IMutatorImmutablizable<T, I> {
	/* SECTION static variables */

	public static final MutatorMutable<?, ?> INSTANCE = new MutatorMutable<>(null);

	private static final long serialVersionUID = -970134255526874348L;


	/* SECTION constructors */

	@SuppressWarnings("unused")
	protected MutatorMutable() {}

	private MutatorMutable(@SuppressWarnings("unused") @Nullable Object u) { requireRunOnceOnly(LOGGER); }


	/* SECTION getters & setters */

	@Override
	public <T> T mutate(Supplier<T> action, boolean initialize) { return action.get(); }

	@Override
	public <T> boolean trySet(Consumer<T> setter, @Nullable T target, boolean initialize) {
		setter.accept(target);
		return true;
	}


	/* SECTION methods */

	@Override
	public I toImmutable() { return castUncheckedUnboxedNonnull(MutatorImmutable.INSTANCE); }

	@Override
	public boolean isImmutable() { return false; }


	@Override
	public final String toString() { return getToStringString(this, super.toString()); }

	@Override
	public final int hashCode() { return getHashCode(this, super::hashCode); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public final boolean equals(Object obj) { return areEqual(this, obj, super::equals); }
}
