package $group__.client.gui.components;

import $group__.annotations.OverridingStatus;
import $group__.client.gui.polygons.Rectangle;
import $group__.client.gui.traits.IDrawable;
import $group__.utilities.Constants;
import $group__.utilities.extensions.IStructure;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.concurrent.Immutable;
import java.util.Optional;

import static $group__.utilities.extensions.IStrictEquals.areEqual;
import static $group__.utilities.extensions.IStrictHashCode.getHashCode;
import static $group__.utilities.extensions.IStrictToString.getToStringString;
import static $group__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.utilities.helpers.PreconditionsExtension.requireRunOnceOnly;

@Immutable
public final class GuiDrawableNull<N extends Number> extends Gui implements IDrawable<N>,
		IStructure<GuiDrawableNull<N>, GuiDrawableNull<N>> {
	public static final GuiDrawableNull<?> INSTANCE;

	private static final Logger LOGGER = LogManager.getLogger(GuiDrawableNull.class);


	static {
		INSTANCE = new GuiDrawableNull<>();
	}


	private GuiDrawableNull() { requireRunOnceOnly(LOGGER); }


	public static <N extends Number> GuiDrawableNull<N> getInstance() { return castUncheckedUnboxedNonnull(INSTANCE); }


	@Override
	public boolean tryDraw(Minecraft client) { return false; }


	@Override
	public Optional<Rectangle<?, N>> spec() { return Optional.empty(); }


	@Override
	@OverridingStatus(group = Constants.PACKAGE)
	@Deprecated
	public final GuiDrawableNull<N> toImmutable() { return this; }

	@Override
	@OverridingStatus(group = Constants.PACKAGE)
	public final boolean isImmutable() { return true; }


	@Override
	@OverridingStatus(group = Constants.PACKAGE)
	public final String toString() { return getToStringString(this, super.toString()); }

	@Override
	@OverridingStatus(group = Constants.PACKAGE)
	public final int hashCode() { return getHashCode(this, super::hashCode); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	@OverridingStatus(group = Constants.PACKAGE)
	public final boolean equals(Object obj) { return areEqual(this, obj, super::equals); }
}
