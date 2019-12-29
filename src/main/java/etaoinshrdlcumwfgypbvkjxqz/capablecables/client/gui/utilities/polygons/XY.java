package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.IImmutablizable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.PrimitiveHelper.Numbers;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Objects;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.rejectUnsupportedOperation;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.unexpectedThrowable;

@SideOnly(Side.CLIENT)
public class XY<N extends Number> implements IImmutablizable<XY<N>> {
    protected N x;
    protected N y;
    public XY(N x, N y) {
        this.x = x;
        this.y = y;
    }

    public void setX(N x) { this.x = x; }
    public N getX() { return x; }
    public void setY(N y) { this.y = y; }
    public N getY() { return y; }

    @SafeVarargs
    public final XY<N> sum(XY<N>... o) { return new XY<>(Numbers.sum(x, extractX(o)), Numbers.sum(y, extractY(o))); }
    @SafeVarargs
    public final XY<N> sumX(XY<N>... o) { return new XY<>(Numbers.sum(x, extractX(o)), y); }
    @SafeVarargs
    public final XY<N> sumY(XY<N>... o) { return new XY<>(x, Numbers.sum(y, extractY(o))); }
    public XY<N> minus(XY<N> o) { return new XY<>(Numbers.minus(x, o.x), Numbers.minus(y, o.y)); }
    public XY<N> minusX(XY<N> o) { return new XY<>(Numbers.minus(x, o.x), y); }
    public XY<N> minusY(XY<N> o) { return new XY<>(x, Numbers.minus(y, o.y)); }

    @SafeVarargs
    public final XY<N> max(XY<N>... o) { return new XY<>(Numbers.max(x, extractX(o)), Numbers.max(y, extractY(o))); }
    @SafeVarargs
    public final XY<N> min(XY<N>... o) { return new XY<>(Numbers.min(x, extractX(o)), Numbers.min(y, extractY(o))); }

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <N extends Number> N[] extractX(XY<N>... o) { return (N[]) Arrays.stream(o).map(t -> t.x).toArray(); }
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <N extends Number> N[] extractY(XY<N>... o) { return (N[]) Arrays.stream(o).map(t -> t.y).toArray(); }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public XY<N> clone() { try { return (XY<N>) super.clone(); } catch (CloneNotSupportedException | ClassCastException ex) { throw unexpectedThrowable(ex); } }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XY)) return false;
        XY<?> xy = (XY<?>) o;
        return x.equals(xy.x) &&
                y.equals(xy.y);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() { return Objects.hash(x, y); }

    /**
     * {@inheritDoc}
     */
    public XY<N> toImmutable() { return new Immutable<>(this); }
    @javax.annotation.concurrent.Immutable
    public static class Immutable<N extends Number> extends XY<N> {
        public Immutable(N x, N y) { super(x, y); }
        public Immutable(XY<N> c) { this(c.x, c.y); }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setX(N x) { throw rejectUnsupportedOperation(); }
        /**
         * {@inheritDoc}
         */
        @Override
        public void setY(N y) { throw rejectUnsupportedOperation(); }

        /**
         * {@inheritDoc}
         */
        @Override
        public Immutable<N> clone() { try { return (Immutable<N>) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); } }

        /**
         * {@inheritDoc}
         */
        @Override
        public Immutable<N> toImmutable() { return this; }
    }
}
