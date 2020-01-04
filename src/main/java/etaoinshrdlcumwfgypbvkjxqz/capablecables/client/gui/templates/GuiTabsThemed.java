package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.templates;

import com.google.common.collect.ImmutableList;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.IThemed;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Objects;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.rejectUnsupportedOperation;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.unexpectedThrowable;

@SideOnly(Side.CLIENT)
public class GuiTabsThemed<N extends Number> extends GuiTabs<N> implements IThemed<IThemed.Theme> {
    public GuiTabsThemed(int open, Theme theme, List<ITab<N>> tabs) {
        super(open, tabs);
        this.theme = theme;
    }
    @SafeVarargs
    public GuiTabsThemed(int open, Theme theme, ITab<N>... tabs) {
        super(open, tabs);
        this.theme = theme;
    }

    protected Theme theme;
    /** {@inheritDoc} */
    @Override
    public void setTheme(Theme theme) { this.theme = theme; }
    /** {@inheritDoc} */
    @Override
    public Theme getTheme() { return theme; }

    /** {@inheritDoc} */
    @Override
    public GuiTabsThemed<N> clone() { try { return (GuiTabsThemed<N>) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); } }
    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuiTabsThemed)) return false;
        if (!super.equals(o)) return false;
        GuiTabsThemed<?> that = (GuiTabsThemed<?>) o;
        return theme == that.theme;
    }
    /** {@inheritDoc} */
    @Override
    public int hashCode() { return Objects.hash(super.hashCode(), theme); }

    @Override
    public GuiTabsThemed<N> toImmutable() { return new Immutable<>(this); }
    @javax.annotation.concurrent.Immutable
    public static class Immutable<N extends Number> extends GuiTabsThemed<N> {
        public Immutable(int open, Theme theme, List<ITab<N>> tabs) { super(open, theme, ImmutableList.copyOf(tabs)); }
        @SafeVarargs
        public Immutable(int open, Theme theme, ITab<N>... tabs) { super(open, theme, ImmutableList.copyOf(tabs)); }
        public Immutable(GuiTabsThemed<N> c) { this(c.open, c.theme, c.tabs); }

        /** {@inheritDoc} */
        @Override
        public void setOpen(int open) { throw rejectUnsupportedOperation(); }
        /** {@inheritDoc} */
        @Override
        public void setTheme(Theme theme) { throw rejectUnsupportedOperation(); }

        /** {@inheritDoc} */
        @Override
        public GuiTabsThemed.Immutable<N> clone() { try { return (GuiTabsThemed.Immutable<N>) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); } }

        @Override
        public GuiTabsThemed.Immutable<N> toImmutable() { return this; }
        /** {@inheritDoc} */
        @Override
        public boolean isImmutable() { return true; }
    }
}
