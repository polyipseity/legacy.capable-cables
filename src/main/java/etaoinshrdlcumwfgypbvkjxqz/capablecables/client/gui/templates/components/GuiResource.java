package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.templates.components;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.Frame;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.helpers.GuiHelper;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.Objects;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.helpers.GuiHelper.bindTexture;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.helpers.GuiHelper.resetColor;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.unexpectedThrowable;

@SideOnly(Side.CLIENT)
public class GuiResource<N extends Number, T extends Number, D extends GuiResource<N, T, D>> extends GuiRectangle<N, D> {
	protected Frame<N, ?> padding;
	protected ResourceLocation resource;
	protected Rectangle<T, ?> texture;

	public GuiResource(Rectangle<N, ?> rect, Frame<N, ?> padding, ResourceLocation resource, Rectangle<T, ?> texture) {
		super(rect, Colors.COLORLESS);
		this.padding = padding;
		this.resource = resource;
		this.texture = texture;
	}

	public void setPadding(Frame<N, ?> padding) { this.padding = padding; }

	public Frame<N, ?> getPadding() { return padding; }

	public void setResource(ResourceLocation resource) { this.resource = resource; }

	public ResourceLocation getResource() { return resource; }

	public void setTexture(Rectangle<T, ?> texture) { this.texture = texture; }

	public Rectangle<T, ?> getTexture() { return texture; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setColor(Color color) { throw rejectUnsupportedOperation(); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void draw(Minecraft game) {
		resetColor();
		bindTexture(game, resource);
		GuiHelper.drawModalRectWithCustomSizedTexture(rect, texture);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public D clone() {
		D r;
		try { r = super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); }
		r.padding = padding.clone();
		r.resource = resource;
		r.texture = texture.clone();
		return r;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof GuiResource)) return false;
		if (!super.equals(o)) return false;
		GuiResource<?, ?, ?> that = (GuiResource<?, ?, ?>) o;
		return padding.equals(that.padding) &&
				resource.equals(that.resource) &&
				texture.equals(that.texture);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() { return Objects.hash(super.hashCode(), padding, resource, texture); }

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public D toImmutable() { return (D) new Immutable<>(this); }

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, T extends Number, D extends Immutable<N, T, D>> extends GuiResource<N, T, D> {
		public Immutable(Rectangle<N, ?> rect, Frame<N, ?> padding, ResourceLocation resource, Rectangle<T, ?> texture) { super(rect.toImmutable(), padding.toImmutable(), resource, texture.toImmutable()); }

		public Immutable(GuiResource<N, T, ?> c) { super(c.rect, c.padding, c.resource, c.texture); }


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
