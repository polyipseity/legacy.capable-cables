package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.templates;

import com.google.common.collect.ImmutableList;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.IDrawableThemed;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.IThemed.EnumTheme;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Objects;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.rejectUnsupportedOperation;

@SideOnly(Side.CLIENT)
public class GuiTabsThemed<N extends Number, D extends GuiTabsThemed<N, D>> extends GuiTabs<N, D> implements IDrawableThemed<N, D, EnumTheme> {
	public GuiTabsThemed(int open, EnumTheme theme, List<ITab<N, ?>> tabs) {
		super(open, tabs);
		this.theme = theme;
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public GuiTabsThemed(int open, EnumTheme theme, ITab<N, ?>... tabs) {
		super(open, tabs);
		this.theme = theme;
	}

	protected EnumTheme theme;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTheme(EnumTheme theme) {
		this.theme = theme;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EnumTheme getTheme() { return theme; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof GuiTabsThemed)) return false;
		if (!super.equals(o)) return false;
		GuiTabsThemed<?, ?> that = (GuiTabsThemed<?, ?>) o;
		return theme == that.theme;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() { return Objects.hash(super.hashCode(), theme); }

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public D toImmutable() { return (D) new Immutable<>(this); }

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, D extends Immutable<N, D>> extends GuiTabsThemed<N, D> {
		public Immutable(int open, EnumTheme theme, List<ITab<N, ?>> tabs) { super(open, theme, ImmutableList.copyOf(tabs)); }

		@SuppressWarnings("varargs")
		@SafeVarargs
		public Immutable(int open, EnumTheme theme, ITab<N, ?>... tabs) { super(open, theme, ImmutableList.copyOf(tabs)); }

		public Immutable(GuiTabsThemed<N, ?> c) { this(c.open, c.theme, c.tabs); }

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setOpen(int open) { throw rejectUnsupportedOperation(); }

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setTheme(EnumTheme theme) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@SuppressWarnings("unchecked")
		@Override
		public D toImmutable() { return (D) this; }

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isImmutable() { return true; }
	}
}
