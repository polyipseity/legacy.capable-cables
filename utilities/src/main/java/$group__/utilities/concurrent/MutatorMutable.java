package $group__.utilities.concurrent;

import $group__.utilities.extensions.IStrictObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static $group__.utilities.extensions.IStrictEquals.areEqual;
import static $group__.utilities.extensions.IStrictHashCode.getHashCode;
import static $group__.utilities.extensions.IStrictToString.getToStringString;
import static $group__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.utilities.helpers.PreconditionsExtension.requireRunOnceOnly;

@Immutable
public class MutatorMutable<T extends MutatorMutable<T, I>, I extends MutatorImmutable<I>> implements Serializable,
		IStrictObject, IMutatorImmutablizable<T, I> {
	/* SECTION static variables */

	public static final MutatorMutable<?, ?> INSTANCE;

	private static final Logger LOGGER = LogManager.getLogger(MutatorMutable.class);
	private static final long serialVersionUID = -970134255526874348L;


	/* SECTION static initializer */

	static {
		INSTANCE = new MutatorMutable<>(null);
	}


	/* SECTION constructors */

	@SuppressWarnings("unused")
	protected MutatorMutable() {}

	private MutatorMutable(@SuppressWarnings({"unused", "SameParameterValue"}) @Nullable Object u) { requireRunOnceOnly(LOGGER); }


	/* SECTION getters & setters */

	@Override
	public <A> A mutate(Supplier<A> action, boolean initialize) { return action.get(); }

	@Override
	public <A> boolean trySet(Consumer<A> setter, @Nullable A target, boolean initialize) {
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
