package $group__.$modId__.client.gui.components;

import $group__.$modId__.client.gui.utilities.constructs.IDrawable;
import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.client.gui.utilities.helpers.Guis;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.meta.When;
import java.awt.*;
import java.util.Optional;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable.tryCloneUnboxedNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictEquals.isEqual;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.helpers.Throwables.unexpected;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class GuiRectangle<N extends Number, T extends GuiRectangle<N, T>> extends Gui implements IDrawable<N, T> {
	/* SECTION variables */

	protected Rectangle<N, ?> rect;
	protected Color color;


	/* SECTION constructors */

	public GuiRectangle(GuiRectangle<N, ?> copy) { this(copy.getRect(), copy.getColor()); }

	public GuiRectangle(Rectangle<N, ?> rect, Color color) {
		this.rect = rect;
		this.color = color;
	}


	/* SECTION getters & setters */

	public Rectangle<N, ?> getRect() { return rect; }

	public void setRect(Rectangle<N, ?> rect) { this.rect = rect; }

	public Color getColor() { return color; }

	public void setColor(Color color) { this.color = color; }


	/* SECTION methods */

	@Override
	public void draw(Minecraft client) { Guis.drawRect(this, getRect(), getColor()); }

	@Override
	public Optional<Rectangle<N, ?>> spec() { return Optional.of(getRect().copy()); }

	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull(new Immutable<>(this)); }

	@Override
	public boolean isImmutable() { return false; }

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public int hashCode() {
		return isImmutable() ? getHashCode(this, super.hashCode(), getRect(), getColor()) : getHashCode(this, super.hashCode());
	}

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public boolean equals(Object o) {
		return isImmutable() ? isEqual(this, o, super.equals(o),
				t -> getRect().equals(t.getRect()),
				t -> getColor().equals(t.getColor())) : isEqual(this, o, super.equals(o));
	}

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T clone() {
		T r;
		try { r = castUncheckedUnboxedNonnull(super.clone()); } catch (CloneNotSupportedException e) {
			throw unexpected(e);
		}
		r.rect = rect.copy();
		r.color = tryCloneUnboxedNonnull(color);
		return r;
	}

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public String toString() {
		return getToStringString(this, super.toString(),
				new Object[]{"rect", getRect()},
				new Object[]{"color", getColor()});
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, T extends Immutable<N, T>> extends GuiRectangle<N, T> {
		/* SECTION constructors */

		public Immutable(GuiRectangle<N, ?> copy) { this(copy.getRect(), copy.getColor()); }

		public Immutable(Rectangle<N, ?> rect, Color color) { super(rect.toImmutable(), tryToImmutableUnboxedNonnull(color)); }


		/* SECTION getters & setters */

		@Override
		@Deprecated
		public void setRect(Rectangle<N, ?> rect) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public void setColor(Color color) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final boolean isImmutable() { return true; }
	}
}
