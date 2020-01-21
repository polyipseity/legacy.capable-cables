package $group__.$modId__.client.gui.templates.components;

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

import static $group__.$modId__.client.gui.utilities.helpers.Guis.popMatrix;
import static $group__.$modId__.client.gui.utilities.helpers.Guis.pushMatrix;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictEquals.isEquals;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable.tryCloneNonnull;
import static $group__.$modId__.utilities.helpers.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.helpers.Throwables.unexpected;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class GuiRectangle<N extends Number, T extends GuiRectangle<N, T>> extends Gui implements IDrawable<N, T> {
	/* SECTION variables */

	protected Rectangle<N, ?> rect;
	protected Color color;


	/* SECTION constructors */

	public GuiRectangle(Rectangle<N, ?> rect, Color color) {
		this.rect = rect;
		this.color = color;
	}

	public GuiRectangle(GuiRectangle<N, ?> copy) { this(copy.getRect(), copy.getColor()); }


	/* SECTION getters & setters */

	public Rectangle<N, ?> getRect() { return rect; }

	public void setRect(Rectangle<N, ?> rect) { this.rect = rect; }

	public Color getColor() { return color; }

	public void setColor(Color color) { this.color = color; }


	/* SECTION methods */

	@Override
	public void draw(Minecraft client) {
		pushMatrix();
		Guis.drawRect(getRect(), getColor());
		popMatrix();
	}

	@Override
	public Rectangle<N, ?> spec() { return getRect(); }

	@Override
	public T toImmutable() { return castUnchecked(new Immutable<>(this)); }


	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public String toString() { return getToStringString(this, super.toString(),
			new Object[]{"rect", getRect()},
			new Object[]{"color", getColor()}); }

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public int hashCode() { return getHashCode(this, super.hashCode(), getRect(), getColor()); }

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
			t -> getRect().equals(t.getRect()),
			t -> getColor().equals(t.getColor())); }

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T clone() {
		T r;
		try { r = castUnchecked(super.clone()); } catch (CloneNotSupportedException e) { throw unexpected(e); }
		r.rect = rect.clone();
		r.color = tryCloneNonnull(color);
		return r;
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, T extends Immutable<N, T>> extends GuiRectangle<N, T> {
		/* SECTION constructors */

		public Immutable(Rectangle<N, ?> rect, Color color) { super(rect.toImmutable(), tryToImmutableNonnull(color)); }

		public Immutable(GuiRectangle<N, ?> copy) { this(copy.getRect(), copy.getColor()); }


		/* SECTION getters & setters */

		@Override
		public void setRect(Rectangle<N, ?> rect) { throw rejectUnsupportedOperation(); }

		@Override
		public void setColor(Color color) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final T toImmutable() { return castUnchecked(this); }

		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final boolean isImmutable() { return true; }
	}
}
