package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.templates.components;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.IDrawable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs.Remarks;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Colors;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.Objects;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.*;

@SideOnly(Side.CLIENT)
public class GuiRectangleDrawable<N extends Number, D extends GuiRectangleDrawable<N, D>> extends GuiRectangle<N, D> {
	@Remarks("Mutable")
	protected IDrawable<N, ?> drawable;

	public GuiRectangleDrawable(Rectangle<N, ?> rect, Color color, IDrawable<N, ?> drawable) {
		super(rect, color);
		setDrawable(this, drawable);
	}

	public GuiRectangleDrawable(Rectangle<N, ?> rect, IDrawable<N, ?> drawable) { this(rect, Colors.COLORLESS, drawable); }

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

	protected static <N extends Number> void setDrawable(GuiRectangleDrawable<N, ?> t, IDrawable<N, ?> d) {
		if (d.isImmutable()) throw rejectArguments(d);
		t.drawable = d;
	}

	public IDrawable<N, ?> getDrawable() { return drawable; }

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
		if (!(o instanceof GuiRectangleDrawable)) return false;
		if (!super.equals(o)) return false;
		GuiRectangleDrawable<?, ?> that = (GuiRectangleDrawable<?, ?>) o;
		return drawable.equals(that.drawable);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() { return Objects.hash(super.hashCode(), drawable); }

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({"unchecked"})
	@Override
	public D toImmutable() { return (D) new Immutable<>(this); }

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, D extends Immutable<N, D>> extends GuiRectangleDrawable<N, D> {
		protected final GuiRectangle<N, D> guiRect = new GuiRectangle<>(this);


		public Immutable(Rectangle<N, ?> rect, Color color, IDrawable<N, ?> drawable) {
			super(rect.toImmutable(), color, drawable);
			drawable.specification().setOffsetAndSize(this.rect);
			this.drawable = drawable.toImmutable();
		}

		public Immutable(Rectangle<N, ?> rect, IDrawable<N, ?> drawable) { this(rect, Colors.COLORLESS, drawable); }

		public Immutable(GuiRectangleDrawable<N, ?> c) { this(c.rect, c.color, c.drawable); }


		/**
		 * {@inheritDoc}
		 */
		@Override
		public void draw(Minecraft game) {
			guiRect.draw(game);
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
		public void setDrawable(IDrawable<N, ?> drawable) { throw rejectUnsupportedOperation(); }


		/** {@inheritDoc} */
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
