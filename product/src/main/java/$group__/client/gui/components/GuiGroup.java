package $group__.client.gui.components;

import $group__.client.gui.coordinates.XY;
import $group__.client.gui.polygons.Rectangle;
import $group__.client.gui.themes.GuiThemedNull;
import $group__.client.gui.themes.ITheme;
import $group__.client.gui.traits.IDrawable;
import $group__.client.gui.utilities.builders.BuilderGuiDrawable;
import $group__.logging.ILogging;
import $group__.utilities.concurrent.IMutatorImmutablizable;
import $group__.utilities.extensions.delegated.ICollectionDelegated;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static $group__.utilities.concurrent.IMutator.trySetNonnull;
import static $group__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.utilities.helpers.specific.Throwables.rejectUnsupportedOperationIf;

@SideOnly(Side.CLIENT)
public class GuiGroup<T extends GuiGroup<T, N, C, TH, CO, E>, N extends Number, C, TH extends ITheme<TH>,
		CO extends Collection<E>, E extends IDrawable<N>> extends GuiDrawable<T, N, C, TH> implements ICollectionDelegated<CO, E> {
	/* SECTION variables */

	protected CO children;


	/* SECTION constructors */

	public GuiGroup(CO children, IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) {
		super(GuiColorNull.getInstance(), GuiThemedNull.getInstance(), mutator, logging);
		this.children = trySetNonnull(getMutator(), children, true);
	}

	public GuiGroup(GuiGroup<?, N, ?, ?, ? extends CO, E> copy) { this(copy, copy.getMutator()); }


	protected GuiGroup(GuiGroup<?, N, ?, ?, ? extends CO, E> copy, IMutatorImmutablizable<?, ?> mutator) { this(copy.getChildren(), mutator, copy.getLogging()); }


	/* SECTION static methods */

	public static <T extends BuilderGuiDrawable<T, V, N, C, TH>, V extends GuiGroup<V, N, C, TH, CO, E>,
			N extends Number, C, TH extends ITheme<TH>, CO extends Collection<E>, E extends IDrawable<N>> BuilderGuiDrawable<T, V, N, C, TH> newBuilderGG(CO children, IMutatorImmutablizable<?, ?> mutator) { return new BuilderGuiDrawable<>(t -> castUncheckedUnboxedNonnull(new GuiGroup<>(children, t.mutator, t.logging))); }


	/* SECTION getters & setters */

	public CO getChildren() { return children; }

	public void setChildren(CO children) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetChildren(children)); }

	public boolean trySetChildren(CO children) { return trySet(t -> this.children = t, children); }

	public Optional<CO> tryGetChildren() { return Optional.of(getChildren()); }

	@Override
	@Deprecated
	public CO getCollection() { return getChildren(); }

	@Override
	@Deprecated
	public void setCollection(CO collection) { setChildren(collection); }


	/* SECTION methods */

	@Override
	public boolean tryDraw(Minecraft client) {
		return children.stream().reduce(false, (t, u) -> u.tryDraw(client) || t
				, Boolean::logicalOr);
	}

	@Override
	public Optional<Rectangle<?, N>> spec() {
		Collection<E> c = getChildren();
		if (c.isEmpty()) return Optional.empty();

		List<Rectangle<?, N>> s =
				c.stream().map(E::spec).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
		if (s.isEmpty()) return Optional.empty();
		Rectangle<?, N> f = s.get(0);

		XY<?, N> min = f.min().min(s.stream().map(Rectangle::min).collect(Collectors.toList()));
		return Optional.of(new Rectangle<>(min,
				f.max().max(s.stream().map(Rectangle::max).collect(Collectors.toList())).sum(ImmutableList.of(min.negate())), getMutator(), getLogging()));
	}


	@Override
	public T toImmutable() {
		return castUncheckedUnboxedNonnull(isImmutable() ? this : new GuiGroup<>(this,
				IMutatorImmutablizable.of(getMutator().toImmutable())));
	}
}
