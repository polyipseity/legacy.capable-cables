package $group__.$modId__.client.gui.templates.components;

import $group__.$modId__.client.gui.utilities.constructs.Frame;
import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.client.gui.utilities.helpers.Guis;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.helpers.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.meta.When;
import java.awt.*;

import static $group__.$modId__.client.gui.utilities.helpers.Guis.*;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictEquals.isEquals;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class GuiResource<N extends Number, TE extends Number, T extends GuiResource<N, TE, T>> extends GuiRectangle<N, T> {
	/* SECTION variables */

	protected Frame<N, ?> padding;
	protected ResourceLocation resource;
	protected Rectangle<TE, ?> texture;


	/* SECTION constructors */

	public GuiResource(Rectangle<N, ?> rect, Frame<N, ?> padding, ResourceLocation resource, Rectangle<TE, ?> texture) {
		super(rect, Colors.COLORLESS);
		this.padding = padding;
		this.resource = resource;
		this.texture = texture;
	}

	public GuiResource(GuiResource<N, TE, ?> c) { this(c.getRect(), c.getPadding(), c.getResource(), c.getTexture()); }


	/* SECTION getters & setters */

	public Frame<N, ?> getPadding() { return padding; }

	public void setPadding(Frame<N, ?> padding) { this.padding = padding; }

	public ResourceLocation getResource() { return resource; }

	public void setResource(ResourceLocation resource) { this.resource = resource; }

	public Rectangle<TE, ?> getTexture() { return texture; }

	public void setTexture(Rectangle<TE, ?> texture) { this.texture = texture; }

	/** {@inheritDoc} */
	@Override
	public void setColor(Color color) { throw rejectUnsupportedOperation(); }


	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	public void draw(Minecraft client) {
		pushMatrix();
		reset();
		bindTexture(getResource());
		Guis.drawModalRectWithCustomSizedTexture(getRect(), getTexture());
		popMatrix();
	}


	/** {@inheritDoc} */
	@Override
	public T toImmutable() { return castUnchecked((Object) new Immutable<>(this)); }


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
	public T clone() {
		T r = super.clone();
		r.padding = padding.clone();
		r.texture = texture.clone();
		return r;
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, TE extends Number, T extends Immutable<N, TE, T>> extends GuiResource<N, TE, T> {
		/* SECTION constructors */

		public Immutable(Rectangle<N, ?> rect, Frame<N, ?> padding, ResourceLocation resource, Rectangle<TE, ?> texture) { super(rect.toImmutable(), padding.toImmutable(), resource, texture.toImmutable()); }

		public Immutable(GuiResource<N, TE, ?> c) { super(c.getRect(), c.getPadding(), c.getResource(), c.getTexture()); }


		/* SECTION getters & setters */

		/** {@inheritDoc} */
		@Override
		public void setPadding(Frame<N, ?> padding) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setResource(ResourceLocation resource) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setTexture(Rectangle<TE, ?> texture) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		/** {@inheritDoc} */
		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final T toImmutable() { return castUnchecked(this); }

		/** {@inheritDoc} */
		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final boolean isImmutable() { return true; }
	}
}
