package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.templates.components;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.IDrawable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.helpers.GuiHelper;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.Objects;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.unexpectedThrowable;

@SideOnly(Side.CLIENT)
public class GuiRectangle<N extends Number, D extends GuiRectangle<N, D>> extends Gui implements IDrawable<N, D> {
	protected Rectangle<N, ?> rect;
	protected Color color;

	public GuiRectangle(Rectangle<N, ?> rect, Color color) {
		this.rect = rect;
		this.color = color;
	}

	public GuiRectangle(GuiRectangle<N, ?> c) { this(c.rect, c.color); }


	public void setRect(Rectangle<N, ?> rect) { this.rect = rect; }

	public Rectangle<N, ?> getRect() { return rect; }

	public void setColor(Color color) { this.color = color; }

	public Color getColor() { return color; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void draw(Minecraft game) { GuiHelper.drawRect(rect, color); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Rectangle<N, ?> specification() { return rect; }

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public D clone() {
		D r;
		try { r = (D) super.clone(); } catch (CloneNotSupportedException | ClassCastException ex) { throw unexpectedThrowable(ex); }
		r.rect = rect.clone();
		r.color = Colors.copyColor(color);
		return r;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof GuiRectangle)) return false;
		GuiRectangle<?, ?> that = (GuiRectangle<?, ?>) o;
		return rect.equals(that.rect) &&
				color.equals(that.color);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() { return Objects.hash(rect, color); }

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({"unchecked"})
	@Override
	public D toImmutable() { return (D) new Immutable<>(this); }

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, D extends Immutable<N, D>> extends GuiRectangle<N, D> {
		public Immutable(Rectangle<N, ?> rect, Color color) { super(rect.toImmutable(), color); }

		public Immutable(GuiRectangle<N, ?> c) { this(c.rect, c.color); }


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
