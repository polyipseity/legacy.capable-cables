package $group__.$modId__.client.gui.components;

import $group__.$modId__.client.gui.themes.ITheme;
import $group__.$modId__.client.gui.utilities.builders.BuilderGuiDrawable;
import $group__.$modId__.concurrent.IMutatorImmutablizable;
import $group__.$modId__.utilities.extensions.delegated.IListDelegated;
import $group__.$modId__.logging.ILogging;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static $group__.$modId__.concurrent.IMutator.trySetNonnull;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectIndexOutOfBounds;
import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectUnsupportedOperationIf;

@SideOnly(Side.CLIENT)
public class GuiTabs<T extends GuiTabs<T, N, C, TH, TA, E>, N extends Number, C, TH extends ITheme<TH>, TA extends List<E>, E extends GuiTab<?, N, ?, ?, ?>> extends GuiGroup<T, N, C, TH, TA, E> implements IListDelegated<TA, E> {
	/* SECTION variables */

	protected int open = 0;


	/* SECTION constructors */

	public GuiTabs(TA tabs, int open, IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) {
		super(tabs, mutator, logging);
		setOpen(this, open, true);
	}

	public GuiTabs(TA tabs, IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) { this(tabs, 0, mutator, logging); }

	public GuiTabs(GuiTabs<?, N, ?, ?, TA, ? extends E> copy) { this(copy, copy.getMutator()); }


	protected GuiTabs(GuiTabs<?, N, ?, ?, TA, ? extends E> copy, IMutatorImmutablizable<?, ?> mutator) { this(copy.getTabs(), copy.getOpen(), mutator, copy.getLogging()); }


	/* SECTION static methods */

	public static <T extends BuilderGuiDrawable<T, V, N, C, TH>, V extends GuiTabs<V, N, C, TH, TA, E>, N extends Number, C, TH extends ITheme<TH>, TA extends List<E>, E extends GuiTab<?, N, ?, ?, ?>> BuilderGuiDrawable<T, V, N, C, TH> newBuilderGT(TA tabs) { return newBuilderGT(tabs, 0); }

	public static <T extends BuilderGuiDrawable<T, V, N, C, TH>, V extends GuiTabs<V, N, C, TH, TA, E>, N extends Number, C, TH extends ITheme<TH>, TA extends List<E>, E extends GuiTab<?, N, ?, ?, ?>> BuilderGuiDrawable<T, V, N, C, TH> newBuilderGT(TA tabs, int open) { return new BuilderGuiDrawable<>(t -> castUncheckedUnboxedNonnull(new GuiTabs<>(tabs, open, t.mutator, t.logging))); }


	protected static boolean setOpen(GuiTabs<?, ?, ?, ?, ?, ?> t, int open) { return setOpen(t, open, false); }

	protected static boolean setOpen(GuiTabs<?, ?, ?, ?, ?, ?> t, int open, boolean initialize) {
		Collection<? extends GuiTab<?, ?, ?, ?, ?>> tabs = t.getTabs();

		int bound = tabs.size();
		if (bound <= open) throw rejectIndexOutOfBounds(bound, open);

		int open1 = t.getOpen();
		t.open = trySetNonnull(t.getMutator(), open, initialize);

		int o1 = t.getOpen();
		final int[] i = {0, 0};
		boolean r = tabs.stream().allMatch(e -> e.trySetOpen(i[0]++ == o1));

		if (!r) tabs.forEach(e -> e.trySetOpen(i[1]++ == open1));
		return r;
	}


	/* SECTION getters & setters */

	public int getOpen() { return open; }

	public boolean trySetOpen(int open) { return setOpen(this, open); }

	public Optional<Integer> tryGetOpen() { return Optional.of(getOpen()); }

	public void setOpen(int open) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetOpen(open)); }

	public TA getTabs() { return children; }

	public boolean trySetTabs(TA tabs, int open) { return trySet(t -> children = t, tabs) && setOpen(this, open); }

	public Optional<TA> tryGetTabs() { return Optional.of(getTabs()); }

	public void setTabs(TA tabs, int open) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetTabs(tabs, open)); }


	/**
	 * @since 0.0.1.0
	 * @deprecated replaced with {@link #getTabs}
	 */
	@Override
	@Deprecated
	public TA getChildren() { return getTabs(); }

	/**
	 * @since 0.0.1.0
	 * @deprecated replaced with {@link #setTabs}
	 */
	@Override
	@Deprecated
	public void setChildren(TA children) { setTabs(children, getOpen()); }

	/**
	 * @since 0.0.1.0
	 * @deprecated replaced with {@link #getTabs}
	 */
	@Override
	@Deprecated
	public TA getList() { return getChildren(); }

	/**
	 * @since 0.0.1.0
	 * @deprecated replaced with {@link #setTabs}
	 */
	@Override
	@Deprecated
	public void setList(TA list) { setChildren(list); }


	/* SECTION methods */

	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull(isImmutable() ? this : new GuiTabs<>(this, IMutatorImmutablizable.of(getMutator().toImmutable()))); }
}
