package $group__.$modId__.utilities.constructs.classes.concrete;

import $group__.$modId__.utilities.constructs.classes.NumberDefault;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.helpers.Primitives.Numbers;

import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictEquals.isEquals;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable.tryClone;
import static $group__.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

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

	@Override
	public T negate() {
		T r = clone();
		r.value = Numbers.negate(value);
		r.offset = Numbers.negateNullable(offset);
		return r;
	}

	/** {@inheritDoc} */
	@SuppressWarnings("ConstantConditions")
	@Override
	public T sum(Collection<? extends Number> o) {
		T r = clone();

		Number p = getParent();
		r.value = Numbers.sum(getValue(), o.stream().filter(t -> t instanceof NumberRelative<?>).map(t -> (NumberRelative<?>) t).map(t -> (t.doubleValue() - (t.getOffset() == null ? 0 : t.getOffset().doubleValue())) / (p == null ? 1 : p.doubleValue())).collect(Collectors.toList()));

		List<Number> o1 = o.stream().map(t -> t instanceof NumberRelative<?> ? ((NumberRelative<?>) t).getOffset() == null ? 0 : ((NumberRelative<?>) t).getOffset().doubleValue() : t.doubleValue()).collect(Collectors.toList());
		Number to = getOffset();
		if (to != null) o1.add(0, to);
		r.offset = Numbers.sumNullable(o1);

		return r;
	}


	@Override
	public T newInstanceFrom(Number o) {
		T r = clone();
		if (o instanceof NumberRelative<?>) {
			NumberRelative<?> o1 = (NumberRelative<?>) o;
			r.value = o1.value;
			r.parent = o1.parent;
			r.offset = o1.offset;
		} else {
			r.value = o;
			r.parent = offset = null;
		}
		return r;
	}


	/** {@inheritDoc} */
	@Override
	public int compareTo(Number o) {
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
		r.value = tryClone(value);
		r.parent = tryClone(parent);
		r.offset = tryClone(offset);
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
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final T toImmutable() { return castUnchecked(this); }

		/** {@inheritDoc} */
		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final boolean isImmutable() { return true; }
	}
}
