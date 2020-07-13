package $group__.client.gui.components;

import $group__.client.gui.themes.ITheme;
import $group__.client.gui.themes.IThemed;
import $group__.client.gui.traits.IColored;
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

@SideOnly(Side.CLIENT)
public class GuiRectangle<T extends GuiRectangle<T, N, C, TH>, N extends Number, C extends Color,
		TH extends ITheme<TH>> extends GuiDrawable<T, N, C, TH> {
	protected Rectangle<?, N> rectangle;


	public GuiRectangle(Rectangle<?, N> rectangle, IColored<C> colored, IThemed<TH> themed, IMutatorImmutablizable<?,
			?> mutator, ILogging<Logger> logging) {
		super(colored, themed, mutator, logging);
		this.rectangle = trySetNonnull(getMutator(), rectangle, true);
	}

	public GuiRectangle(GuiRectangle<?, N, C, TH> copy) { this(copy, copy.getMutator()); }


	protected GuiRectangle(GuiRectangle<?, N, C, TH> copy, IMutatorImmutablizable<?, ?> mutator) { this(copy.getRectangle(), copy.getColored(), copy.getThemed(), mutator, copy.getLogging()); }


	public static <T extends BuilderGuiDrawable<T, V, N, C, TH>, V extends GuiRectangle<V, N, C, TH>, N extends Number, C extends Color, TH extends ITheme<TH>> BuilderGuiDrawable<T, V, N, C, TH> newBuilderGuiRectangle(Rectangle<?, N> rectangle) { return new BuilderGuiDrawable<>(t -> castUncheckedUnboxedNonnull(new GuiRectangle<>(rectangle, t.colored, t.themed, t.mutator, t.logging))); }


	public Rectangle<?, N> getRectangle() { return rectangle; }

	public void setRectangle(Rectangle<?, N> rectangle) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetRectangle(rectangle)); }

	public boolean trySetRectangle(Rectangle<?, N> rectangle) { return trySet(t -> this.rectangle = t, rectangle); }

	public Optional<Rectangle<?, N>> tryGetRectangle() { return Optional.of(getRectangle()); }


	@Override
	public boolean tryDraw(Minecraft client) {
		return tryGetColor().map(c -> {
			GUIs.drawRectangle(getTheme(), getRectangle(), c);
			return true;
		}).orElse(false);
	}

	@Override
	public Optional<Rectangle<?, N>> spec() { return Optional.of(getRectangle().toImmutable()); }


	@Override
	public T toImmutable() {
		return castUncheckedUnboxedNonnull(isImmutable() ? this : new GuiRectangle<>(this,
				IMutatorImmutablizable.of(getMutator().toImmutable())));
	}
}
