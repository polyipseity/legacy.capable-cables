package etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.templates.components;

import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.IDrawable;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Colors;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictEquals.isEquals;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictHashCode.getHashCode;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictToString.getToStringString;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.rejectArguments;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;

@SideOnly(Side.CLIENT)
public class GuiRectangleDrawable<N extends Number, D extends GuiRectangleDrawable<N, D>> extends GuiRectangle<N, D> {
	/* SECTION variables */

	protected IDrawable<N, ?> drawable;


	/* SECTION constructors */

	public GuiRectangleDrawable(Rectangle<N, ?> rect, Color color, /* REMARKS mutable */ IDrawable<N, ?> drawable) {
		super(rect, color);
		setDrawable(this, drawable);
	}

	public GuiRectangleDrawable(Rectangle<N, ?> rect, /* REMARKS mutable */ IDrawable<N, ?> drawable) { this(rect, Colors.COLORLESS, drawable); }

	public GuiRectangleDrawable(GuiRectangleDrawable<N, ?> c) { this(c.getRect(), c.getColor(), c.getDrawable()); }


	/* SECTION getters & setters */

	public IDrawable<N, ?> getDrawable() { return drawable; }

	public void setDrawable(IDrawable<N, ?> drawable) { setDrawable(this, drawable); }


	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	public void draw(Minecraft game) {
		super.draw(game);
		IDrawable<N, ?> d = getDrawable();
		d.specification().setOffsetAndSize(getRect());
		d.draw(game);
	}


	/** {@inheritDoc} */
	@Override
	public D toImmutable() { return castUnchecked((Object) new Immutable<>(this)); }

	/** {@inheritDoc} */
	@Override
	public String toString() { return getToStringString(this, super.toString(),
			new Object[]{"drawable", getDrawable()}); }

	/** {@inheritDoc} */
	@Override
	public int hashCode() { return getHashCode(this, super.hashCode(), getDrawable()); }

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
			t -> getDrawable().equals(t.getDrawable())); }

	/** {@inheritDoc} */
	@Override
	public D clone() {
		D r = super.clone();
		r.drawable = drawable.clone();
		return r;
	}


	/* SECTION static methods */

	protected static <N extends Number> void setDrawable(GuiRectangleDrawable<N, ?> t, IDrawable<N, ?> d) {
		if (d.isImmutable()) throw rejectArguments(d);
		t.drawable = d;
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, D extends Immutable<N, D>> extends GuiRectangleDrawable<N, D> {
		/* SECTION variables */

		/* REMARKS internal */ protected final GuiRectangle<N, D> guiRect = new GuiRectangle<>(this);


		/* SECTION constructors */

		public Immutable(Rectangle<N, ?> rect, Color color, IDrawable<N, ?> drawable) {
			super(rect.toImmutable(), color, drawable);
			drawable.specification().setOffsetAndSize(getRect());
			this.drawable = drawable.toImmutable();
		}

		public Immutable(Rectangle<N, ?> rect, IDrawable<N, ?> drawable) { this(rect, Colors.COLORLESS, drawable); }

		public Immutable(GuiRectangleDrawable<N, ?> c) { this(c.getRect(), c.getColor(), c.getDrawable()); }


		/* SECTION getters & setter */

		/** {@inheritDoc} */
		@Override
		public void setRect(Rectangle<N, ?> rect) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setColor(Color color) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setDrawable(IDrawable<N, ?> drawable) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		/** {@inheritDoc} */
		@Override
		public void draw(Minecraft game) {
			guiRect.draw(game); // CODE super.super.draw(game);
			getDrawable().draw(game);
		}


		/** {@inheritDoc} */
		@Override
		public D toImmutable() { return castUnchecked(this); }

		/** {@inheritDoc} */
		@Override
		public boolean isImmutable() { return true; }
	}
}
