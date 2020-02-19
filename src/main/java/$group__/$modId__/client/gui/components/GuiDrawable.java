package $group__.$modId__.client.gui.components;

import $group__.$modId__.annotations.OverridingStatus;
import $group__.$modId__.client.gui.themes.ITheme;
import $group__.$modId__.client.gui.themes.IThemed;
import $group__.$modId__.client.gui.themes.IThemedUser;
import $group__.$modId__.client.gui.traits.IColored;
import $group__.$modId__.client.gui.traits.IColoredUser;
import $group__.$modId__.client.gui.traits.IDrawable;
import $group__.$modId__.concurrent.IMutatorImmutablizable;
import $group__.$modId__.concurrent.IMutatorUser;
import $group__.$modId__.utilities.extensions.ICloneable;
import $group__.$modId__.utilities.extensions.IStructure;
import $group__.$modId__.logging.ILogging;
import $group__.$modId__.logging.ILoggingUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.meta.When;

import static $group__.$modId__.concurrent.IMutator.trySetNonnull;
import static $group__.$modId__.utilities.extensions.IStrictEquals.areEqual;
import static $group__.$modId__.utilities.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

public abstract class GuiDrawable<T extends GuiDrawable<T, N, C, TH>, N extends Number, C, TH extends ITheme<TH>> extends Gui implements IStructure<T, T>, ICloneable<T>, IMutatorUser<IMutatorImmutablizable<?, ?>>, ILoggingUser<ILogging<Logger>, Logger>, IColoredUser<IColored<C>, C>, IThemedUser<IThemed<TH>, TH>, IDrawable<N> {
	/* SECTION variables */

	protected IMutatorImmutablizable<?, ?> mutator;
	protected ILogging<Logger> logging;
	protected IColored<C> colored;
	protected IThemed<TH> themed;


	/* SECTION constructors */

	public GuiDrawable(IColored<C> colored, IThemed<TH> themed, IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) {
		this.mutator = trySetNonnull(mutator, mutator, true);
		this.colored = trySetNonnull(getMutator(), colored, true);
		this.themed = trySetNonnull(getMutator(), themed, true);
		this.logging = trySetNonnull(getMutator(), logging, true);
	}

	public GuiDrawable(GuiDrawable<?, N, C, TH> copy) { this(copy, copy.getMutator()); }


	protected GuiDrawable(GuiDrawable<?, N, C, TH> copy, IMutatorImmutablizable<?, ?> mutator) { this(copy.getColored(), copy.getThemed(), mutator, copy.getLogging()); }


	/* SECTION getters & setters */

	@Override
	@Nonnull
	public IMutatorImmutablizable<?, ?> getMutator() { return mutator; }

	@Override
	public boolean trySetMutator(IMutatorImmutablizable<?, ?> mutator) { return trySet(t -> this.mutator = t, mutator); }

	public IColored<C> getColored() { return colored; }

	public boolean trySetColored(@Nullable IColored<C> colored) { return trySet(t -> this.colored = t, colored); }

	public IThemed<TH> getThemed() { return themed; }

	public boolean trySetThemed(@Nullable IThemed<TH> themed) { return trySet(t -> this.themed = t, themed); }

	public ILogging<Logger> getLogging() { return logging; }

	public boolean trySetLogging(ILogging<Logger> logging) { return trySet(t -> this.logging = t, logging); }


	/* SECTION methods */

	@Override
	public abstract boolean tryDraw(Minecraft client);


	@Override
	public boolean isImmutable() { return getMutator().isImmutable(); }


	@Override
	@OverridingStatus(group = GROUP)
	public final String toString() { return getToStringString(this, super.toString()); }

	@Override
	@OverridingStatus(group = GROUP)
	public final int hashCode() { return getHashCode(this, super::hashCode); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	@OverridingStatus(group = GROUP)
	public final boolean equals(Object o) { return areEqual(this, o, super::equals); }

	@SuppressWarnings("Convert2MethodRef")
	@Override
	@OverridingMethodsMustInvokeSuper
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T clone() { return ICloneable.clone(() -> super.clone(), getLogger()); }
}
