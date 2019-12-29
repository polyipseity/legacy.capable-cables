package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.templates.components;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.IDrawable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.IImmutablizable;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.rejectUnsupportedOperation;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.unexpectedThrowable;

@SideOnly(Side.CLIENT)
public abstract class GuiRectangleThemedDrawable<N extends Number, D extends IDrawable<N, ? extends GuiRectangleThemedDrawable<N, D, MD>>, MD extends IImmutablizable<D>> extends GuiRectangleThemed<N> {
    protected MD drawable;
    public GuiRectangleThemedDrawable(Rectangle<N> rect, int color, Theme theme, MD drawable) {
        super(rect, color, theme);
        this.drawable = drawable;
    }

    public void setDrawable(MD drawable) { this.drawable = drawable; }
    public MD getDrawable() { return drawable; }

    protected Theme theme;
    /**
     * {@inheritDoc}
     */
    @Override
    public void setTheme(Theme theme) { this.theme = theme; }
    /**
     * {@inheritDoc}
     */
    @Override
    public Theme getTheme() { return theme; }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public GuiRectangleThemedDrawable<N, D, MD> clone() {
        GuiRectangleThemedDrawable<N, D, MD> r;
        try { r = (GuiRectangleThemedDrawable<N, D, MD>) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); }
        r.drawable = (MD) ((D) drawable).clone();
        return r;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuiRectangleThemedDrawable)) return false;
        if (!super.equals(o)) return false;
        GuiRectangleThemedDrawable<?, ?, ?> that = (GuiRectangleThemedDrawable<?, ?, ?>) o;
        return drawable.equals(that.drawable) &&
                theme == that.theme;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() { return Objects.hash(super.hashCode(), drawable, theme); }

    /**
     * {@inheritDoc}
     */
    @Override
    public GuiRectangleThemedDrawable<N, D, MD> toImmutable() {
        GuiRectangleThemedDrawable<N, D, MD> t = this;
        return new Immutable<N, D, MD>(this) {
            /**
             * {@inheritDoc}
             */
            @Override
            public void draw(Minecraft minecraft) { t.draw(minecraft); }
        };
    }
    @javax.annotation.concurrent.Immutable
    public static abstract class Immutable<N extends Number, D extends IDrawable<N, ? extends GuiRectangleThemedDrawable<N, D, MD>>, MD extends IImmutablizable<D>> extends GuiRectangleThemedDrawable<N, D, MD> {
        @SuppressWarnings("unchecked")
        public Immutable(Rectangle<N> rect, int color, Theme theme, MD drawable) { super(rect.toImmutable(), color, theme, (MD) drawable.toImmutable()); }
        public Immutable(GuiRectangleThemedDrawable<N, D, MD> c) { super(c.rect, c.color, c.theme, c.drawable); }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setRect(Rectangle<N> rect) { throw rejectUnsupportedOperation(); }
        /**
         * {@inheritDoc}
         */
        @Override
        public void setColor(int color) { throw rejectUnsupportedOperation(); }
        /**
         * {@inheritDoc}
         */
        @Override
        public void setTheme(Theme theme) { throw rejectUnsupportedOperation(); }
        /**
         * {@inheritDoc}
         */
        @Override
        public void setDrawable(MD drawable) { throw rejectUnsupportedOperation(); }

        /**
         * {@inheritDoc}
         */
        @Override
        public GuiRectangleThemedDrawable.Immutable<N, D, MD> clone() { try { return (GuiRectangleThemedDrawable.Immutable<N, D, MD>) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); } }

        /**
         * {@inheritDoc}
         */
        @Override
        public GuiRectangleThemedDrawable.Immutable<N, D, MD> toImmutable() { return this; }
    }
}
