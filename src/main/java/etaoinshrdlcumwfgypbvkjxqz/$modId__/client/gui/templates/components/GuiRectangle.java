package etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.templates.components;

import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.IDrawable;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.helpers.GuiHelper;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.OverridingStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.meta.When;
import java.awt.*;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictEquals.isEquals;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictHashCode.getHashCode;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictToString.getToStringString;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.unexpectedThrowable;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.variables.Constants.GROUP;

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

	public GuiRectangle(GuiRectangle<N, ?> c) { this(c.getRect(), c.getColor()); }


	/* SECTION getters & setters */

	public Rectangle<N, ?> getRect() { return rect; }

	public void setRect(Rectangle<N, ?> rect) { this.rect = rect; }

	public Color getColor() { return color; }

	public void setColor(Color color) { this.color = color; }


	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	public void draw(Minecraft game) { GuiHelper.drawRect(getRect(), getColor()); }

	/** {@inheritDoc} */
	@Override
	public Rectangle<N, ?> specification() { return getRect(); }

	/** {@inheritDoc} */
	@Override
	public T toImmutable() { return castUnchecked(new Immutable<>(this)); }


	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public String toString() { return getToStringString(this, super.toString(),
			new Object[]{"rect", getRect()},
			new Object[]{"color", getColor()}); }

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public int hashCode() { return getHashCode(this, super.hashCode(), getRect(), getColor()); }

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
			t -> getRect().equals(t.getRect()),
			t -> getColor().equals(t.getColor())); }

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T clone() {
		T r;
		try { r = castUnchecked(super.clone()); } catch (CloneNotSupportedException | ClassCastException e) { throw unexpectedThrowable(e); }
		r.rect = rect.clone();
		return r;
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, T extends Immutable<N, T>> extends GuiRectangle<N, T> {
		/* SECTION constructors */

		public Immutable(Rectangle<N, ?> rect, Color color) { super(rect.toImmutable(), color); }

		public Immutable(GuiRectangle<N, ?> c) { this(c.getRect(), c.getColor()); }


		/* SECTION getters & setters */

		/** {@inheritDoc} */
		@Override
		public void setRect(Rectangle<N, ?> rect) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setColor(Color color) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		/** {@inheritDoc} */
		@Override
		public T toImmutable() { return castUnchecked(this); }

		/** {@inheritDoc} */
		@Override
		public boolean isImmutable() { return true; }
	}
}
