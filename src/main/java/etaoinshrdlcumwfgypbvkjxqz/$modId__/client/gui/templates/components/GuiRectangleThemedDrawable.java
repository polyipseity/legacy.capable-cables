package etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.templates.components;

import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.IDrawable;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.IThemed;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Colors;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.IThemed.tryCastThemedTo;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictEquals.isEquals;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictHashCode.getHashCode;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictToString.getToStringString;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.rejectArguments;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;

@SideOnly(Side.CLIENT)
public class GuiRectangleThemedDrawable<N extends Number, T extends IThemed.ITheme<T>, D extends GuiRectangleThemedDrawable<N, T, D>> extends GuiRectangleThemed<N, T, D> {
	/* SECTION variables */

	protected IDrawable<N, ?> drawable;


	/* SECTION constructors */

	public GuiRectangleThemedDrawable(Rectangle<N, ?> rect, Color color, T theme, /* REMARKS mutable */ IDrawable<N, ?> drawable) {
		super(rect, color, theme);
		setDrawable(this, drawable);
	}

	public GuiRectangleThemedDrawable(Rectangle<N, ?> rect, T theme, /* REMARKS mutable */ IDrawable<N, ?> drawable) { this(rect, Color.WHITE, theme, drawable); }

	public GuiRectangleThemedDrawable(GuiRectangleThemedDrawable<N, T, ?> c) { this(c.getRect(), c.getColor(), c.getTheme(), c.getDrawable()); }


	/* SECTION getters & setters */

	public IDrawable<N, ?> getDrawable() { return drawable; }

	public void setDrawable(IDrawable<N, ?> drawable) { setDrawable(this, drawable); }

	/** {@inheritDoc} */
	@Override
	public void setTheme(T theme) {
		IThemed<T> t;
		if ((t = tryCastThemedTo(drawable)) != null) t.setTheme(theme);
		super.setTheme(theme);
	}


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
	public String toString() { return getToStringString(this, super.toString(),
				new Object[]{"drawable", getDrawable()}); }

	/** {@inheritDoc} */
	@Override
	public int hashCode() { return getHashCode(this, super.hashCode(), getDrawable(), getTheme()); }

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


	/** {@inheritDoc} */
	@Override
	public D toImmutable() { return castUnchecked((Object) new Immutable<>(this)); }


	/* SECTION static methods */

	protected static <N extends Number, T extends ITheme<T>> void setDrawable(GuiRectangleThemedDrawable<N, T, ?> t, IDrawable<N, ?> d) {
		if (d.isImmutable()) throw rejectArguments(d);
		t.drawable = d;
		t.setTheme(t.getTheme());
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, T extends ITheme<T>, D extends Immutable<N, T, D>> extends GuiRectangleThemedDrawable<N, T, D> {
		/* SECTION variables */

		/* REMARKS internal */ protected final GuiRectangleThemed<N, T, D> guiRectT = new GuiRectangleThemed<>(this);


		/* SECTION constructors */

		public Immutable(Rectangle<N, ?> rect, Color color, T theme, IDrawable<N, ?> drawable) {
			super(rect.toImmutable(), color, theme, drawable);
			drawable.specification().setOffsetAndSize(getRect());
			this.drawable = drawable.toImmutable();
		}

		public Immutable(Rectangle<N, ?> rect, T theme, IDrawable<N, ?> drawable) { this(rect, Colors.COLORLESS, theme, drawable); }

		public Immutable(GuiRectangleThemedDrawable<N, T, ?> c) { this(c.getRect(), c.getColor(), c.getTheme(), c.getDrawable()); }


		/* SECTION getters & setters */

		/** {@inheritDoc} */
		@Override
		public void setRect(Rectangle<N, ?> rect) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setColor(Color color) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setTheme(T theme) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setDrawable(IDrawable<N, ?> drawable) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		/** {@inheritDoc} */
		@Override
		public void draw(Minecraft game) {
			guiRectT.draw(game); // CODE super.super.draw(game);
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
