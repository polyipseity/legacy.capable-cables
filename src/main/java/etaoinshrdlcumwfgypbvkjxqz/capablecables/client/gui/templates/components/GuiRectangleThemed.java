package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.templates.components;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.Color;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.IThemed;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons.Rectangle;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.rejectUnsupportedOperation;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.unexpectedThrowable;

@SideOnly(Side.CLIENT)
public class GuiRectangleThemed<N extends Number> extends GuiRectangle<N> implements IThemed<IThemed.Theme> {
    public GuiRectangleThemed(Rectangle<N> rect, Color color, Theme theme) {
        super(rect, color);
        this.theme = theme;
    }

    /** {@inheritDoc} */
    @Override
    public void draw(Minecraft minecraft) { theme.getHandler().drawRect(specs().getOffset(), specs().getSize(), color); }

    protected Theme theme;
    /** {@inheritDoc} */
    @Override
    public void setTheme(Theme theme) { this.theme = theme; }
    /** {@inheritDoc} */
    @Override
    public Theme getTheme() { return theme; }

    /** {@inheritDoc} */
    @Override
    public GuiRectangleThemed<N> clone() { try { return (GuiRectangleThemed<N>) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); } }
    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuiRectangleThemed)) return false;
        if (!super.equals(o)) return false;
        GuiRectangleThemed<?> that = (GuiRectangleThemed<?>) o;
        return theme == that.theme;
    }
    /** {@inheritDoc} */
    @Override
    public int hashCode() { return Objects.hash(super.hashCode(), theme); }

    /** {@inheritDoc} */
    @Override
    public GuiRectangleThemed<N> toImmutable() { return new Immutable<>(this); }
    @javax.annotation.concurrent.Immutable
    public static class Immutable<N extends Number> extends GuiRectangleThemed<N> {
        public Immutable(Rectangle<N> rect, Color color, Theme theme) { super(rect.toImmutable(), color.toImmutable(), theme); }
        public Immutable(GuiRectangleThemed<N> c) { this(c.rect, c.color, c.theme); }

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
        public GuiRectangleThemed.Immutable<N> clone() { try { return (GuiRectangleThemed.Immutable<N>) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); } }

         /** {@inheritDoc} */
        @Override
        public GuiRectangleThemed.Immutable<N> toImmutable() { return this; }
         /** {@inheritDoc} */
        @Override
        public boolean isImmutable() { return true; }
    }
}
