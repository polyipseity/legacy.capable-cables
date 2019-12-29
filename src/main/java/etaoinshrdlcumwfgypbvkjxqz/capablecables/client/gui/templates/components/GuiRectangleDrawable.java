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
public class GuiRectangleDrawable<N extends Number, D extends IDrawable<N, ? extends GuiRectangleDrawable<N, D, MD>>, MD extends IImmutablizable<D>> extends GuiRectangle<N> implements IImmutablizable<GuiRectangle<N>> {
    protected MD drawable;
    public GuiRectangleDrawable(Rectangle<N> rect, int color, MD drawable) {
        super(rect, color);
        this.drawable = drawable;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void draw(Minecraft minecraft) {
        super.draw(minecraft);
        D d = (D) drawable;
        d.specs().setOffsetAndSize(rect.getOffset(), rect.getSize());
        d.draw(minecraft);
    }

    public void setDrawable(MD drawable) { this.drawable = drawable; }
    public MD getDrawable() { return drawable; }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public GuiRectangleDrawable<N, D, MD> clone() {
        GuiRectangleDrawable<N, D, MD> r;
        try { r = (GuiRectangleDrawable<N, D, MD>) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); }
        r.drawable = (MD) ((D) drawable).clone();
        return r;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuiRectangleDrawable)) return false;
        if (!super.equals(o)) return false;
        GuiRectangleDrawable<?, ?, ?> that = (GuiRectangleDrawable<?, ?, ?>) o;
        return drawable.equals(that.drawable);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() { return Objects.hash(super.hashCode(), drawable); }

    /**
     * {@inheritDoc}
     */
    @Override
    public GuiRectangleDrawable<N, D, MD> toImmutable() { return new Immutable<>(this); }
    @javax.annotation.concurrent.Immutable
    public static class Immutable<N extends Number, D extends IDrawable<N, ? extends GuiRectangleDrawable<N, D, MD>>, MD extends IImmutablizable<D>> extends GuiRectangleDrawable<N, D, MD> {
        @SuppressWarnings("unchecked")
        public Immutable(Rectangle<N> rect, int color, MD drawable) { super(rect.toImmutable(), color, (MD) drawable.toImmutable()); }
        public Immutable(GuiRectangleDrawable<N, D, MD> c) { this(c.rect, c.color, c.drawable); }

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
        public void setDrawable(MD drawable) { throw rejectUnsupportedOperation(); }

        /**
         * {@inheritDoc}
         */
        @Override
        public GuiRectangleDrawable.Immutable<N, D, MD> clone() { try { return (GuiRectangleDrawable.Immutable<N, D, MD>) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); } }

        /**
         * {@inheritDoc}
         */
        @Override
        public GuiRectangleDrawable.Immutable<N, D, MD> toImmutable() { return this; }
    }
}
