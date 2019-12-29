package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.templates;

import com.google.common.collect.ImmutableList;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.IDrawable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons.XY;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.IImmutablizable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.*;

@SideOnly(Side.CLIENT)
public class GuiTabs<N extends Number> extends Gui implements IDrawable<N, GuiTabs<N>>, IImmutablizable<GuiTabs<N>> {
    protected List<ITab<N>> tabs;
    protected int open;

    public GuiTabs(int open, List<ITab<N>> tabs) {
        if (tabs.isEmpty()) throw rejectArguments(tabs);
        this.open = open;
        this.tabs = tabs;
    }
    @SafeVarargs
    public GuiTabs(int open, ITab<N>... tabs) { this(open, Arrays.asList(tabs)); }

    public void setTabs(List<ITab<N>> tabs)  { throw rejectUnsupportedOperation(); }
    public List<ITab<N>> getTabs() { return tabs; }
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setOpen(int open) {
        tabs.get(open); // Check index.
        this.open = open;
    }
    public int getOpen() { return open; }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Minecraft minecraft) {
        int i = 0;
        for (ITab<N> t : tabs) {
            if (i++ == open) t.draw(minecraft);
            else t.drawClosed(minecraft);
        }
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Rectangle<N> specs() {
        Rectangle<N> f = tabs.get(0).specs();
        Rectangle<N>[] r = (Rectangle<N>[]) tabs.subList(1, tabs.size()).stream().map(IDrawable::specs).toArray();
        XY<N> min = f.min().min((XY<N>[]) Arrays.stream(r).map(Rectangle::min).toArray());
        return new Rectangle<>(min, f.max().max((XY<N>[]) Arrays.stream(r).map(Rectangle::max).toArray()).minus(min));
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public GuiTabs<N> clone() {
        GuiTabs<N> r;
        try { r = (GuiTabs<N>) super.clone(); } catch (CloneNotSupportedException | ClassCastException ex) { throw unexpectedThrowable(ex); }
        ITab<N>[] tabsC = (ITab<N>[]) tabs.stream().map(IDrawable::clone).toArray();
        r.tabs = tabs instanceof ImmutableList ? ImmutableList.copyOf(tabsC) : Arrays.asList(tabsC);
        return r;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuiTabs)) return false;
        GuiTabs<?> guiTabs = (GuiTabs<?>) o;
        return open == guiTabs.open &&
                tabs.equals(guiTabs.tabs);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() { return Objects.hash(tabs, open); }

    /**
     * {@inheritDoc}
     */
    @Override
    public GuiTabs<N> toImmutable() { return new Immutable<>(this); }
    @javax.annotation.concurrent.Immutable
    public static class Immutable<N extends Number> extends GuiTabs<N> {
        public Immutable(int open, List<ITab<N>> tabs) { super(open, ImmutableList.copyOf(tabs)); }
        @SafeVarargs
        public Immutable(int open, ITab<N>... tabs) { super(open, ImmutableList.copyOf(tabs)); }
        public Immutable(GuiTabs<N> c) { this(c.open, c.tabs); }

        /**
         * {@inheritDoc}
         */
        @Override
        public Immutable<N> clone() { try { return (Immutable<N>) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); } }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setOpen(int open) { throw rejectUnsupportedOperation(); }

        /**
         * {@inheritDoc}
         */
        @Override
        public Immutable<N> toImmutable() { return this; }
    }

    public interface ITab<N extends Number> extends IDrawable<N, ITab<N>>, IImmutablizable<ITab<N>> {
        void drawClosed(Minecraft minecraft);

        class Impl<N extends Number, D extends IDrawable<N, ? extends Impl<N, D, MD>>, MD extends IImmutablizable<D>> implements ITab<N> {
            protected MD content;
            protected MD access;
            public Impl(MD access, MD content) {
                this.content = content;
                this.access = access;
            }
            @SuppressWarnings("EmptyMethod")
            protected void merge() {}

            public void setContent(MD content) { this.content = content; }
            public MD getContent() { return content; }
            public void setAccess(MD access) { this.access = access; }
            public MD getAccess() { return access; }

            /**
             * {@inheritDoc}
             */
            @SuppressWarnings("unchecked")
            @Override
            public void draw(Minecraft minecraft) {
                ((D) access).draw(minecraft);
                ((D) content).draw(minecraft);
                merge();
            }
            /**
             * {@inheritDoc}
             */
            @SuppressWarnings("unchecked")
            @Override
            public void drawClosed(Minecraft minecraft) { ((D) access).draw(minecraft); }
            /**
             * {@inheritDoc}
             */
            @SuppressWarnings("unchecked")
            @Override
            public Rectangle<N> specs() {
                XY<N> min = ((D) access).specs().min().min(((D) content).specs().min());
                return new Rectangle<>(min, ((D) access).specs().max().max(((D) content).specs().max()).minus(min));
            }

            /**
             * {@inheritDoc}
             */
            @SuppressWarnings("unchecked")
            @Override
            public Impl<N, D, MD> clone() {
                Impl<N, D, MD> r;
                try { r = (Impl<N, D, MD>) super.clone(); } catch (CloneNotSupportedException | ClassCastException ex) { throw unexpectedThrowable(ex); }
                r.access = (MD) ((D) access).clone();
                r.content = (MD) ((D) content).clone();
                return r;
            }
            /**
             * {@inheritDoc}
             */
            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Impl)) return false;
                Impl<?, ?, ?> impl = (Impl<?, ?, ?>) o;
                return content.equals(impl.content) &&
                        access.equals(impl.access);
            }
            /**
             * {@inheritDoc}
             */
            @Override
            public int hashCode() { return Objects.hash(content, access); }

            /**
             * {@inheritDoc}
             */
            @Override
            public ITab<N> toImmutable() { return new Immutable<>(this); }
            @javax.annotation.concurrent.Immutable
            public static class Immutable<N extends Number, D extends IDrawable<N, ? extends Impl<N, D, MD>>, MD extends IImmutablizable<D>> extends Impl<N, D, MD> {
                @SuppressWarnings("unchecked")
                public Immutable(MD access, MD content) { super((MD) access.toImmutable(), (MD) content.toImmutable()); }
                public Immutable(Impl<N, D, MD> c) { this(c.access, c.content); }

                /**
                 * {@inheritDoc}
                 */
                @Override
                public void setContent(MD content) { throw rejectUnsupportedOperation(); }
                /**
                 * {@inheritDoc}
                 */
                @Override
                public void setAccess(MD access) { throw rejectUnsupportedOperation(); }

                /**
                 * {@inheritDoc}
                 */
                @Override
                public Immutable<N, D, MD> clone() { try { return (Immutable<N, D, MD>) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); } }

                @Override
                public ITab<N> toImmutable() { return this; }
            }
        }
    }
}
