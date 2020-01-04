package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.templates.components;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.Color;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.IDrawable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.Remarks;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.*;

@SideOnly(Side.CLIENT)
public class GuiRectangleThemedDrawable<N extends Number, D extends IDrawable<N, D>> extends GuiRectangleThemed<N> {
    @Remarks("Mutable")
    protected IDrawable<N, D> drawable;
    public GuiRectangleThemedDrawable(Rectangle<N> rect, Color color, Theme theme, IDrawable<N, D> drawable) {
        super(rect, color, theme);
        setDrawable(drawable);
    }
    public GuiRectangleThemedDrawable(Rectangle<N> rect, Theme theme, IDrawable<N, D> drawable) { this(rect, Color.TRANSPARENT, theme, drawable); }

    /** {@inheritDoc} */
    @Override
    public void draw(Minecraft minecraft) {
        theme.getHandler().drawRect(rect.getOffset(), rect.getSize(), color);
        drawable.specs().setOffsetAndSize(rect.getOffset(), rect.getSize());
        drawable.draw(minecraft);
    }

    public void setDrawable(IDrawable<N, D> drawable) {
        if (drawable.isImmutable()) throw rejectArguments(drawable);
        this.drawable = drawable;
    }
    public IDrawable<N, D> getDrawable() { return drawable; }

    protected Theme theme;
    /** {@inheritDoc} */
    @Override
    public void setTheme(Theme theme) { this.theme = theme; }
    /** {@inheritDoc} */
    @Override
    public Theme getTheme() { return theme; }

    /** {@inheritDoc} */
    @Override
    public GuiRectangleThemedDrawable<N, D> clone() {
        GuiRectangleThemedDrawable<N, D> r;
        try { r = (GuiRectangleThemedDrawable<N, D>) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); }
        r.drawable = drawable.clone();
        return r;
    }
    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuiRectangleThemedDrawable)) return false;
        if (!super.equals(o)) return false;
        GuiRectangleThemedDrawable<?, ?> that = (GuiRectangleThemedDrawable<?, ?>) o;
        return drawable.equals(that.drawable) &&
                theme == that.theme;
    }
    /** {@inheritDoc} */
    @Override
    public int hashCode() { return Objects.hash(super.hashCode(), drawable, theme); }

    /** {@inheritDoc} */
    @Override
    public GuiRectangleThemedDrawable<N, D> toImmutable() { return new Immutable<>(this); }
    @javax.annotation.concurrent.Immutable
    public static class Immutable<N extends Number, D extends IDrawable<N, D>> extends GuiRectangleThemedDrawable<N, D> {
        public Immutable(Rectangle<N> rect, Color color, Theme theme, IDrawable<N, D> drawable) { super(rect.toImmutable(), color.toImmutable(), theme, drawable); }
        public Immutable(Rectangle<N> rect, Theme theme, IDrawable<N, D> drawable) { this(rect, Color.TRANSPARENT_I, theme, drawable); }
        public Immutable(GuiRectangleThemedDrawable<N, D> c) { this(c.rect, c.color, c.theme, c.drawable); }

         /** {@inheritDoc} */
        @Override
        public void setRect(Rectangle<N> rect) { throw rejectUnsupportedOperation(); }
         /** {@inheritDoc} */
        @Override
        public void setColor(Color color) { throw rejectUnsupportedOperation(); }
         /** {@inheritDoc} */
        @Override
        public void setTheme(Theme theme) { throw rejectUnsupportedOperation(); }
         /** {@inheritDoc} */
        @Override
        public void setDrawable(IDrawable<N, D> drawable) { throw rejectUnsupportedOperation(); }

         /** {@inheritDoc} */
        @Override
        public GuiRectangleThemedDrawable.Immutable<N, D> clone() { try { return (GuiRectangleThemedDrawable.Immutable<N, D>) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); } }

         /** {@inheritDoc} */
        @Override
        public GuiRectangleThemedDrawable.Immutable<N, D> toImmutable() { return this; }
         /** {@inheritDoc} */
        @Override
        public boolean isImmutable() { return true; }
    }
}
