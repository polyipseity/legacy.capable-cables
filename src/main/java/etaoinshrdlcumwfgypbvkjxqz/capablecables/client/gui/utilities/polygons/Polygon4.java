package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.*;

@SideOnly(Side.CLIENT)
public class Polygon4<N extends Number> extends PolygonN<N> {
    public Polygon4(XY<N> a, XY<N> b, XY<N> c, XY<N> d) { super(a, b, c, d); }
    public Polygon4(List<XY<N>> l) {
        super(l);
        if (l.size() != 4) throw rejectArguments(l);
    }

    public XY<N> a() { return get(0); }
    public void setA(XY<N> a) { set(0, a); }
    public XY<N> b() { return get(1); }
    public void setB(XY<N> b) { set(0, b); }
    public XY<N> c() { return get(2); }
    public void setC(XY<N> c) { set(0, c); }
    public XY<N> d() { return get(3); }
    public void setD(XY<N> d) { set(0, d); }

    /** {@inheritDoc} */
    @Override
    public Polygon4<N> clone() { try { return (Polygon4<N>) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); } }

    /** {@inheritDoc} */
    @Override
    public Polygon4<N> toImmutable() { return new Immutable<>(this); }
    public static class Immutable<N extends Number> extends Polygon4<N> {
        public Immutable(XY<N> a, XY<N> b, XY<N> c, XY<N> d) { super(ImmutableList.of(a.toImmutable(), b.toImmutable(), c.toImmutable(), d.toImmutable())); }
        public Immutable(Polygon4<N> c) { this(c.a(), c.b(), c.c(), c.d()); }

        /** {@inheritDoc} */
        @Override
        public void setList(List<XY<N>> list) { throw rejectUnsupportedOperation(); }
        /** {@inheritDoc} */
        @Override
        public void setA(XY<N> a) { throw rejectUnsupportedOperation(); }
        /** {@inheritDoc} */
        @Override
        public void setB(XY<N> b) { throw rejectUnsupportedOperation(); }
        /** {@inheritDoc} */
        @Override
        public void setC(XY<N> c) { throw rejectUnsupportedOperation(); }
        /** {@inheritDoc} */
        @Override
        public void setD(XY<N> d) { throw rejectUnsupportedOperation(); }

        /** {@inheritDoc} */
        @Override
        public Polygon4.Immutable<N> clone() { try { return (Polygon4.Immutable<N>) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); } }

        /** {@inheritDoc} */
        @Override
        public Polygon4.Immutable<N> toImmutable() { return this; }
        /** {@inheritDoc} */
        @Override
        public boolean isImmutable() { return true; }
    }
}
