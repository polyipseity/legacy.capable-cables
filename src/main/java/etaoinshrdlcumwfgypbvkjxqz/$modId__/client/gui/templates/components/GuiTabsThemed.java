package etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.templates.components;

import com.google.common.collect.ImmutableList;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.IDrawableThemed;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.IThemed;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictEquals.isEquals;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictHashCode.getHashCode;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictToString.getToStringString;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;

@SideOnly(Side.CLIENT)
public class GuiTabsThemed<N extends Number, T extends IThemed.ITheme<T>, D extends GuiTabsThemed<N, T, D>> extends GuiTabs<N, D> implements IDrawableThemed<N, T, D> {
	/* SECTION variables */

	protected T theme;


	/* SECTION constructors */

	public GuiTabsThemed(List<ITab<N, ?>> tabs, T theme, int open) {
		super(tabs, open);
		setTheme(this, theme);
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public GuiTabsThemed(T theme, int open, ITab<N, ?>... tabs) {
		this(Arrays.asList(tabs), theme, open);
	}

	public GuiTabsThemed(GuiTabsThemed<N, T, ?> c) { this(c.getTabs(), c.getTheme(), c.getOpen()); }


	/* SECTION getters & setters */

	/** {@inheritDoc} */
	@Override
	public void setTheme(T theme) { setTheme(this, theme); }

	/** {@inheritDoc} */
	@Override
	public T getTheme() { return theme; }


	/* SECTION methods */

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public D toImmutable() { return castUnchecked((Object) new Immutable<>(this)); }


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
	public static class Immutable<N extends Number, T extends ITheme<T>, D extends Immutable<N, T, D>> extends GuiTabsThemed<N, T, D> {
		/* SECTION constructors */

		public Immutable(List<ITab<N, ?>> tabs, T theme, int open) {
			super(ImmutableList.copyOf(tabs), theme, open);
		}

		@SuppressWarnings("varargs")
		@SafeVarargs
		public Immutable(T theme, int open, ITab<N, ?>... tabs) {
			this(Arrays.asList(tabs), theme, open);
		}

		public Immutable(GuiTabsThemed<N, T, ?> c) { this(c.getTabs(), c.getTheme(), c.getOpen()); }


		/* SECTION getters & setters */

		/** {@inheritDoc} */
		@Override
		public void setTabs(List<ITab<N, ?>> iTabs, int open) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setOpen(int open) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setTheme(T theme) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		/** {@inheritDoc} */
		@Override
		public D toImmutable() { return castUnchecked(this); }

		/** {@inheritDoc} */
		@Override
		public boolean isImmutable() { return true; }
	}
}
