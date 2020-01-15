package etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.templates.components;

import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.Frame;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.helpers.GuiHelper;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.helpers.GuiHelper.bindTexture;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.helpers.GuiHelper.resetColor;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictEquals.isEquals;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictHashCode.getHashCode;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;

@SideOnly(Side.CLIENT)
public class GuiResource<N extends Number, T extends Number, D extends GuiResource<N, T, D>> extends GuiRectangle<N, D> {
	/* SECTION variables */

	protected Frame<N, ?> padding;
	protected ResourceLocation resource;
	protected Rectangle<T, ?> texture;


	/* SECTION constructors */

	public GuiResource(Rectangle<N, ?> rect, Frame<N, ?> padding, ResourceLocation resource, Rectangle<T, ?> texture) {
		super(rect, Colors.COLORLESS);
		this.padding = padding;
		this.resource = resource;
		this.texture = texture;
	}

	public GuiResource(GuiResource<N, T, ?> c) { this(c.getRect(), c.getPadding(), c.getResource(), c.getTexture()); }


	/* SECTION getters & setters */

	public Frame<N, ?> getPadding() { return padding; }

	public void setPadding(Frame<N, ?> padding) { this.padding = padding; }

	public ResourceLocation getResource() { return resource; }

	public void setResource(ResourceLocation resource) { this.resource = resource; }

	public Rectangle<T, ?> getTexture() { return texture; }

	public void setTexture(Rectangle<T, ?> texture) { this.texture = texture; }

	/** {@inheritDoc} */
	@Override
	public void setColor(Color color) { throw rejectUnsupportedOperation(); }


	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	public void draw(Minecraft game) {
		resetColor();
		bindTexture(game, getResource());
		GuiHelper.drawModalRectWithCustomSizedTexture(getRect(), getTexture());
	}


	/** {@inheritDoc} */
	@Override
	public D toImmutable() { return castUnchecked((Object) new Immutable<>(this)); }


	/** {@inheritDoc} */
	@Override
	public int hashCode() { return getHashCode(this, super.hashCode(), getPadding(), getResource(), getTexture()); }

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
			t -> getPadding().equals(t.getPadding()),
			t -> getResource().equals(t.getResource()),
			t -> getTexture().equals(t.getTexture())); }

	/** {@inheritDoc} */
	@Override
	public D clone() {
		D r = super.clone();
		r.padding = padding.clone();
		r.texture = texture.clone();
		return r;
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, T extends Number, D extends Immutable<N, T, D>> extends GuiResource<N, T, D> {
		/* SECTION constructors */

		public Immutable(Rectangle<N, ?> rect, Frame<N, ?> padding, ResourceLocation resource, Rectangle<T, ?> texture) { super(rect.toImmutable(), padding.toImmutable(), resource, texture.toImmutable()); }

		public Immutable(GuiResource<N, T, ?> c) { super(c.getRect(), c.getPadding(), c.getResource(), c.getTexture()); }


		/* SECTION getters & setters */

		/** {@inheritDoc} */
		@Override
		public void setPadding(Frame<N, ?> padding) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setResource(ResourceLocation resource) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setTexture(Rectangle<T, ?> texture) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		/** {@inheritDoc} */
		@Override
		public D toImmutable() { return castUnchecked(this); }

		/** {@inheritDoc} */
		@Override
		public boolean isImmutable() { return true; }
	}
}