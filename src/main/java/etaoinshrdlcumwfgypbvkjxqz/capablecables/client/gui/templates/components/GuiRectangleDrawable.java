package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.templates.components;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.Color;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.IDrawable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.IImmutablizable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.Remarks;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.*;

@SideOnly(Side.CLIENT)
public class GuiRectangleDrawable<N extends Number, D extends IDrawable<N, D>> extends GuiRectangle<N> implements IImmutablizable<GuiRectangle<N>> {
    @Remarks("Mutable")
    protected IDrawable<N, D> drawable;
    public GuiRectangleDrawable(Rectangle<N> rect, Color color, IDrawable<N, D> drawable) {
        super(rect, color);
        setDrawable(drawable);
    }
    public GuiRectangleDrawable(Rectangle<N> rect, IDrawable<N, D> drawable) { this(rect, Color.TRANSPARENT, drawable); }

      /** {@inheritDoc} */
    @Override
    public void draw(Minecraft minecraft) {
        super.draw(minecraft);
        drawable.specs().setOffsetAndSize(rect.getOffset(), rect.getSize());
        drawable.draw(minecraft);
    }

    public void setDrawable(IDrawable<N, D> drawable) {
        if (drawable.isImmutable()) throw rejectArguments(drawable);
        this.drawable = drawable;
    }
    public IDrawable<N, D> getDrawable() { return drawable; }

      /** {@inheritDoc} */
    @Override
    public GuiRectangleDrawable<N, D> clone() {
        GuiRectangleDrawable<N, D> r;
        try { r = (GuiRectangleDrawable<N, D>) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); }
        r.drawable = drawable.clone();
        return r;
    }
      /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuiRectangleDrawable)) return false;
        if (!super.equals(o)) return false;
        GuiRectangleDrawable<?, ?> that = (GuiRectangleDrawable<?, ?>) o;
        return drawable.equals(that.drawable);
    }
      /** {@inheritDoc} */
    @Override
    public int hashCode() { return Objects.hash(super.hashCode(), drawable); }

      /** {@inheritDoc} */
    @Override
    public GuiRectangleDrawable<N, D> toImmutable() { return new Immutable<>(this); }
    @javax.annotation.concurrent.Immutable
    public static class Immutable<N extends Number, D extends IDrawable<N, D>> extends GuiRectangleDrawable<N, D> {
        public Immutable(Rectangle<N> rect, Color color, IDrawable<N, D> drawable) { super(rect.toImmutable(), color.toImmutable(), drawable.toImmutable()); }
        public Immutable(Rectangle<N> rect, IDrawable<N, D> drawable) { this(rect, Color.TRANSPARENT_I, drawable); }
        public Immutable(GuiRectangleDrawable<N, D> c) { this(c.rect, c.color, c.drawable); }

         /** {@inheritDoc} */
        @Override
        public void setRect(Rectangle<N> rect) { throw rejectUnsupportedOperation(); }
         /** {@inheritDoc} */
        @Override
        public void setColor(Color color) { throw rejectUnsupportedOperation(); }
         /** {@inheritDoc} */
        @Override
        public void setDrawable(IDrawable<N, D> drawable) { throw rejectUnsupportedOperation(); }

         /** {@inheritDoc} */
        @Override
        public GuiRectangleDrawable.Immutable<N, D> clone() { try { return (GuiRectangleDrawable.Immutable<N, D>) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); } }

         /** {@inheritDoc} */
        @Override
        public GuiRectangleDrawable.Immutable<N, D> toImmutable() { return this; }
         /** {@inheritDoc} */
        @Override
        public boolean isImmutable() { return true; }
    }
}
