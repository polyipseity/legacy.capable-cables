package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons;

import com.google.common.collect.ImmutableList;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.IImmutablizable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.rejectUnsupportedOperation;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.unexpectedThrowable;

@SideOnly(Side.CLIENT)
public class PolygonN<N extends Number> extends AbstractList<XY<N>> implements IImmutablizable<PolygonN<N>> {
    protected List<XY<N>> list;
    @SafeVarargs
    public PolygonN(XY<N>... e) { this.list = Arrays.asList(e); }
    public PolygonN(List<XY<N>> l) { this.list = l; }

    public void setList(List<XY<N>> list) { this.list = list; }
    public List<XY<N>> getList() { return list; }

    /**
     * {@inheritDoc}
     */
    @Override
    public XY<N> get(int index) { return list.get(index); }
    /**
     * {@inheritDoc}
     */
    @Override
    public int size() { return list.size(); }
    /**
     * {@inheritDoc}
     */
    @Override
    public XY<N> set(int index, XY<N> element) { return list.set(index, element); }
    /**
     * {@inheritDoc}
     */
    @Override
    public void add(int index, XY<N> element) { list.add(index, element); }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object o) { return list.remove(o); }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public PolygonN<N> clone() {
        PolygonN<N> r;
        try { r = (PolygonN<N>) super.clone(); } catch (CloneNotSupportedException | ClassCastException ex) { throw unexpectedThrowable(ex); }
        r.list = list instanceof ImmutableList ? ImmutableList.copyOf(list) : new ArrayList<>(list);
        return r;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PolygonN)) return false;
        if (!super.equals(o)) return false;
        PolygonN<?> polygonN = (PolygonN<?>) o;
        return list.equals(polygonN.list);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() { return Objects.hash(super.hashCode(), list); }

    /**
     * {@inheritDoc}
     */
    @Override
    public PolygonN<N> toImmutable() { return new Immutable<>(this); }
    public static class Immutable<N extends Number> extends PolygonN<N> {
        @SafeVarargs
        public Immutable(XY<N>... e) { super(ImmutableList.copyOf(e)); }
        public Immutable(List<XY<N>> l) { super(ImmutableList.copyOf(l)); }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setList(List<XY<N>> list) { throw rejectUnsupportedOperation(); }

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
