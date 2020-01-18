package $group__.$modId__.client.gui.templates.components;

import $group__.$modId__.client.gui.utilities.constructs.IDrawableThemed;
import $group__.$modId__.client.gui.utilities.constructs.IThemed;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.meta.When;
import java.util.Arrays;
import java.util.List;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictEquals.isEquals;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class GuiTabsThemed<N extends Number, TH extends IThemed.ITheme<TH>, T extends GuiTabsThemed<N, TH, T>> extends GuiTabs<N, T> implements IDrawableThemed<N, TH, T> {
	/* SECTION variables */

	protected TH theme;


	/* SECTION constructors */

	public GuiTabsThemed(List<ITab<N, ?>> tabs, TH theme, int open) {
		super(tabs, open);
		setTheme(this, theme);
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public GuiTabsThemed(TH theme, int open, ITab<N, ?>... tabs) {
		this(Arrays.asList(tabs), theme, open);
	}

	public GuiTabsThemed(GuiTabsThemed<N, TH, ?> c) { this(c.getTabs(), c.getTheme(), c.getOpen()); }


	/* SECTION getters & setters */

	/** {@inheritDoc} */
	@Override
	public void setTheme(TH theme) { setTheme(this, theme); }

	/** {@inheritDoc} */
	@Override
	public TH getTheme() { return theme; }


	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	public T toImmutable() { return castUnchecked((Object) new Immutable<>(this)); }


	/** {@inheritDoc} */
	@Override
	public String toString() { return getToStringString(this, super.toString(),
				new Object[]{"theme", getTheme()}); }

	/** {@inheritDoc} */
	@Override
	public int hashCode() { return getHashCode(this, super.hashCode(), getTheme()); }

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
			t -> getTheme().equals(t.getTheme())); }


	/* SECTION static methods */

	protected static <T extends ITheme<T>> void setTheme(GuiTabsThemed<?, T, ?> t, T theme) {
		t.theme = theme;

		T th = t.getTheme();
		t.getTabs().forEach(tab -> { if (tab instanceof IThemed<?>) castUnchecked(tab, (IThemed<T>) null).setTheme(th); });
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, TH extends ITheme<TH>, T extends Immutable<N, TH, T>> extends GuiTabsThemed<N, TH, T> {
		/* SECTION constructors */

		public Immutable(List<ITab<N, ?>> tabs, TH theme, int open) {
			super(ImmutableList.copyOf(tabs), theme, open);
		}

		@SuppressWarnings("varargs")
		@SafeVarargs
		public Immutable(TH theme, int open, ITab<N, ?>... tabs) {
			this(Arrays.asList(tabs), theme, open);
		}

		public Immutable(GuiTabsThemed<N, TH, ?> c) { this(c.getTabs(), c.getTheme(), c.getOpen()); }


		/* SECTION getters & setters */

		/** {@inheritDoc} */
		@Override
		public void setTabs(List<ITab<N, ?>> iTabs, int open) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setOpen(int open) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setTheme(TH theme) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		/** {@inheritDoc} */
		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final T toImmutable() { return castUnchecked(this); }

		/** {@inheritDoc} */
		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final boolean isImmutable() { return true; }
	}
}
