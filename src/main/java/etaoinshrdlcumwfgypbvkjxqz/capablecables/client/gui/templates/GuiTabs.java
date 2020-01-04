package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.templates;

import com.google.common.collect.ImmutableList;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.IDrawable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons.XY;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.*;

@SideOnly(Side.CLIENT)
public class GuiTabs<N extends Number> extends Gui implements IDrawable<N, GuiTabs<N>> {
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

      /** {@inheritDoc} */
    @Override
    public void draw(Minecraft minecraft) {
        int i = 0;
        for (ITab<N> t : tabs) {
            if (i++ == open) t.draw(minecraft);
            else t.drawClosed(minecraft);
        }
    }
      /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public Rectangle<N> specs() {
        Rectangle<N> f = tabs.get(0).specs();
        Rectangle<N>[] r = (Rectangle<N>[]) tabs.subList(1, tabs.size()).stream().map(IDrawable::specs).toArray();
        XY<N> min = f.min().min((XY<N>[]) Arrays.stream(r).map(Rectangle::min).toArray());
        return new Rectangle<>(min, f.max().max((XY<N>[]) Arrays.stream(r).map(Rectangle::max).toArray()).minus(min));
    }

      /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public GuiTabs<N> clone() {
        GuiTabs<N> r;
        try { r = (GuiTabs<N>) super.clone(); } catch (CloneNotSupportedException | ClassCastException ex) { throw unexpectedThrowable(ex); }
        ITab<N>[] tabsC = (ITab<N>[]) tabs.stream().map(IDrawable::clone).toArray();
        r.tabs = tabs instanceof ImmutableList ? ImmutableList.copyOf(tabsC) : Arrays.asList(tabsC);
        return r;
    }
      /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuiTabs)) return false;
        GuiTabs<?> guiTabs = (GuiTabs<?>) o;
        return open == guiTabs.open &&
                tabs.equals(guiTabs.tabs);
    }
      /** {@inheritDoc} */
    @Override
    public int hashCode() { return Objects.hash(tabs, open); }

      /** {@inheritDoc} */
    @Override
    public GuiTabs<N> toImmutable() { return new Immutable<>(this); }
    @javax.annotation.concurrent.Immutable
    public static class Immutable<N extends Number> extends GuiTabs<N> {
        public Immutable(int open, List<ITab<N>> tabs) { super(open, ImmutableList.copyOf(tabs)); }
        @SafeVarargs
        public Immutable(int open, ITab<N>... tabs) { super(open, ImmutableList.copyOf(tabs)); }
        public Immutable(GuiTabs<N> c) { this(c.open, c.tabs); }

         /** {@inheritDoc} */
        @Override
        public Immutable<N> clone() { try { return (Immutable<N>) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); } }

         /** {@inheritDoc} */
        @Override
        public void setOpen(int open) { throw rejectUnsupportedOperation(); }

         /** {@inheritDoc} */
        @Override
        public Immutable<N> toImmutable() { return this; }
         /** {@inheritDoc} */
        @Override
        public boolean isImmutable() { return true; }
    }

    public interface ITab<N extends Number> extends IDrawable<N, ITab<N>> {
        void drawClosed(Minecraft minecraft);

        class Impl<N extends Number, D extends IDrawable<N, D>> implements ITab<N> {
            protected IDrawable<N, D> content;
            protected IDrawable<N, D> access;
            public Impl(IDrawable<N, D> access, IDrawable<N, D> content) {
                this.content = content;
                this.access = access;
            }
            @SuppressWarnings("EmptyMethod")
            protected void merge() {}

            public void setContent(IDrawable<N, D> content) { this.content = content; }
            public IDrawable<N, D> getContent() { return content; }
            public void setAccess(IDrawable<N, D> access) { this.access = access; }
            public IDrawable<N, D> getAccess() { return access; }

/** {@inheritDoc} */
            @Override
            public void draw(Minecraft minecraft) {
                access.draw(minecraft);
                content.draw(minecraft);
                merge();
            }
/** {@inheritDoc} */
            @Override
            public void drawClosed(Minecraft minecraft) { access.draw(minecraft); }
/** {@inheritDoc} */
            @Override
            public Rectangle<N> specs() {
                XY<N> min = access.specs().min().min(content.specs().min());
                return new Rectangle<>(min, access.specs().max().max(content.specs().max()).minus(min));
            }

/** {@inheritDoc} */
            @SuppressWarnings("unchecked")
            @Override
            public Impl<N, D> clone() {
                Impl<N, D> r;
                try { r = (Impl<N, D>) super.clone(); } catch (CloneNotSupportedException | ClassCastException ex) { throw unexpectedThrowable(ex); }
                r.access = access.clone();
                r.content = content.clone();
                return r;
            }
/** {@inheritDoc} */
            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Impl)) return false;
                Impl<?, ?> impl = (Impl<?, ?>) o;
                return content.equals(impl.content) &&
                        access.equals(impl.access);
            }
/** {@inheritDoc} */
            @Override
            public int hashCode() { return Objects.hash(content, access); }

/** {@inheritDoc} */
            @Override
            public ITab<N> toImmutable() { return new Immutable<>(this); }
            @javax.annotation.concurrent.Immutable
            public static class Immutable<N extends Number, D extends IDrawable<N, D>> extends Impl<N, D> {
                public Immutable(IDrawable<N, D> access, IDrawable<N, D> content) { super(access.toImmutable(), content.toImmutable()); }
                public Immutable(Impl<N, D> c) { this(c.access, c.content); }

    /** {@inheritDoc} */
                @Override
                public void setContent(IDrawable<N, D> content) { throw rejectUnsupportedOperation(); }
    /** {@inheritDoc} */
                @Override
                public void setAccess(IDrawable<N, D> access) { throw rejectUnsupportedOperation(); }

    /** {@inheritDoc} */
                @Override
                public Immutable<N, D> clone() { try { return (Immutable<N, D>) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); } }

    /** {@inheritDoc} */
                @Override
                public ITab<N> toImmutable() { return this; }
    /** {@inheritDoc} */
                @Override
                public boolean isImmutable() { return true; }
            }
        }
    }
}
