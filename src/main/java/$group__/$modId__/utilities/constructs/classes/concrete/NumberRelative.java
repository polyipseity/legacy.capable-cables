package $group__.$modId__.utilities.constructs.classes.concrete;

import $group__.$modId__.utilities.constructs.classes.NumberDefault;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.helpers.Primitives.Numbers;
import $group__.$modId__.utilities.variables.Globals;
import com.google.common.collect.Streams;

import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictEquals.isEquals;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable.tryCloneUnboxed;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable.tryCloneUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Optionals.unboxOptional;
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

	@SuppressWarnings("CopyConstructorMissesField")
	public NumberRelative(NumberRelative<?> copy) { this(copy.getValue(), unboxOptional(copy.getParent()), unboxOptional(copy.getOffset())); }


	/* SECTION getters & setters */

	public Number getValue() { return value; }

	public void setValue(Number value) { this.value = value; }

	public Optional<Number> getParent() { return Optional.ofNullable(parent); }

	public void setParent(@Nullable Number parent) { this.parent = parent; }

	public Optional<Number> getOffset() { return Optional.ofNullable(offset); }

	public void setOffset(@Nullable Number offset) { this.offset = offset; }


	/* SECTION methods */

	@Override
	public T negate() {
		T r = clone();
		r.value = Numbers.negate(value).orElseThrow(Globals::rethrowCaughtThrowableStatic);
		r.offset = unboxOptional(Numbers.negate(offset));
		return r;
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public T sum(Iterable<? extends Number> o) {
		T r = clone();

		double p = getParent().map(Number::doubleValue).orElse(1D);
		r.value = Numbers.sum(getValue(), Streams.stream(o).filter(t -> t instanceof NumberRelative<?>).map(t -> (NumberRelative<?>) t).map(t -> (t.doubleValue() - t.getOffset().map(Number::doubleValue).orElse(0D)) / p).collect(Collectors.toList())).orElseThrow(Globals::rethrowCaughtThrowableStatic);

		List<Number> o2 = Streams.stream(o).map(t -> t instanceof NumberRelative<?> ? ((NumberRelative<?>) t).getOffset().map(Number::doubleValue).orElse(0D) : t.doubleValue()).filter(t -> t != 0D).collect(Collectors.toList());
		getOffset().ifPresent(t -> o2.add(0, t));
		r.offset = unboxOptional(Numbers.sum(o2));

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
	public T toImmutable() { return castUncheckedUnboxedNonnull(new Immutable<>(this)); }

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
	public double doubleValue() { return getParent().map(Number::doubleValue).orElse(1D) * getValue().doubleValue() + getOffset().map(Number::doubleValue).orElse(0D); }


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

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T clone() {
		T r = super.clone();
		r.value = tryCloneUnboxedNonnull(value);
		r.parent = tryCloneUnboxed(parent);
		r.offset = tryCloneUnboxed(offset);
		return r;
	}


	/* SECTION static variables */

	private static final long serialVersionUID = -1684871905587506897L;


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<T extends NumberRelative<T>> extends NumberRelative<T> {
		/* SECTION constructors */

		public Immutable(Number value, @Nullable Number parent, @Nullable Number offset) { super(value, parent, offset); }

		public Immutable(Number value, @Nullable Number parent) { this(value, parent, null); }

		public Immutable(Number value) { this(value, null); }

		public Immutable(Immutable<?> c) { this(c.getValue(), unboxOptional(c.getParent()), unboxOptional(c.getOffset())); }


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
		public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

		/** {@inheritDoc} */
		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final boolean isImmutable() { return true; }


		/* SECTION static variables */

		private static final long serialVersionUID = -7462190942914160372L;
	}
}
