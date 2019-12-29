package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.IImmutablizable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.unexpectedThrowable;

@SideOnly(Side.CLIENT)
public class Frame<N extends Number> implements IImmutablizable<Frame<N>> {
    protected XY<N> tl;
    protected XY<N> br;
    public Frame(XY<N> tl, XY<N> br) {
        this.tl = tl;
        this.br = br;
    }
    public Frame(N top, N right, N bottom, N left) {
        tl = new XY<>(left, top);
        br = new XY<>(right, bottom);
    }

    public void setTopLeft(XY<N> tl) { this.tl = tl; }
    public XY<N> getTopLeft() { return tl; }
    public void setBottomRight(XY<N> br) { this.br = br; }
    public XY<N> getBottomRight() { return br; }

    public void setTop(N t) { tl.setY(t); }
    public N top() { return tl.getY(); }
    public void setRight(N r) { br.setX(r); }
    public N right() { return br.getX(); }
    public void setBottom(N b) { br.setY(b); }
    public N bottom() { return br.getY(); }
    public void setLeft(N l) { tl.setX(l); }
    public N left() { return tl.getX(); }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Frame<N> clone() {
        Frame<N> r;
        try { r = (Frame<N>) super.clone(); } catch (CloneNotSupportedException | ClassCastException ex) { throw unexpectedThrowable(ex); }
        r.tl = tl.clone();
        r.br = br.clone();
        return r;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Frame)) return false;
        Frame<?> frame = (Frame<?>) o;
        return tl.equals(frame.tl) &&
                br.equals(frame.br);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() { return Objects.hash(tl, br); }

    /**
     * {@inheritDoc}
     */
    @Override
    public Frame<N> toImmutable() { return new Immutable<>(this); }
    @javax.annotation.concurrent.Immutable
    public static class Immutable<N extends Number> extends Frame<N> {
        public Immutable(XY<N> tl, XY<N> br) { super(tl.toImmutable(), br.toImmutable()); }
        public Immutable(N top, N right, N bottom, N left) { this(new XY<>(left, top), new XY<>(right, bottom)); }
        public Immutable(Frame<N> c) { this(c.tl, c.br); }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setTopLeft(XY<N> tl) { throw ThrowableHelper.rejectUnsupportedOperation(); }
        /**
         * {@inheritDoc}
         */
        @Override
        public void setBottomRight(XY<N> br) { throw ThrowableHelper.rejectUnsupportedOperation(); }
        /**
         * {@inheritDoc}
         */
        @Override
        public void setTop(N t) { throw ThrowableHelper.rejectUnsupportedOperation(); }
        /**
         * {@inheritDoc}
         */
        @Override
        public void setRight(N r) { throw ThrowableHelper.rejectUnsupportedOperation(); }
        /**
         * {@inheritDoc}
         */
        @Override
        public void setBottom(N b) { throw ThrowableHelper.rejectUnsupportedOperation(); }
        /**
         * {@inheritDoc}
         */
        @Override
        public void setLeft(N l) { throw ThrowableHelper.rejectUnsupportedOperation(); }

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
