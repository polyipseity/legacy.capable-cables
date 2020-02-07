package $group__.$modId__.utilities;

import $group__.$modId__.annotations.OverridingStatus;
import $group__.$modId__.traits.basic.IDirty;
import $group__.$modId__.utilities.helpers.specific.Numbers;
import $group__.$modId__.utilities.helpers.specific.Throwables;
import com.google.common.collect.Streams;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.specific.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

public class NumberRelative<T extends NumberRelative<T>> extends NumberDefault<T> implements IDirty {
	/* SECTION static variables */

	private static final long serialVersionUID = -1684871905587506897L;


	/* SECTION variables */

	protected Number value;
	@Nullable protected Number parent;
	@Nullable protected Number offset;

	protected long dirtiness;


	/* SECTION constructors */

	public NumberRelative(Number value) { this(value, null); }

	public NumberRelative(Number value, @Nullable Number parent) { this(value, parent, null); }

	public NumberRelative(Number value, @Nullable Number parent, @Nullable Number offset) {
		this.value = value;
		this.parent = parent;
		this.offset = offset;
	}

	@SuppressWarnings("CopyConstructorMissesField")
	public NumberRelative(NumberRelative<?> copy) { this(copy.getValue(), unboxOptional(copy.getParent()), unboxOptional(copy.getOffset())); }


	/* SECTION getters & setters */

	public Number getValue() { return value; }

	public void setValue(Number value) {
		this.value = value;
		markDirty(LOGGER);
	}

	public Optional<Number> getParent() { return Optional.ofNullable(parent); }

	public void setParent(@Nullable Number parent) {
		this.parent = parent;
		markDirty(LOGGER);
	}

	public Optional<Number> getOffset() { return Optional.ofNullable(offset); }

	public void setOffset(@Nullable Number offset) {
		this.offset = offset;
		markDirty(LOGGER);
	}


	/* SECTION methods */

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public T sum(Iterable<? extends Number> o) {
		T r = copy();

		double p = getParent().map(Number::doubleValue).orElse(1D);
		r.value = Numbers.sum(getValue(), Streams.stream(o).filter(t -> t instanceof NumberRelative<?>).map(t -> (NumberRelative<?>) t).map(t -> (t.doubleValue() - t.getOffset().map(Number::doubleValue).orElse(0D)) / p).collect(Collectors.toList()), ).orElseThrow(Throwables::rethrowCaughtThrowableStatic);

		List<Number> o2 = Streams.stream(o).map(t -> t instanceof NumberRelative<?> ? ((NumberRelative<?>) t).getOffset().map(Number::doubleValue).orElse(0D) : t.doubleValue()).filter(t -> t != 0D).collect(Collectors.toList());
		getOffset().ifPresent(t -> o2.add(0, t));
		r.offset = unboxOptional(Numbers.sum(o2, ));

		return r;
	}

	@Override
	public T negate() {
		T r = copy();
		r.value = Numbers.negate(value).orElseThrow(Throwables::rethrowCaughtThrowableStatic);
		r.offset = unboxOptional(Numbers.negate(offset));
		return r;
	}

	@Override
	public T cloneFrom(Number o) {
		T r = copy();
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

	@Override
	public int compareTo(Number o) {
		boolean greater = doubleValue() > o.doubleValue();
		boolean lesser = doubleValue() < o.doubleValue();
		return greater ? 1 : lesser ? -1 : 0;
	}

	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull(new Immutable<>(this)); }

	@Override
	public boolean isImmutable() { return false; }

	@Override
	public int intValue() { return (int) doubleValue(); }

	@Override
	public long longValue() { return (long) doubleValue(); }

	@Override
	public float floatValue() { return (float) doubleValue(); }

	@Override
	public double doubleValue() { return getParent().map(Number::doubleValue).orElse(1D) * getValue().doubleValue() + getOffset().map(Number::doubleValue).orElse(0D); }


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<T extends NumberRelative<T>> extends NumberRelative<T> {
		/* SECTION static variables */

		private static final long serialVersionUID = -7462190942914160372L;


		/* SECTION constructors */

		public Immutable(Number value) { this(value, null); }

		public Immutable(Number value, @Nullable Number parent) { this(value, parent, null); }

		public Immutable(Number value, @Nullable Number parent, @Nullable Number offset) { super(value, parent, offset); }

		public Immutable(Immutable<?> c) { this(c.getValue(), unboxOptional(c.getParent()), unboxOptional(c.getOffset())); }


		/* SECTION getters & setters */

		@Override
		@Deprecated
		public void setLogger(Logger value) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public void setValue(Number value) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public void setParent(@Nullable Number parent) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public void setOffset(@Nullable Number offset) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		@Override
		@OverridingStatus(group = GROUP)
		public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

		@Override
		@OverridingStatus(group = GROUP)
		public final boolean isImmutable() { return true; }
	}
}
