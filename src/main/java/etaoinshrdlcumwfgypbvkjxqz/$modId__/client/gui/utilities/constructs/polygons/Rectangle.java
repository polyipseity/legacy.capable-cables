package etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.polygons;

import com.google.common.collect.ImmutableList;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.XY;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictEquals.isEquals;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictHashCode.getHashCode;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictToString.getToStringString;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;

@SideOnly(Side.CLIENT)
public class Rectangle<N extends Number, M extends Rectangle<N, M>> extends Polygon4<N, M> {
	/* SECTION variables */

	protected XY<N, ?> offset;
	protected XY<N, ?> size;

	/* SECTION constructors */

	public Rectangle(XY<N, ?> offset, XY<N, ?> size) {
		super(offset.toImmutable(), offset.sumX(size).toImmutable(), offset.sum(size).toImmutable(), offset.sumY(size).toImmutable());
		this.offset = offset;
		this.size = size;
	}

	public Rectangle(N offsetX, N offsetY, N sizeX, N sizeY) { this(new XY<>(offsetX, offsetY), new XY<>(sizeX, sizeY)); }

	public Rectangle(Rectangle<N, ?> c) { this(c.getOffset(), c.getSize()); }


	/* SECTION getters & setters */

	public XY<N, ?> getOffset() { return offset; }

	public void setOffset(XY<N, ?> offset) {
		XY<N, ?> change = offset.minus(this.getOffset());
		replaceAll(t -> t.sum(change).toImmutable());
		this.offset = offset;
	}

	public XY<N, ?> getSize() { return size; }

	public void setSize(XY<N, ?> size) {
		XY<N, ?> change = size.minus(this.getSize());
		set(1, b().sumX(change).toImmutable());
		set(2, c().sum(change).toImmutable());
		set(3, d().sumY(change).toImmutable());
		this.size = size;
	}

	public void setOffsetAndSize(XY<N, ?> offset, XY<N, ?> size) {
		setOffset(offset);
		setSize(size);
	}

	public final void setOffsetAndSize(Rectangle<N, ?> rect) { setOffsetAndSize(rect.getOffset(), rect.getSize()); }


	/** {@inheritDoc} */
	@Override
	public void setList(List<XY<N, ?>> list) { throw rejectUnsupportedOperation(); }

	/** {@inheritDoc} */
	@Override
	public void setA(XY<N, ?> a) { throw rejectUnsupportedOperation(); }

	/** {@inheritDoc} */
	@Override
	public void setB(XY<N, ?> b) { throw rejectUnsupportedOperation(); }

	/** {@inheritDoc} */
	@Override
	public void setC(XY<N, ?> c) { throw rejectUnsupportedOperation(); }

	/** {@inheritDoc} */
	@Override
	public void setD(XY<N, ?> d) { throw rejectUnsupportedOperation(); }


	/* SECTION methods */

	public XY<N, ?> min() { return a().min(c()); }

	public XY<N, ?> max() { return a().max(c()); }


	/** {@inheritDoc} */
	@Override
	public M toImmutable() { return castUnchecked((Object) new Immutable<>(this)); }


	/** {@inheritDoc} */
	@Override
	public String toString() { return getToStringString(this, super.toString(),
				new Object[]{"offset", getOffset()},
				new Object[]{"size", getSize()}); }

	/** {@inheritDoc} */
	@Override
	public int hashCode() { return getHashCode(this, super.hashCode(), getOffset(), getSize()); }

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
			t -> getOffset().equals(t.getOffset()),
			t -> getSize().equals(t.getSize())); }

	/** {@inheritDoc} */
	@Override
	public M clone() {
		M r = super.clone();
		r.offset = offset.clone();
		r.size = size.clone();
		return r;
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, M extends Immutable<N, M>> extends Rectangle<N, M> {
		/* SECTION constructors */

		public Immutable(XY<N, ?> offset, XY<N, ?> size) {
			super(offset.toImmutable(), size.toImmutable());
			list = ImmutableList.copyOf(list);
		}

		public Immutable(N offsetX, N offsetY, N sizeX, N sizeY) { this(new XY<>(offsetX, offsetY), new XY<>(sizeX, sizeY)); }

		public Immutable(Rectangle<N, ?> c) { this(c.getOffset(), c.getSize()); }


		/* SECTION getters & setters */

		/** {@inheritDoc} */
		@Override
		public void setList(List<XY<N, ?>> list) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setSize(XY<N, ?> size) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setOffset(XY<N, ?> offset) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setOffsetAndSize(XY<N, ?> offset, XY<N, ?> size) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		/** {@inheritDoc} */
		@Override
		public M toImmutable() { return castUnchecked(this); }

		/** {@inheritDoc} */
		@Override
		public boolean isImmutable() { return true; }
	}
}
