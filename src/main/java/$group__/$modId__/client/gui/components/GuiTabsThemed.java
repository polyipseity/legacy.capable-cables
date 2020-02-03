package $group__.$modId__.client.gui.components;

import $group__.$modId__.client.gui.utilities.constructs.IDrawableThemed;
import $group__.$modId__.client.gui.utilities.constructs.IThemed;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.helpers.Casts;
import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class GuiTabsThemed<N extends Number, L extends List<E>, E extends GuiTabs.ITab<N, ?>, TH extends IThemed.ITheme<TH>, T extends GuiTabsThemed<N, L, E, TH, T>> extends GuiTabs<N, L, E, T> implements IDrawableThemed<N, TH, T> {
	/* SECTION variables */

	@SuppressWarnings("NotNullFieldNotInitialized")
	protected TH theme;


	/* SECTION constructors */

	@SuppressWarnings("varargs")
	@SafeVarargs
	public GuiTabsThemed(TH theme, int open, E... tabs) { this(castUncheckedUnboxedNonnull(Arrays.asList(tabs)), theme, open); }

	public GuiTabsThemed(L tabs, TH theme, int open) {
		super(tabs, open);
		setTheme(this, theme);
	}

	public GuiTabsThemed(GuiTabsThemed<N, L, E, TH, ?> copy) { this(copy.getTabs(), copy.getTheme(), copy.getOpen()); }


	/* SECTION static methods */

	protected static <T extends ITheme<T>> void setTheme(GuiTabsThemed<?, ?, ?, T, ?> t, T theme) {
		t.theme = theme;
		T th = t.getTheme();
		t.getTabs().forEach(tab -> Casts.<IThemed<T>>castChecked(tab, castUncheckedUnboxedNonnull(IThemed.class)).ifPresent(t1 -> t1.setTheme(th)));
	}


	/* SECTION getters & setters */

	@Override
	public TH getTheme() { return theme; }

	@Override
	public void setTheme(TH theme) { setTheme(this, theme); }


	/* SECTION methods */

	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull((Object) new Immutable<>(this)); }

	@Override
	public boolean isImmutable() { return false; }


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, L extends List<E>, E extends ITab<N, ?>, TH extends ITheme<TH>, T extends Immutable<N, L, E, TH, T>> extends GuiTabsThemed<N, L, E, TH, T> {
		/* SECTION constructors */

		@SuppressWarnings("varargs")
		@SafeVarargs
		public Immutable(TH theme, int open, E... tabs) { this(castUncheckedUnboxedNonnull(ImmutableList.copyOf(tabs)), theme, open); }

		public Immutable(L tabs, TH theme, int open) { super(tryToImmutableUnboxedNonnull(tabs), tryToImmutableUnboxedNonnull(theme), tryToImmutableUnboxedNonnull(open)); }

		public Immutable(GuiTabsThemed<N, L, E, TH, ?> copy) { this(copy.getTabs(), copy.getTheme(), copy.getOpen()); }


		/* SECTION getters & setters */

		@Override
		@Deprecated
		public void setOpen(int open) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public void setTabs(L tabs, int open) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public void setTheme(TH theme) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		@Override
		@OverridingStatus(group = GROUP)
		public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

		@Override
		@OverridingStatus(group = GROUP)
		public final boolean isImmutable() { return true; }
	}
}
