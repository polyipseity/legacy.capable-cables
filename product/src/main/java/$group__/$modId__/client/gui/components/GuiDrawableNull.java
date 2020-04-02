package $group__.$modId__.client.gui.components;

import $group__.$modId__.annotations.OverridingStatus;
import $group__.$modId__.client.gui.polygons.Rectangle;
import $group__.$modId__.client.gui.traits.IDrawable;
import $group__.$modId__.utilities.extensions.IStructure;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.concurrent.Immutable;
import java.util.Optional;

import static $group__.$modId__.utilities.Constants.PACKAGE;
import static $group__.$modId__.utilities.extensions.IStrictEquals.areEqual;
import static $group__.$modId__.utilities.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.PreconditionsExtension.requireRunOnceOnly;

@Immutable
public final class GuiDrawableNull<N extends Number> extends Gui implements IDrawable<N>,
		IStructure<GuiDrawableNull<N>, GuiDrawableNull<N>> {
	/* SECTION static variables */

	public static final GuiDrawableNull<?> INSTANCE = new GuiDrawableNull<>();

	private static final Logger LOGGER = LogManager.getLogger(GuiDrawableNull.class);


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
	@OverridingStatus(group = PACKAGE)
	@Deprecated
	public final GuiDrawableNull<N> toImmutable() { return this; }

	@Override
	@OverridingStatus(group = PACKAGE)
	public final boolean isImmutable() { return true; }


	@Override
	@OverridingStatus(group = PACKAGE)
	public final String toString() { return getToStringString(this, super.toString()); }

	@Override
	@OverridingStatus(group = PACKAGE)
	public final int hashCode() { return getHashCode(this, super::hashCode); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	@OverridingStatus(group = PACKAGE)
	public final boolean equals(Object obj) { return areEqual(this, obj, super::equals); }
}
