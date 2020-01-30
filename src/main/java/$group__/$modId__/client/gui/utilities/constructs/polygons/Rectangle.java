package $group__.$modId__.client.gui.utilities.constructs.polygons;

import $group__.$modId__.client.gui.utilities.constructs.XY;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.meta.When;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictEquals.isEqual;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static com.google.common.collect.ImmutableSet.of;

@SideOnly(Side.CLIENT)
public class Rectangle<N extends Number, T extends Rectangle<N, T>> extends Polygon4<N, T> {
	/* SECTION variables */

	protected XY<N, ?> offset;
	protected XY<N, ?> size;

	/* SECTION constructors */

	public Rectangle(N offsetX, N offsetY, N sizeX, N sizeY) { this(new XY<>(offsetX, offsetY), new XY<>(sizeX, sizeY)); }

	public Rectangle(XY<N, ?> offset, XY<N, ?> size) {
		super(offset.toImmutable(), offset.sumX(size).toImmutable(), offset.sum(of(size)).toImmutable(), offset.sumY(size).toImmutable());
		this.offset = offset;
		this.size = size;
	}

	public Rectangle(Rectangle<N, ?> copy) { this(copy.getOffset(), copy.getSize()); }


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

	public final void setOffsetAndSize(Rectangle<N, ?> rect) { setOffsetAndSize(rect.getOffset(), rect.getSize()); }

	public void setOffsetAndSize(XY<N, ?> offset, XY<N, ?> size) {
		setOffset(offset);
		setSize(size);
	}

	@Override
	@Deprecated
	public void setA(XY<N, ?> a) { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void setB(XY<N, ?> b) { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void setC(XY<N, ?> c) { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void setD(XY<N, ?> d) { throw rejectUnsupportedOperation(); }


	/* SECTION methods */

	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull((Object) new Immutable<>(this)); }

	@Override
	public boolean isImmutable() { return false; }

	public XY<N, ?> max() { return a().max(of(c())); }

	public XY<N, ?> min() { return a().min(of(c())); }

	@Override
	public int hashCode() {
		return isImmutable() ? getHashCode(this, super.hashCode(), getOffset(), getSize()) : getHashCode(this, super.hashCode());
	}

	@Override
	public boolean equals(Object o) {
		return isImmutable() ? isEqual(this, o, super.equals(o),
				t -> getOffset().equals(t.getOffset()),
				t -> getSize().equals(t.getSize())) : isEqual(this, o, super.equals(o));
	}

	@Override
	public T clone() {
		T r = super.clone();
		r.offset = offset.copy();
		r.size = size.copy();
		return r;
	}

	@Override
	public String toString() {
		return getToStringString(this, super.toString(),
				new Object[]{"offset", getOffset()},
				new Object[]{"size", getSize()});
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, T extends Immutable<N, T>> extends Rectangle<N, T> {
		/* SECTION constructors */

		public Immutable(N offsetX, N offsetY, N sizeX, N sizeY) { this(new XY<>(offsetX, offsetY), new XY<>(sizeX, sizeY)); }

		public Immutable(XY<N, ?> offset, XY<N, ?> size) {
			super(offset.toImmutable(), size.toImmutable());
			vertexes = tryToImmutableUnboxedNonnull(vertexes);
		}

		public Immutable(Rectangle<N, ?> copy) { this(copy.getOffset(), copy.getSize()); }


		/* SECTION getters & setters */

		@Override
		@Deprecated
		public void setOffset(XY<N, ?> offset) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public void setSize(XY<N, ?> size) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public void setOffsetAndSize(XY<N, ?> offset, XY<N, ?> size) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final boolean isImmutable() { return true; }
	}
}
