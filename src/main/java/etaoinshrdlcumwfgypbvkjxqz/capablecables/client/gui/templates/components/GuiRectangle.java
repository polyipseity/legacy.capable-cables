package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.templates.components;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.Color;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.IDrawable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons.Rectangle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.rejectUnsupportedOperation;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.unexpectedThrowable;

@SideOnly(Side.CLIENT)
public class GuiRectangle<N extends Number> extends Gui implements IDrawable<N, GuiRectangle<N>> {
    protected Rectangle<N> rect;
    protected Color color;

    public GuiRectangle(Rectangle<N> rect, Color color) {
        this.rect = rect;
        this.color = color;
    }

    public void setRect(Rectangle<N> rect) { this.rect = rect; }
    public Rectangle<N> getRect() { return rect; }
    public void setColor(Color color) { this.color = color; }
    public Color getColor() { return color; }

      /** {@inheritDoc} */
    @Override
    public void draw(Minecraft minecraft) { drawRect(rect.a().getX().intValue(), rect.a().getY().intValue(), rect.b().getX().intValue(), rect.b().getY().intValue(), color.getColor()); }
      /** {@inheritDoc} */
    @Override
    public Rectangle<N> specs() { return rect; }

      /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public GuiRectangle<N> clone() {
        GuiRectangle<N> r;
        try { r = (GuiRectangle<N>) super.clone(); } catch (CloneNotSupportedException | ClassCastException ex) { throw unexpectedThrowable(ex); }
        r.rect = rect.clone();
        r.color = color.clone();
        return r;
    }
      /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuiRectangle)) return false;
        GuiRectangle<?> that = (GuiRectangle<?>) o;
        return rect.equals(that.rect) &&
                color.equals(that.color);
    }
      /** {@inheritDoc} */
    @Override
    public int hashCode() { return Objects.hash(rect, color); }

      /** {@inheritDoc} */
    @Override
    public GuiRectangle<N> toImmutable() { return new Immutable<>(this); }
    @javax.annotation.concurrent.Immutable
    public static class Immutable<N extends Number> extends GuiRectangle<N> {
        public Immutable(Rectangle<N> rect, Color color) { super(rect.toImmutable(), color.toImmutable()); }
        public Immutable(GuiRectangle<N> c) { this(c.rect, c.color); }

         /** {@inheritDoc} */
        @Override
        public void setRect(Rectangle<N> rect) { throw rejectUnsupportedOperation(); }
         /** {@inheritDoc} */
        @Override
        public void setColor(Color color) { throw rejectUnsupportedOperation(); }

         /** {@inheritDoc} */
        @Override
        public Immutable<N> clone() { try { return (Immutable<N>) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); } }

         /** {@inheritDoc} */
        @Override
        public Immutable<N> toImmutable() { return this; }
         /** {@inheritDoc} */
        @Override
        public boolean isImmutable() { return true; }
    }
}
