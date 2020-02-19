package $group__.$modId__.client.gui.components;

import $group__.$modId__.client.gui.polygons.Rectangle;
import $group__.$modId__.client.gui.themes.ITheme;
import $group__.$modId__.client.gui.themes.IThemed;
import $group__.$modId__.client.gui.traits.IColored;
import $group__.$modId__.client.gui.utilities.builders.BuilderGuiDrawable;
import $group__.$modId__.concurrent.IMutatorImmutablizable;
import $group__.$modId__.logging.ILogging;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Function;

import static $group__.$modId__.concurrent.IMutator.trySetNonnull;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectUnsupportedOperationIf;

public class GuiDrawableFunctional<T extends GuiDrawableFunctional<T, N, C, TH>, N extends Number, C, TH extends ITheme<TH>> extends GuiDrawable<T, N, C, TH> {
	/* SECTION variables */

	protected Function<? super T, ? extends Boolean> tryDrawFunction;
	protected Function<? super T, ? extends Optional<Rectangle<?, N>>> specFunction;


	/* SECTION constructors */

	public GuiDrawableFunctional(Function<? super T, ? extends Boolean> tryDrawFunction, Function<? super T, ? extends Optional<Rectangle<?, N>>> specFunction, IColored<C> colored, IThemed<TH> themed, IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) {
		super(colored, themed, mutator, logging);
		this.tryDrawFunction = trySetNonnull(getMutator(), tryDrawFunction, true);
		this.specFunction = trySetNonnull(getMutator(), specFunction, true);
	}

	public GuiDrawableFunctional(GuiDrawableFunctional<T, N, C, TH> copy) { this(copy, copy.getMutator()); }

	protected GuiDrawableFunctional(GuiDrawableFunctional<T, N, C, TH> copy, IMutatorImmutablizable<?, ?> mutator) { this(copy.getTryDrawFunction(), copy.getSpecFunction(), copy.getColored(), copy.getThemed(), mutator, copy.getLogging()); }


	/* SECTION static methods */

	public static <T extends BuilderGuiDrawable<T, V, N, C, TH>, V extends GuiDrawableFunctional<V, N, C, TH>, L extends Logger, N extends Number, C, TH extends ITheme<TH>> BuilderGuiDrawable<T, V, N, C, TH> newBuilderGDF(Function<? super V, ? extends Boolean> tryDrawFunction, Function<? super V, ? extends Optional<Rectangle<?, N>>> specFunction, IMutatorImmutablizable<?, ?> mutator) { return new BuilderGuiDrawable<>(t -> castUncheckedUnboxedNonnull(new GuiDrawableFunctional<V, N, C, TH>(tryDrawFunction, specFunction, t.colored, t.themed, mutator, t.logging))); }


	/* SECTION getters & setters */

	public Function<? super T, ? extends Boolean> getTryDrawFunction() { return tryDrawFunction; }

	public boolean trySetTryDrawFunction(Function<? super T, ? extends Boolean> tryDrawFunction) { return trySet(t -> this.tryDrawFunction = t, tryDrawFunction); }

	public Optional<Function<? super T, ? extends Boolean>> tryGetTryDrawFunction() { return Optional.of(getTryDrawFunction()); }

	public void setTryDrawFunction(Function<? super T, ? extends Boolean> tryDrawFunction) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetTryDrawFunction(tryDrawFunction)); }

	public Function<? super T, ? extends Optional<Rectangle<?, N>>> getSpecFunction() { return specFunction; }

	public boolean trySetSpecFunction(Function<? super T, ? extends Optional<Rectangle<?, N>>> specFunction) { return trySet(t -> this.specFunction = t, specFunction); }

	public Optional<Function<? super T, ? extends Optional<? extends Rectangle<?, N>>>> tryGetSpecFunction() { return Optional.of(getSpecFunction()); }

	public void setSpecFunction(Function<? super T, ? extends Optional<Rectangle<?, N>>> specFunction) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetSpecFunction(specFunction)); }


	/* SECTION methods */

	@Override
	public boolean tryDraw(Minecraft client) { return tryDrawFunction.apply(castUncheckedUnboxedNonnull(this)); }

	@Override
	public Optional<Rectangle<?, N>> spec() { return specFunction.apply(castUncheckedUnboxedNonnull(this)); }


	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull(isImmutable() ? this : new GuiDrawableFunctional<>(this, IMutatorImmutablizable.of(getMutator().toImmutable()))); }
}
