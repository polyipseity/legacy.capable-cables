package $group__.$modId__.client.gui.components;

import $group__.$modId__.client.gui.polygons.Rectangle;
import $group__.$modId__.client.gui.themes.ITheme;
import $group__.$modId__.client.gui.themes.IThemed;
import $group__.$modId__.client.gui.utilities.builders.BuilderGuiDrawable;
import $group__.$modId__.client.gui.utilities.Guis;
import $group__.$modId__.utilities.concurrent.IMutatorImmutablizable;
import $group__.$modId__.logging.ILogging;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.Optional;

import static $group__.$modId__.client.gui.utilities.Guis.bindTexture;
import static $group__.$modId__.utilities.concurrent.IMutator.trySetNonnull;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectUnsupportedOperationIf;

@SideOnly(Side.CLIENT)
public class GuiResource<T extends GuiResource<T, N, C, TH, NT>, N extends Number, C extends Color, TH extends ITheme<TH>, NT extends Number> extends GuiRectangle<T, N, C, TH> {
	/* SECTION variables */

	protected ResourceLocation resource;
	protected Rectangle<?, NT> texture;


	/* SECTION constructors */

	public GuiResource(Rectangle<?, N> rectangle, ResourceLocation resource, Rectangle<?, NT> texture, IThemed<TH> themed, IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) {
		super(rectangle, GuiColorNull.getInstance(), themed, mutator, logging);
		this.resource = trySetNonnull(getMutator(), resource, true);
		this.texture = trySetNonnull(getMutator(), texture, true);
	}

	public GuiResource(GuiResource<?, N, ?, TH, NT> copy) { this(copy, copy.getMutator()); }


	protected GuiResource(GuiResource<?, N, ?, TH, NT> copy, IMutatorImmutablizable<?, ?> mutator) { this(copy.getRectangle(), copy.getResource(), copy.getTexture(), copy.getThemed(), mutator, copy.getLogging()); }


	/* SECTION static methods */

	public static <T extends BuilderGuiDrawable<T, V, N, C, TH>, V extends GuiResource<V, N, C, TH, NT>, N extends Number, C extends Color, TH extends ITheme<TH>, NT extends Number> BuilderGuiDrawable<T, V, N, C, TH> newBuilderGR(Rectangle<?, N> rectangle, ResourceLocation resource, Rectangle<?, NT> texture) { return new BuilderGuiDrawable<>(t -> castUncheckedUnboxedNonnull(new GuiResource<>(rectangle, resource, texture, t.themed, t.mutator, t.logging))); }


	/* SECTION getters & setters */

	public ResourceLocation getResource() { return resource; }

	public boolean trySetResource(ResourceLocation resource) { return trySet(t -> this.resource = t, resource); }

	public Optional<ResourceLocation> tryGetResource() { return Optional.of(getResource()); }

	public void setResource(ResourceLocation resource) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetResource(resource)); }

	public Rectangle<?, NT> getTexture() { return texture; }

	public boolean trySetTexture(Rectangle<?, NT> texture) { return trySet(t -> this.texture = t, texture); }

	public Optional<Rectangle<?, NT>> tryGetTexture() { return Optional.of(getTexture()); }

	public void setTexture(Rectangle<?, NT> texture) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetTexture(texture)); }


	/* SECTION methods */

	@Override
	public boolean tryDraw(Minecraft client) {
		bindTexture(getResource());
		Guis.drawModalRectWithCustomSizedTexture(getTheme(), getRectangle(), getTexture());
		return true;
	}

	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull(isImmutable() ? this : new GuiResource<>(this, IMutatorImmutablizable.of(getMutator().toImmutable()))); }
}
