package $group__.client.gui.components;

import $group__.client.gui.polygons.Rectangle;
import $group__.client.gui.themes.GuiThemedNull;
import $group__.client.gui.themes.ITheme;
import $group__.client.gui.traits.IDrawable;
import $group__.client.gui.utilities.builders.BuilderGuiDrawable;
import $group__.logging.ILogging;
import $group__.utilities.concurrent.IMutatorImmutablizable;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.Optional;

import static $group__.utilities.concurrent.IMutator.trySetNonnull;
import static $group__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.utilities.helpers.specific.Throwables.rejectUnsupportedOperationIf;
import static net.minecraft.client.renderer.GlStateManager.popMatrix;
import static net.minecraft.client.renderer.GlStateManager.pushMatrix;

@SideOnly(Side.CLIENT)
public class GuiRectangleMatcher<T extends GuiRectangleMatcher<T, N, C, TH, D>, N extends Number, C extends Color,
		TH extends ITheme<TH>, D extends IDrawable<?>> extends GuiRectangle<T, N, C, TH> {
	protected D drawable;


	public GuiRectangleMatcher(Rectangle<?, N> rectangle, D drawable, IMutatorImmutablizable<?, ?> mutator,
	                           ILogging<Logger> logging) {
		super(rectangle, GuiColorNull.getInstance(), GuiThemedNull.getInstance(), mutator, logging);
		this.drawable = trySetNonnull(getMutator(), drawable, true);
	}

	public GuiRectangleMatcher(GuiRectangleMatcher<?, N, ?, ?, ? extends D> copy) { this(copy, copy.getMutator()); }


	protected GuiRectangleMatcher(GuiRectangleMatcher<?, N, ?, ?, ? extends D> copy,
	                              IMutatorImmutablizable<?, ?> mutator) {
		this(copy.getRectangle(), copy.getDrawable()
				, mutator, copy.getLogging());
	}


	public static <T extends BuilderGuiDrawable<T, V, N, C, TH>, V extends GuiRectangleMatcher<V, N, C, TH, D>, N extends Number, C extends Color, TH extends ITheme<TH>, D extends IDrawable<?>> BuilderGuiDrawable<T, V, N, C, TH> newBuilderGuiRectangleMatcher(Rectangle<?, N> rectangle, D drawable) { return new BuilderGuiDrawable<>(t -> castUncheckedUnboxedNonnull(new GuiRectangleMatcher<>(rectangle, drawable, t.mutator, t.logging))); }


	public D getDrawable() { return drawable; }

	public void setDrawable(D drawable) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetDrawable(drawable)); }

	public boolean trySetDrawable(D drawable) { return trySet(t -> this.drawable = t, drawable); }

	public Optional<D> tryGetDrawable() { return Optional.of(getDrawable()); }


	@Override
	public boolean tryDraw(Minecraft client) {
		pushMatrix();
		D d = getDrawable();
		d.spec().ifPresent(r -> GUIs.translateAndScaleFromTo(r, getRectangle()));
		boolean r = d.tryDraw(client);
		popMatrix();
		return r;
	}


	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull(isImmutable() ? this : new GuiRectangleMatcher<>(this, IMutatorImmutablizable.of(getMutator().toImmutable()))); }
}
