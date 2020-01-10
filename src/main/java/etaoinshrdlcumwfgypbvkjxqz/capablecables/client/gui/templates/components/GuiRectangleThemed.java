package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.templates.components;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.IDrawableThemed;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.IThemed.EnumTheme;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.polygons.Rectangle;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.Objects;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.rejectUnsupportedOperation;

@SideOnly(Side.CLIENT)
public class GuiRectangleThemed<N extends Number, D extends GuiRectangleThemed<N, D>> extends GuiRectangle<N, D> implements IDrawableThemed<N, D, EnumTheme> {
	protected EnumTheme theme;


	public GuiRectangleThemed(Rectangle<N, ?> rect, Color color, EnumTheme theme) {
		super(rect, color);
		this.theme = theme;
	}

	public GuiRectangleThemed(GuiRectangleThemed<N, ?> c) { this(c.rect, c.color, c.theme); }


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void draw(Minecraft game) { theme.getHandler().drawRect(rect, color); }


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTheme(EnumTheme theme) { this.theme = theme; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EnumTheme getTheme() { return theme; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof GuiRectangleThemed)) return false;
		if (!super.equals(o)) return false;
		GuiRectangleThemed<?, ?> that = (GuiRectangleThemed<?, ?>) o;
		return theme == that.theme;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() { return Objects.hash(super.hashCode(), theme); }

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public D toImmutable() { return (D) new Immutable<>(this); }

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, D extends Immutable<N, D>> extends GuiRectangleThemed<N, D> {
		public Immutable(Rectangle<N, ?> rect, Color color, EnumTheme theme) { super(rect.toImmutable(), color, theme); }

		public Immutable(GuiRectangleThemed<N, ?> c) { this(c.rect, c.color, c.theme); }

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
