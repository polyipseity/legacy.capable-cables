package etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs;

import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Primitives.Numbers;

import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictEquals.isEquals;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictHashCode.getHashCode;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictToString.getToStringString;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.variables.Constants.GROUP;

public class NumberRelative<T extends NumberRelative<T>> extends NumberDefault<T> {
	/* SECTION variables */

	protected Number value;
	@Nullable
	protected Number parent;
	@Nullable
	protected Number offset;


	/* SECTION constructors */

	public NumberRelative(Number value, @Nullable Number parent, @Nullable Number offset) {
		this.value = value;
		this.parent = parent;
		this.offset = offset;
	}

	public NumberRelative(Number value, @Nullable Number parent) { this(value, parent, null); }

	public NumberRelative(Number value) { this(value, null); }

	public NumberRelative(NumberRelative<?> c) { this(c.getValue(), c.getParent(), c.getOffset()); }


	/* SECTION getters & setters */

	public Number getValue() { return value; }

	public void setValue(Number value) { this.value = value; }

	@Nullable
	public Number getParent() { return parent; }

	public void setParent(@Nullable Number parent) { this.parent = parent; }

	@Nullable
	public Number getOffset() { return offset; }

	public void setOffset(@Nullable Number offset) { this.offset = offset; }


	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	public T sum(Number... o) {
		T r = clone();
		Number p = getParent();
		r.setValue(Numbers.sum(getValue(), Arrays.stream(o).map(t -> p == null ? t.doubleValue() : t.doubleValue() / p.doubleValue()).collect(Collectors.toList())));
		Number to = getOffset();
		r.setOffset(to == null ? Numbers.sum(Arrays.stream(o).filter(t -> t instanceof NumberRelative<?> && ((NumberRelative<?>) t).getOffset() != null).collect(Collectors.toList()), true) : Numbers.sum(to, Arrays.stream(o).filter(t -> t instanceof NumberRelative<?> && ((NumberRelative<?>) t).getOffset() != null).collect(Collectors.toList())));
		return r;
	}

	/** {@inheritDoc} */
	@Override
	public T minus(Number o) {
		T r = clone();
		Number p = getParent();
		r.setValue(Numbers.minus(getValue(), p == null ? o.doubleValue() : o.doubleValue() / p.doubleValue()));
		if (o instanceof NumberRelative<?>) {
			NumberRelative<?> ot = (NumberRelative<?>) o;
			Number oto = ot.getOffset(), to = getOffset();
			if (oto != null) r.setOffset(to == null ? oto : Numbers.minus(to, oto));
		}
		return r;
	}


	/** {@inheritDoc} */
	@Override
	public int compareTo(T o) {
		boolean greater = doubleValue() > o.doubleValue();
		boolean lesser = doubleValue() < o.doubleValue();
		return greater ? 1 : lesser ? -1 : 0;
	}


	/** {@inheritDoc} */
	@Override
	public T toImmutable() { return castUnchecked(new Immutable<>(this)); }

	/** {@inheritDoc} */
	@Override
	public boolean isImmutable() { return false; }


	/** {@inheritDoc} */
	@Override
	public int intValue() { return (int) doubleValue(); }

	/** {@inheritDoc} */
	@Override
	public long longValue() { return (long) doubleValue(); }

	/** {@inheritDoc} */
	@Override
	public float floatValue() { return (float) doubleValue(); }

	/** {@inheritDoc} */
	@Override
	public double doubleValue() {
		Number p = getParent(), o = getOffset();
		return (p == null ? 1D : p.doubleValue()) * getValue().doubleValue() + (o == null ? 0D : o.doubleValue());
	}


	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public String toString() { return getToStringString(this, super.toString(),
			new Object[]{"value", getValue()},
			new Object[]{"parent", getParent()},
			new Object[]{"offset", getOffset()}); }

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public int hashCode() { return getHashCode(this, super.hashCode(), getValue(), getParent(), getOffset()); }

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
				t -> getValue().equals(t.getValue()),
				t -> Objects.equals(getParent(), t.getParent()),
				t -> Objects.equals(getOffset(), t.getOffset())); }

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T clone() {
		T r = super.clone();
		if (value instanceof ICloneable<?>) r.value = castUnchecked(value, (ICloneable<Number>) null).clone();
		if (parent instanceof ICloneable<?>) r.parent = castUnchecked(parent, (ICloneable<Number>) null).clone();
		if (offset instanceof ICloneable<?>) r.offset = castUnchecked(offset, (ICloneable<Number>) null).clone();
		return r;
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<T extends NumberRelative<T>> extends NumberRelative<T> {
		/* SECTION constructors */

		public Immutable(Number value, @Nullable Number parent, @Nullable Number offset) { super(value, parent, offset); }

		public Immutable(Number value, @Nullable Number parent) { this(value, parent, null); }

		public Immutable(Number value) { this(value, null); }

		public Immutable(Immutable<?> c) { this(c.getValue(), c.getParent(), c.getOffset()); }


		/* SECTION getters & setters */

		/** {@inheritDoc} */
		@Override
		public void setValue(Number value) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setParent(@Nullable Number parent) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setOffset(@Nullable Number offset) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		/** {@inheritDoc} */
		@Override
		public T toImmutable() { return castUnchecked(this); }

		/** {@inheritDoc} */
		@Override
		public boolean isImmutable() { return true; }
	}
}
