package $group__.$modId__.client.gui.components;

import $group__.$modId__.annotations.OverridingStatus;
import $group__.$modId__.client.gui.polygons.Rectangle;
import $group__.$modId__.client.gui.traits.IDrawable;
import $group__.$modId__.utilities.extensions.IStructure;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import javax.annotation.concurrent.Immutable;
import java.util.Optional;

import static $group__.$modId__.utilities.extensions.IStrictEquals.areEqual;
import static $group__.$modId__.utilities.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.PreconditionsExtension.requireRunOnceOnly;
import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static $group__.$modId__.utilities.variables.Globals.LOGGER;

@Immutable
public final class GuiDrawableNull<N extends Number> extends Gui implements IDrawable<N>, IStructure<GuiDrawableNull<N>, GuiDrawableNull<N>> {
	/* SECTION static variables */

	public static final GuiDrawableNull<?> INSTANCE = new GuiDrawableNull<>();


	/* SECTION constructors */

	private GuiDrawableNull() { requireRunOnceOnly(LOGGER); }


	/* SECTION static methods */

	public static <N extends Number> GuiDrawableNull<N> getInstance() { return castUncheckedUnboxedNonnull(INSTANCE); }


	/* SECTION methods */

	@Override
	public boolean tryDraw(Minecraft client) { return false; }


	@Override
	public Optional<Rectangle<?, N>> spec() { return Optional.empty(); }


	@Override
	@OverridingStatus(group = GROUP)
	@Deprecated
	public final GuiDrawableNull<N> toImmutable() { return this; }

	@Override
	@OverridingStatus(group = GROUP)
	public final boolean isImmutable() { return true; }


	@Override
	@OverridingStatus(group = GROUP)
	public final String toString() { return getToStringString(this, super.toString()); }

	@Override
	@OverridingStatus(group = GROUP)
	public final int hashCode() { return getHashCode(this, super::hashCode); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	@OverridingStatus(group = GROUP)
	public final boolean equals(Object obj) { return areEqual(this, obj, super::equals); }
}
