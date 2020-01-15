package etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.templates.components;

import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.IDrawableThemed;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.IThemed;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictEquals.isEquals;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictHashCode.getHashCode;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictToString.getToStringString;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;

@SideOnly(Side.CLIENT)
public class GuiRectangleThemed<N extends Number, T extends IThemed.ITheme<T>, D extends GuiRectangleThemed<N, T, D>> extends GuiRectangle<N, D> implements IDrawableThemed<N, T, D> {
	/* SECTION variables */

	protected T theme;


	/* SECTION constructors */

	public GuiRectangleThemed(Rectangle<N, ?> rect, Color color, T theme) {
		super(rect, color);
		this.theme = theme;
	}

	public GuiRectangleThemed(GuiRectangleThemed<N, T, ?> c) { this(c.getRect(), c.getColor(), c.getTheme()); }


	/* SECTION getters & setters */

	/** {@inheritDoc} */
	@Override
	public void setTheme(T theme) { this.theme = theme; }

	/** {@inheritDoc} */
	@Override
	public T getTheme() { return theme; }


	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	public void draw(Minecraft game) { getTheme().drawRect(getRect(), getColor()); }


	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public D toImmutable() { return castUnchecked((Object) new Immutable<>(this)); }


	/** {@inheritDoc} */
	@Override
	public String toString() { return getToStringString(this, super.toString(),
				new Object[]{"theme", getTheme()}); }

	/** {@inheritDoc} */
	@Override
	public int hashCode() { return getHashCode(this, super.hashCode(), getTheme()); }

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
			t -> getTheme().equals(t.getTheme())); }


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, T extends ITheme<T>, D extends Immutable<N, T, D>> extends GuiRectangleThemed<N, T, D> {
		/* SECTION constructors */

		public Immutable(Rectangle<N, ?> rect, Color color, T theme) { super(rect.toImmutable(), color, theme); }

		public Immutable(GuiRectangleThemed<N, T, ?> c) { this(c.getRect(), c.getColor(), c.getTheme()); }


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


		/* SECTION methods */

		/** {@inheritDoc} */
		@Override
		public D toImmutable() { return castUnchecked(this); }

		/** {@inheritDoc} */
		@Override
		public boolean isImmutable() { return true; }
	}
}
