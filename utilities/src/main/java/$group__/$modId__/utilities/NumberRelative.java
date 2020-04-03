package $group__.$modId__.utilities;

import $group__.$modId__.annotations.OverridingStatus;
import $group__.$modId__.logging.ILogging;
import $group__.$modId__.logging.ILoggingUser;
import $group__.$modId__.traits.IOperable;
import $group__.$modId__.utilities.builders.BuilderNumberRelative;
import $group__.$modId__.utilities.concurrent.IMutator;
import $group__.$modId__.utilities.concurrent.IMutatorImmutablizable;
import $group__.$modId__.utilities.concurrent.IMutatorUser;
import $group__.$modId__.utilities.extensions.ICloneable;
import $group__.$modId__.utilities.extensions.IStructure;
import $group__.$modId__.utilities.helpers.specific.Numbers;
import $group__.$modId__.utilities.helpers.specific.Throwables;
import com.google.common.collect.Streams;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.meta.When;
import java.util.Optional;

import static $group__.$modId__.utilities.Constants.PACKAGE;
import static $group__.$modId__.utilities.extensions.IStrictEquals.areEqual;
import static $group__.$modId__.utilities.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.specific.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectUnsupportedOperationIf;

public class NumberRelative<T extends NumberRelative<T>> extends Number implements IStructure<T, T>, ICloneable<T>,
		IOperable.INumberOperable<T>, IMutatorUser<IMutatorImmutablizable<?, ?>>, ILoggingUser<ILogging<Logger>,
				Logger> {
	/* SECTION static variables */

	private static final long serialVersionUID = -1684871905587506897L;


	/* SECTION variables */

	protected Number value;
	@Nullable
	protected Number parent;
	@Nullable
	protected Number offset;

	protected IMutatorImmutablizable<?, ?> mutator;
	protected ILogging<Logger> logging;


	/* SECTION constructors */

	public NumberRelative(Number value, IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) {
		this(value,
				null, mutator, logging);
	}

	public NumberRelative(Number value, @Nullable Number parent, IMutatorImmutablizable<?, ?> mutator,
	                      ILogging<Logger> logging) { this(value, parent, null, mutator, logging); }

	public NumberRelative(Number value, @Nullable Number parent, @Nullable Number offset,
	                      IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) {
		this.mutator = IMutator.trySetNonnull(mutator, mutator, true);
		this.logging = IMutator.trySetNonnull(getMutator(), logging, true);
		this.value = IMutator.trySetNonnull(getMutator(), value, true);
		this.parent = IMutator.trySet(getMutator(), parent, true);
		this.offset = IMutator.trySet(getMutator(), offset, true);
	}

	public NumberRelative(NumberRelative<?> copy) { this(copy, copy.getMutator()); }


	protected NumberRelative(NumberRelative<?> copy, IMutatorImmutablizable<?, ?> mutator) {
		this(copy.getValue(),
				copy.getParent(), copy.getOffset(), mutator, copy.getLogging());
	}


	/* SECTION static methods */

	public static <T extends BuilderNumberRelative<T, V>, V extends NumberRelative<V>> BuilderNumberRelative<T, V> newBuilderNR(Number value) { return new BuilderNumberRelative<>(t -> castUncheckedUnboxedNonnull(new NumberRelative<>(value, t.parent, t.offset, t.mutator, t.logging))); }


	/* SECTION getters & setters */

	public Number getValue() { return value; }

	public void setValue(Number value) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetValue(value)); }

	public boolean trySetValue(Number value) { return trySet(t -> this.value = t, value); }

	public Optional<Number> tryGetValue() { return Optional.of(getValue()); }

	@Nullable
	public Number getParent() { return parent; }

	public void setParent(@Nullable Number parent) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetParent(parent)); }

	public boolean trySetParent(@Nullable Number parent) { return trySet(t -> this.parent = t, parent); }

	public Optional<Number> tryGetParent() { return Optional.ofNullable(getParent()); }

	@Nullable
	public Number getOffset() { return offset; }

	public void setOffset(Number offset) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetOffset(offset)); }

	public boolean trySetOffset(Number offset) { return trySet(t -> this.offset = t, offset); }

	public Optional<Number> tryGetOffset() { return Optional.ofNullable(getOffset()); }

	@Nonnull
	@Override
	public IMutatorImmutablizable<?, ?> getMutator() { return mutator; }

	@Override
	public boolean trySetMutator(IMutatorImmutablizable<?, ?> mutator) {
		return trySet(t -> this.mutator = t,
				mutator);
	}

	public ILogging<Logger> getLogging() { return logging; }

	public boolean trySetLogging(ILogging<Logger> logging) { return trySet(t -> this.logging = t, logging); }


	/* SECTION methods */

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public T sum(Iterable<? extends Number> o) {
		T r = copy();

		double p = tryGetParent().map(Number::doubleValue).orElse(1D);
		r.value = Streams.stream(o).filter(t -> t instanceof NumberRelative<?>).reduce(getValue().doubleValue(), (t,
		                                                                                                          u) -> t + (u.doubleValue() - ((NumberRelative<?>) u).tryGetOffset().map(Number::doubleValue).orElse(0D)) / p, Double::sum);

		r.offset = Streams.stream(o).reduce(tryGetOffset().map(Number::doubleValue).orElse(0D),
				(t, u) -> t + (u instanceof NumberRelative<?> ?
						((NumberRelative<?>) u).tryGetOffset().map(Number::doubleValue).orElse(0D) : u.doubleValue()),
				Double::sum);

		return r;
	}

	@Override
	public T negate() {
		T r = copy();
		r.value = Numbers.negate(value, getLogger()).orElseThrow(Throwables::rethrowCaughtThrowableStatic);
		r.offset = unboxOptional(Numbers.negate(offset, getLogger()));
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
			r.parent = r.offset = null;
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
	public int intValue() { return (int) doubleValue(); }

	@Override
	public long longValue() { return (long) doubleValue(); }

	@Override
	public float floatValue() { return (float) doubleValue(); }

	@Override
	public double doubleValue() { return tryGetParent().map(Number::doubleValue).orElse(1D) * getValue().doubleValue() + tryGetOffset().map(Number::doubleValue).orElse(0D); }


	@Override
	public T toImmutable() {
		return castUncheckedUnboxedNonnull(isImmutable() ? this : new NumberRelative<>(this,
				IMutatorImmutablizable.of(getMutator().toImmutable())));
	}

	@Override
	public boolean isImmutable() { return getMutator().isImmutable(); }


	@Override
	@OverridingStatus(group = PACKAGE)
	public final String toString() { return getToStringString(this, super.toString()); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	@OverridingStatus(group = PACKAGE)
	public final boolean equals(Object o) { return areEqual(this, o, super::equals); }

	@Override
	@OverridingStatus(group = PACKAGE)
	public final int hashCode() { return getHashCode(this, super::hashCode); }

	@SuppressWarnings("Convert2MethodRef")
	@Override
	@OverridingMethodsMustInvokeSuper
	@OverridingStatus(group = PACKAGE, when = When.MAYBE)
	public T clone() { return ICloneable.clone(() -> super.clone(), getLogger()); }
}
