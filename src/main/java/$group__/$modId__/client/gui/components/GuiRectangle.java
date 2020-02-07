package $group__.$modId__.client.gui.components;

import $group__.$modId__.annotations.OverridingStatus;
import $group__.$modId__.client.gui.polygons.Rectangle;
import $group__.$modId__.client.gui.themes.ITheme;
import $group__.$modId__.client.gui.traits.IColored;
import $group__.$modId__.client.gui.traits.IDrawable;
import $group__.$modId__.client.gui.traits.IThemed;
import $group__.$modId__.client.gui.utilities.helpers.Guis;
import $group__.$modId__.traits.IStructure;
import $group__.$modId__.traits.basic.ILogging;
import $group__.$modId__.traits.extensions.ICloneable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.meta.When;
import java.awt.*;
import java.util.Optional;

import static $group__.$modId__.traits.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.traits.extensions.IStrictEquals.isEqual;
import static $group__.$modId__.traits.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.traits.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.specific.Colors.COLORLESS;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class GuiRectangle<T extends GuiRectangle<T, N, TH>, N extends Number, TH extends ITheme<TH>> extends Gui implements IStructure<T>, ICloneable<T>, IDrawable, IColored<Color>, IThemed<TH>, ILogging<Logger> {
	/* SECTION variables */

	protected Rectangle<N, ?> rectangle;
	@Nullable protected Color color;
	@Nullable protected TH theme;
	protected Logger logger;


	/* SECTION constructors */

	public GuiRectangle(GuiRectangle<N, ?> copy) { this(copy.getRectangle(), copy.getColor(), copy.getTheme(), copy.getLogger()); }

	public GuiRectangle(Rectangle<N, ?> rectangle, @Nullable Color color, @Nullable TH theme, Logger logger) {
		this.rectangle = rectangle;
		this.color = color;
		this.theme = theme;
		this.logger = logger;
	}


	/* SECTION getters & setters */

	public Rectangle<N, ?> getRectangle() { return rectangle; }

	public boolean setRectangle(Rectangle<N, ?> rectangle) {
		this.rectangle = rectangle;
		return true;
	}

	@Override
	public Optional<Color> getColor() { return Optional.ofNullable(color); }

	@Override
	public boolean setColor(Color color) {
		this.color = color;
		return true;
	}

	@Override
	public Optional<Logger> getLogger() { return Optional.of(logger); }

	@Override
	public boolean setLogger(Logger logger) {
		this.logger = logger;
		return true;
	}


	/* SECTION methods */

	@Override
	public boolean draw(Minecraft client) {
		Guis.drawRectangle(this, getRectangle(), getColor().orElse(COLORLESS));
		return true;
	}

	@Override
	public Optional<Rectangle.Immutable<N, ?>> spec() {
	}

	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull(new Immutable<>(this)); }

	@Override
	public boolean isImmutable() { return false; }


	@Override
	@OverridingStatus(group = GROUP)
	public final int hashCode() { return getHashCode(this, super::hashCode); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	@OverridingStatus(group = GROUP)
	public final boolean equals(Object o) { return isEqual(this, o, super::equals); }

	@SuppressWarnings("Convert2MethodRef")
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	@OverridingMethodsMustInvokeSuper
	public T clone() { return ICloneable.clone(() -> super.clone(), getLogger()); }

	@Override
	@OverridingStatus(group = GROUP)
	public final String toString() { return getToStringString(this, super.toString()); }


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<T extends Immutable<T, N, TH>, N extends Number, TH extends ITheme<TH>> extends GuiRectangle<T, N, TH> {
		/* SECTION constructors */

		public Immutable(GuiRectangle<N, ?> copy) { this(copy.getRectangle(), copy.getColor(), copy.getLogger()); }

		public Immutable(Rectangle<N, ?> rect, Color color, Logger logger) { super(rect.toImmutable(), tryToImmutableUnboxedNonnull(color, logger), logger); }


		/* SECTION getters & setters */

		@Override
		@Deprecated
		public boolean setRectangle(Rectangle<N, ?> rectangle) { return super.setRectangle(rectangle); }

		@Override
		@Deprecated
		public boolean setColor(Color color) { return super.setColor(color); }

		@Override
		@Deprecated
		public boolean setTheme(TH theme) { return false; }

		@Override
		@Deprecated
		public boolean setLogger(Logger logger) { return false; }


		/* SECTION methods */

		@Override
		@OverridingStatus(group = GROUP)
		public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

		@Override
		@OverridingStatus(group = GROUP)
		public final boolean isImmutable() { return true; }
	}
}
