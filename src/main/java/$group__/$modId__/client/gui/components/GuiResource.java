package $group__.$modId__.client.gui.components;

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

import static $group__.$modId__.client.gui.utilities.helpers.Guis.bindTexture;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable.tryCloneUnboxedNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictEquals.isEqual;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class GuiResource<N extends Number, TN extends Number, T extends GuiResource<N, TN, T>> extends GuiRectangle<N, T> {
	/* SECTION variables */

	protected ResourceLocation resource;
	protected Rectangle<TN, ?> texture;


	/* SECTION constructors */

	public GuiResource(GuiResource<N, TN, ?> copy) { this(copy.getRect(), copy.getResource(), copy.getTexture()); }

	public GuiResource(Rectangle<N, ?> rect, ResourceLocation resource, Rectangle<TN, ?> texture) {
		super(rect, Colors.COLORLESS);
		this.resource = resource;
		this.texture = texture;
	}


	/* SECTION getters & setters */

	public ResourceLocation getResource() { return resource; }

	public void setResource(ResourceLocation resource) { this.resource = resource; }

	public Rectangle<TN, ?> getTexture() { return texture; }

	public void setTexture(Rectangle<TN, ?> texture) { this.texture = texture; }

	@Override
	@Deprecated
	public void setColor(Color color) { throw rejectUnsupportedOperation(); }


	/* SECTION methods */

	@Override
	public void draw(Minecraft client) {
		bindTexture(getResource());
		Guis.drawModalRectWithCustomSizedTexture(this, getRect(), getTexture());
	}


	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull((Object) new Immutable<>(this)); }

	@Override
	public boolean isImmutable() { return false; }


	@Override
	public int hashCode() {
		return isImmutable() ? getHashCode(this, super.hashCode(), getResource(), getTexture()) : getHashCode(this, super.hashCode());
	}

	@Override
	public boolean equals(Object o) {
		return isImmutable() ? isEqual(this, o, super.equals(o),
				t -> getResource().equals(t.getResource()),
				t -> getTexture().equals(t.getTexture())) : isEqual(this, o, super.equals(o));
	}

	@Override
	public T clone() {
		T r = super.clone();
		r.resource = tryCloneUnboxedNonnull(resource);
		r.texture = texture.clone();
		return r;
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, TE extends Number, T extends Immutable<N, TE, T>> extends GuiResource<N, TE, T> {
		/* SECTION constructors */

		public Immutable(Rectangle<N, ?> rect, ResourceLocation resource, Rectangle<TE, ?> texture) { super(rect.toImmutable(), tryToImmutableUnboxedNonnull(resource), texture.toImmutable()); }

		public Immutable(GuiResource<N, TE, ?> copy) { super(copy.getRect(), copy.getResource(), copy.getTexture()); }


		/* SECTION getters & setters */

		@Override
		@Deprecated
		public void setResource(ResourceLocation resource) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public void setTexture(Rectangle<TE, ?> texture) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final boolean isImmutable() { return true; }
	}
}
