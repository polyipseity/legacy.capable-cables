package $group__.$modId__.client.gui.components;

import $group__.$modId__.annotations.OverridingStatus;
import $group__.$modId__.client.gui.polygons.Rectangle;
import $group__.$modId__.client.gui.themes.ITheme;
import $group__.$modId__.client.gui.utilities.helpers.Guis;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.awt.*;

import static $group__.$modId__.client.gui.utilities.helpers.Guis.bindTexture;
import static $group__.$modId__.traits.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class GuiResource<T extends GuiResource<T, N, TH, NT>, N extends Number, TH extends ITheme<TH>, NT extends Number> extends GuiRectangle<T, N, TH> {
	/* SECTION variables */

	protected ResourceLocation resource;
	protected Rectangle<NT, ?> texture;


	/* SECTION constructors */

	public GuiResource(Rectangle<N, ?> rectangle, @Nullable TH theme, Logger logger, ResourceLocation resource, Rectangle<NT, ?> texture) {
		super(rectangle, null, theme, logger);
		this.resource = resource;
		this.texture = texture;
	}

	public GuiResource(GuiResource<?, ? extends N, ? extends TH, ? extends NT> copy) { this(copy.getRectangle(), copy.getResource(), copy.getTexture(), copy.getLogger()); }


	/* SECTION getters & setters */

	public ResourceLocation getResource() { return resource; }

	public boolean setResource(ResourceLocation resource) {
		this.resource = resource;
		return true;
	}

	public Rectangle<NT, ?> getTexture() { return texture; }

	public boolean setTexture(Rectangle<NT, ?> texture) {
		this.texture = texture;
		return true;
	}

	@Override
	@Deprecated
	public boolean setColor(Color color) { return false; }


	/* SECTION methods */

	@Override
	public boolean draw(Minecraft client) {
		bindTexture(getResource());
		Guis.drawModalRectWithCustomSizedTexture(this, getRectangle(), getTexture());
		return true;
	}


	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull((Object) new Immutable<>(this)); }

	@Override
	public boolean isImmutable() { return false; }


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, NT extends Number, T extends Immutable<N, NT, T>> extends GuiResource<N, NT, T> {
		/* SECTION constructors */

		public Immutable(Rectangle<N, ?> rect, ResourceLocation resource, Rectangle<NT, ?> texture, Logger logger) { super(rect.toImmutable(), tryToImmutableUnboxedNonnull(resource, logger), texture.toImmutable(), logger); }

		public Immutable(GuiResource<N, NT, ?> copy) { this(copy.getRectangle(), copy.getResource(), copy.getTexture(), copy.getLogger()); }


		/* SECTION getters & setters */

		@Override
		@Deprecated
		public void setLogger(Logger logger) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public boolean setResource(ResourceLocation resource) { throw rejectUnsupportedOperation(); }

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
