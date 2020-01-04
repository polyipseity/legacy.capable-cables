package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Objects;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.rejectUnsupportedOperation;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.unexpectedThrowable;

@SideOnly(Side.CLIENT)
public class Rectangle<N extends Number> extends Polygon4<N> {
    protected XY<N> offset;
    protected XY<N> size;
    public Rectangle(XY<N> offset, XY<N> size) {
        super(offset.toImmutable(), offset.sumX(size).toImmutable(), offset.sum(size).toImmutable(), offset.sumY(size).toImmutable());
        this.offset = offset;
        this.size = size;
    }

    public void setOffset(XY<N> offset) {
        XY<N> change = offset.minus(this.offset);
        replaceAll(t -> t.sum(change).toImmutable());
        this.offset = offset;
    }
    public XY<N> getOffset() { return offset; }
    public void setSize(XY<N> size) {
        XY<N> change = size.minus(this.size);
        set(1, b().sumX(change).toImmutable());
        set(2, c().sum(change).toImmutable());
        set(3, d().sumY(change).toImmutable());
        this.size = size;
    }
    public XY<N> getSize() { return size; }
    public void setOffsetAndSize(XY<N> offset, XY<N> size) {
        setOffset(offset);
        setSize(size);
    }

    public XY<N> min() { return a().min(c()); }
    public XY<N> max() { return a().max(c()); }

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
    public Rectangle<N> clone() {
        Rectangle<N> r;
        try { r = (Rectangle<N>) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); }
        r.offset = offset.clone();
        r.size = size.clone();
        return r;
    }
      /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rectangle)) return false;
        if (!super.equals(o)) return false;
        Rectangle<?> rectangle = (Rectangle<?>) o;
        return offset.equals(rectangle.offset) &&
                size.equals(rectangle.size);
    }
      /** {@inheritDoc} */
    @Override
    public int hashCode() { return Objects.hash(super.hashCode(), offset, size); }

      /** {@inheritDoc} */
    @Override
    public Rectangle<N> toImmutable() { return new Immutable<>(this); }
    public static class Immutable<N extends Number> extends Rectangle<N> {
        public Immutable(XY<N> offset, XY<N> size) {
            super(offset.toImmutable(), size.toImmutable());
            list = ImmutableList.copyOf(list);
        }
        public Immutable(Rectangle<N> c) { this(c.offset, c.size); }

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
        public void setSize(XY<N> size) { throw rejectUnsupportedOperation(); }
         /** {@inheritDoc} */
        @Override
        public void setOffset(XY<N> offset) { throw rejectUnsupportedOperation(); }
         /** {@inheritDoc} */
        @Override
        public void setOffsetAndSize(XY<N> offset, XY<N> size) { throw rejectUnsupportedOperation(); }

         /** {@inheritDoc} */
        @Override
        public Rectangle.Immutable<N> clone() { try { return (Rectangle.Immutable<N>) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); } }

         /** {@inheritDoc} */
        @Override
        public Rectangle.Immutable<N> toImmutable() { return this; }
         /** {@inheritDoc} */
        @Override
        public boolean isImmutable() { return true; }
    }
}
