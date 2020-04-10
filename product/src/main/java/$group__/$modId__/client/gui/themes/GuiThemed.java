package $group__.$modId__.client.gui.themes;

import $group__.$modId__.annotations.OverridingStatus;
import $group__.$modId__.logging.ILogging;
import $group__.$modId__.logging.ILoggingUser;
import $group__.$modId__.utilities.concurrent.IMutatorImmutablizable;
import $group__.$modId__.utilities.concurrent.IMutatorUser;
import $group__.$modId__.utilities.extensions.ICloneable;
import $group__.$modId__.utilities.extensions.IStructure;
import org.apache.logging.log4j.Logger;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.meta.When;

import static $group__.$modId__.utilities.Constants.PACKAGE;
import static $group__.$modId__.utilities.concurrent.IMutator.trySetNonnull;
import static $group__.$modId__.utilities.extensions.IStrictEquals.areEqual;
import static $group__.$modId__.utilities.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;

public class GuiThemed<T extends GuiThemed<T, TH>, TH extends ITheme<TH>> implements IStructure<T, T>, ICloneable<T>,
		IMutatorUser<IMutatorImmutablizable<?, ?>>, IThemed<TH>, ILoggingUser<ILogging<Logger>, Logger> {
	/* SECTION constructors */

	protected TH theme;

	protected IMutatorImmutablizable<?, ?> mutator;
	protected ILogging<Logger> logging;


	/* SECTION constructors */

	public GuiThemed(TH theme, IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) {
		this.mutator = trySetNonnull(mutator, mutator, true);
		this.theme = trySetNonnull(getMutator(), theme, true);
		this.logging = trySetNonnull(getMutator(), logging, true);
	}

	public GuiThemed(GuiThemed<?, TH> copy) { this(copy, copy.getMutator()); }


	protected GuiThemed(GuiThemed<?, TH> copy, IMutatorImmutablizable<?, ?> mutator) { this(copy.getTheme(), mutator, copy.logging); }


	/* SECTION getters & setters */

	@Nonnull
	@Override
	public TH getTheme() { return theme; }

	@Override
	public boolean trySetTheme(@Nullable @CheckForNull TH theme) { return theme != null && trySet(t -> this.theme = t, theme); }

	@Nonnull
	@Override
	public IMutatorImmutablizable<?, ?> getMutator() { return mutator; }

	@Override
	public boolean trySetMutator(IMutatorImmutablizable<?, ?> mutator) { return trySet(t -> this.mutator = t, mutator); }

	@Override
	public ILogging<Logger> getLogging() { return logging; }

	@Override
	public boolean trySetLogging(ILogging<Logger> logging) { return trySet(t -> this.logging = t, logging); }


	/* SECTION methods */

	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull(isImmutable() ? this : new GuiThemed<>(this, IMutatorImmutablizable.of(getMutator().toImmutable()))); }

	@Override
	public boolean isImmutable() { return getMutator().isImmutable(); }


	@Override
	public final String toString() { return getToStringString(this, super.toString()); }

	@Override
	public final int hashCode() { return getHashCode(this, super::hashCode); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public final boolean equals(Object obj) { return areEqual(this, obj, super::equals); }

	@SuppressWarnings("Convert2MethodRef")
	@Override
	@OverridingMethodsMustInvokeSuper
	@OverridingStatus(group = PACKAGE, when = When.MAYBE)
	public T clone() { return ICloneable.clone(() -> super.clone(), getLogger()); }
}
