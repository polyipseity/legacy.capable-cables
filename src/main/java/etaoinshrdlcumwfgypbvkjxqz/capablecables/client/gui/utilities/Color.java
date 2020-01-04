package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.IImmutablizable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

import static com.google.common.primitives.UnsignedBytes.checkedCast;
import static com.google.common.primitives.UnsignedBytes.toInt;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.rejectUnsupportedOperation;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.unexpectedThrowable;

@SideOnly(Side.CLIENT)
public class Color implements Cloneable, IImmutablizable<Color> {
    public static final Color
            TRANSPARENT = new Color("FF000000"),
            TRANSPARENT_I = TRANSPARENT.toImmutable();

    private byte red, green, blue, alpha;
    public Color(int red, int green, int blue, int alpha) {
        setRed(red);
        setGreen(green);
        setBlue(blue);
        setAlpha(alpha);
    }
    public Color(int color) { setColor(color); }
    public Color(String color) { setColor(Integer.parseInt(color, 16)); }

    public void setRed(Number red) { this.red = checkedCast(red.longValue()); }
    public int getRed() { return toInt(red); }
    public void setBlue(Number blue) { this.blue = checkedCast(blue.longValue()); }
    public int getBlue() { return toInt(blue); }
    public void setGreen(Number green) { this.green = checkedCast(green.longValue()); }
    public int getGreen() { return toInt(green); }
    public void setAlpha(Number alpha) { this.alpha = checkedCast(alpha.longValue()); }
    public int getAlpha() { return toInt(alpha); }

    public void setColor(int color) {
        setRed(color >> 16 & 255);
        setGreen(color >> 8 & 255);
        setBlue(color & 255);
        setAlpha(color >> 24 & 255);
    }
    public void setColor(String color) { setColor(Integer.parseInt(color, 16)); }
    public int getColor() { return getAlpha() << 24 + getRed() << 16 + getBlue() << 8 + getGreen(); }

    /** {@inheritDoc} */
    @Override
    public String toString() { return Integer.toString(getColor(), 16); }

    /** {@inheritDoc} */
    @Override
    public Color clone() { try { return (Color) super.clone(); } catch (CloneNotSupportedException | ClassCastException ex) { throw unexpectedThrowable(ex); } }
    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Color)) return false;
        Color color = (Color) o;
        return red == color.red &&
                green == color.green &&
                blue == color.blue &&
                alpha == color.alpha;
    }
    /** {@inheritDoc} */
    @Override
    public int hashCode() { return Objects.hash(red, green, blue, alpha); }

    /** {@inheritDoc} */
    @Override
    public Color toImmutable() { return new Immutable(this); }
    public static class Immutable extends Color {
        public Immutable(int red, int green, int blue, int alpha) { super(red, green, blue, alpha); }
        public Immutable(int color) { super(color); }
        public Immutable(String color) { super(color); }
        public Immutable(Color c) { super(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()); }

        /** {@inheritDoc} */
        @Override
        public void setRed(Number red) { throw rejectUnsupportedOperation(); }
        /** {@inheritDoc} */
        @Override
        public void setGreen(Number green) { throw rejectUnsupportedOperation(); }
        /** {@inheritDoc} */
        @Override
        public void setBlue(Number blue) { throw rejectUnsupportedOperation(); }
        /** {@inheritDoc} */
        @Override
        public void setAlpha(Number alpha) { throw rejectUnsupportedOperation(); }

        /** {@inheritDoc} */
        @Override
        public void setColor(int color) { throw rejectUnsupportedOperation(); }

        /** {@inheritDoc} */
        @Override
        public Immutable clone() { try { return (Immutable) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); } }

        /** {@inheritDoc} */
        @Override
        public Immutable toImmutable() { return this; }
        /** {@inheritDoc} */
        @Override
        public boolean isImmutable() { return true; }
    }
}
