package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.templates.components;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.IDrawable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.IThemed;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs.Remarks;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Colors;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.Objects;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.IThemed.tryCastTo;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.*;

@SideOnly(Side.CLIENT)
public class GuiRectangleThemedDrawable<N extends Number, D extends GuiRectangleThemedDrawable<N, D>> extends GuiRectangleThemed<N, D> {
	@Remarks("Mutable")
	protected IDrawable<N, ?> drawable;

	public GuiRectangleThemedDrawable(Rectangle<N, ?> rect, Color color, EnumTheme theme, IDrawable<N, ?> drawable) {
		super(rect, color, theme);
		setDrawable(this, drawable);
	}

	public GuiRectangleThemedDrawable(Rectangle<N, ?> rect, EnumTheme theme, IDrawable<N, ?> drawable) { this(rect, Color.WHITE, theme, drawable); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void draw(Minecraft game) {
		super.draw(game);
		drawable.specification().setOffsetAndSize(rect);
		drawable.draw(game);
	}

	public void setDrawable(IDrawable<N, ?> drawable) { setDrawable(this, drawable); }

	protected static <N extends Number> void setDrawable(GuiRectangleThemedDrawable<N, ?> t, IDrawable<N, ?> d) {
		if (d.isImmutable()) throw rejectArguments(d);
		t.drawable = d;
	}

	public IDrawable<N, ?> getDrawable() { return drawable; }


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTheme(EnumTheme theme) {
		IThemed<EnumTheme> t;
		if ((t = tryCastTo(EnumTheme.class, drawable)) != null) t.setTheme(theme);
		this.theme = theme;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EnumTheme getTheme() { return theme; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public D clone() {
		D r;
		try { r = super.clone(); } catch (ClassCastException ex) {
			throw unexpectedThrowable(ex);
		}
		r.drawable = drawable.clone();
		return r;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof GuiRectangleThemedDrawable)) return false;
		if (!super.equals(o)) return false;
		GuiRectangleThemedDrawable<?, ?> that = (GuiRectangleThemedDrawable<?, ?>) o;
		return drawable.equals(that.drawable) &&
				theme == that.theme;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() { return Objects.hash(super.hashCode(), drawable, theme); }

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public D toImmutable() { return (D) new Immutable<>(this); }

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, D extends Immutable<N, D>> extends GuiRectangleThemedDrawable<N, D> {
		protected final GuiRectangleThemed<N, D> guiRectT = new GuiRectangleThemed<>(this);


		public Immutable(Rectangle<N, ?> rect, Color color, EnumTheme theme, IDrawable<N, ?> drawable) {
			super(rect.toImmutable(), color, theme, drawable);
			drawable.specification().setOffsetAndSize(this.rect);
			this.drawable = drawable.toImmutable();
		}

		public Immutable(Rectangle<N, ?> rect, EnumTheme theme, IDrawable<N, ?> drawable) { this(rect, Colors.COLORLESS, theme, drawable); }

		public Immutable(GuiRectangleThemedDrawable<N, ?> c) { this(c.rect, c.color, c.theme, c.drawable); }


		/**
		 * {@inheritDoc}
		 */
		@Override
		public void draw(Minecraft game) {
			guiRectT.draw(game);
			drawable.draw(game);
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setRect(Rectangle<N, ?> rect) { throw rejectUnsupportedOperation(); }

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setColor(Color color) { throw rejectUnsupportedOperation(); }

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setTheme(EnumTheme theme) { throw rejectUnsupportedOperation(); }

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setDrawable(IDrawable<N, ?> drawable) { throw rejectUnsupportedOperation(); }


		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings("unchecked")
		@Override
		public D toImmutable() { return (D) this; }

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isImmutable() { return true; }
	}
}
