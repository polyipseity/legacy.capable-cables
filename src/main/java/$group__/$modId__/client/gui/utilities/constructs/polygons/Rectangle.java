package $group__.$modId__.client.gui.utilities.constructs.polygons;

import $group__.$modId__.client.gui.utilities.constructs.XY;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.meta.When;
import java.util.List;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictEquals.isEquals;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static com.google.common.collect.ImmutableSet.of;

@SideOnly(Side.CLIENT)
public class Rectangle<N extends Number, T extends Rectangle<N, T>> extends Polygon4<N, T> {
	/* SECTION variables */

	protected XY<N, ?> offset;
	protected XY<N, ?> size;

	/* SECTION constructors */

	public Rectangle(XY<N, ?> offset, XY<N, ?> size) {
		super(offset.toImmutable(), offset.sumX(size).toImmutable(), offset.sum(of(size)).toImmutable(), offset.sumY(size).toImmutable());
		this.offset = offset;
		this.size = size;
	}

	public Rectangle(N offsetX, N offsetY, N sizeX, N sizeY) { this(new XY<>(offsetX, offsetY), new XY<>(sizeX, sizeY)); }

	public Rectangle(Rectangle<N, ?> c) { this(c.getOffset(), c.getSize()); }


	/* SECTION getters & setters */

	public XY<N, ?> getOffset() { return offset; }

	public void setOffset(XY<N, ?> offset) {
		XY<N, ?> change = offset.sum(of(getOffset().negate()));
		replaceAll(t -> t.sum(of(change)).toImmutable());
		this.offset = offset;
	}

	public XY<N, ?> getSize() { return size; }

	public void setSize(XY<N, ?> size) {
		XY<N, ?> change = size.sum(of(getSize().negate()));
		set(1, b().sumX(change).toImmutable());
		set(2, c().sum(of(change)).toImmutable());
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

	public XY<N, ?> max() { return a().max(of(c())); }

	public XY<N, ?> min() { return a().min(of(c())); }


	/** {@inheritDoc} */
	@Override
	public T toImmutable() { return castUnchecked((Object) new Immutable<>(this)); }


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
	public T clone() {
		T r = super.clone();
		r.offset = offset.clone();
		r.size = size.clone();
		return r;
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, T extends Immutable<N, T>> extends Rectangle<N, T> {
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
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final T toImmutable() { return castUnchecked(this); }

		/** {@inheritDoc} */
		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final boolean isImmutable() { return true; }
	}
}
