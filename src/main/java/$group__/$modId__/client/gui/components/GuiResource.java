package $group__.$modId__.client.gui.components;

import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.client.gui.utilities.helpers.Guis;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.helpers.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

import static $group__.$modId__.client.gui.utilities.helpers.Guis.bindTexture;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class GuiResource<N extends Number, NT extends Number, T extends GuiResource<N, NT, T>> extends GuiRectangle<N, T> {
	/* SECTION variables */

	protected ResourceLocation resource;
	protected Rectangle<NT, ?> texture;


	/* SECTION constructors */

	public GuiResource(Rectangle<N, ?> rect, ResourceLocation resource, Rectangle<NT, ?> texture) {
		super(rect, Colors.COLORLESS);
		this.resource = resource;
		this.texture = texture;
	}

	public GuiResource(GuiResource<N, NT, ?> copy) { this(copy.getRect(), copy.getResource(), copy.getTexture()); }


	/* SECTION getters & setters */

	public ResourceLocation getResource() { return resource; }

	public void setResource(ResourceLocation resource) {
		this.resource = resource;
		markDirty();
	}

	public Rectangle<NT, ?> getTexture() { return texture; }

	public void setTexture(Rectangle<NT, ?> texture) {
		this.texture = texture;
		markDirty();
	}

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


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, NT extends Number, T extends Immutable<N, NT, T>> extends GuiResource<N, NT, T> {
		/* SECTION constructors */

		public Immutable(Rectangle<N, ?> rect, ResourceLocation resource, Rectangle<NT, ?> texture) { super(rect.toImmutable(), tryToImmutableUnboxedNonnull(resource), texture.toImmutable()); }

		public Immutable(GuiResource<N, NT, ?> copy) { this(copy.getRect(), copy.getResource(), copy.getTexture()); }


		/* SECTION getters & setters */

		@Override
		@Deprecated
		public void setResource(ResourceLocation resource) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public void setTexture(Rectangle<NT, ?> texture) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		@Override
		@OverridingStatus(group = GROUP)
		public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

		@Override
		@OverridingStatus(group = GROUP)
		public final boolean isImmutable() { return true; }
	}
}
