package $group__.$modId__.traits;

import $group__.$modId__.annotations.OverridingStatus;
import $group__.$modId__.utilities.extensions.ICloneable;
import $group__.$modId__.utilities.extensions.IStructure;

import javax.annotation.meta.When;
import java.util.concurrent.atomic.AtomicReference;

import static $group__.$modId__.utilities.Constants.PACKAGE;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.specific.Comparables.greaterThan;
import static $group__.$modId__.utilities.helpers.specific.Comparables.lessThan;
import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectUnsupportedOperation;
import static java.util.Arrays.asList;

public interface IOperable<T extends IOperable<T, A>, A> {
	/* SECTION static methods */

	@SuppressWarnings("varargs")
	@SafeVarargs
	static <N extends IOperable<N, A>, A> N sum(N t, A... o) { return t.sum(asList(o)); }

	@SuppressWarnings("varargs")
	@SafeVarargs
	static <N extends IOperable<N, A>, A> N max(N t, A... o) { return t.max(asList(o)); }

	@SuppressWarnings("varargs")
	@SafeVarargs
	static <N extends IOperable<N, A>, A> N min(N t, A... o) { return t.min(asList(o)); }


	/* SECTION methods */

	T sum(Iterable<? extends A> o);

	T max(Iterable<? extends A> o);

	T min(Iterable<? extends A> o);

	T negate();


	/* SECTION static classes */

	interface INumberOperable<T extends INumberOperable<T>> extends IOperable<T, Number>, IStructure<T, T>, ICloneable<T>, Comparable<Number> {
		/* SECTION methods */

		@Override
		@OverridingStatus(group = PACKAGE, when = When.MAYBE)
		default T max(Iterable<? extends Number> o) {
			AtomicReference<T> r = new AtomicReference<>(castUncheckedUnboxedNonnull(this));
			o.forEach(ot -> r.updateAndGet(rt -> lessThan(rt, ot) ? rt.cloneFrom(ot) : rt));
			return r.get();
		}

		@Override
		@OverridingStatus(group = PACKAGE, when = When.MAYBE)
		default T min(Iterable<? extends Number> o) {
			AtomicReference<T> r = new AtomicReference<>(castUncheckedUnboxedNonnull(this));
			o.forEach(ot -> r.updateAndGet(rt -> greaterThan(rt, ot) ? rt.cloneFrom(ot) : rt));
			return r.get();
		}

		// TODO: replace with cast system
		@OverridingStatus(group = PACKAGE, when = When.MAYBE)
		default T cloneFrom(Number o) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }
	}
}
