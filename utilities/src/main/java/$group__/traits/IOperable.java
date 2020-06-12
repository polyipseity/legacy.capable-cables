package $group__.traits;

import $group__.annotations.OverridingStatus;
import $group__.utilities.Constants;
import $group__.utilities.extensions.ICloneable;
import $group__.utilities.extensions.IStructure;
import $group__.utilities.helpers.specific.Comparables;
import $group__.utilities.helpers.specific.Throwables;

import javax.annotation.meta.When;
import java.util.concurrent.atomic.AtomicReference;

import static $group__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
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

	interface INumberOperable<T extends INumberOperable<T>> extends IOperable<T, Number>, IStructure<T, T>,
			ICloneable<T>, Comparable<Number> {
		/* SECTION methods */

		@Override
		@OverridingStatus(group = Constants.PACKAGE, when = When.MAYBE)
		default T max(Iterable<? extends Number> o) {
			AtomicReference<T> r = new AtomicReference<>(castUncheckedUnboxedNonnull(this));
			o.forEach(ot -> r.updateAndGet(rt -> Comparables.lessThan(rt, ot) ? rt.cloneFrom(ot) : rt));
			return r.get();
		}

		@Override
		@OverridingStatus(group = Constants.PACKAGE, when = When.MAYBE)
		default T min(Iterable<? extends Number> o) {
			AtomicReference<T> r = new AtomicReference<>(castUncheckedUnboxedNonnull(this));
			o.forEach(ot -> r.updateAndGet(rt -> Comparables.greaterThan(rt, ot) ? rt.cloneFrom(ot) : rt));
			return r.get();
		}

		// TODO: replace with cast system
		@OverridingStatus(group = Constants.PACKAGE, when = When.MAYBE)
		default T cloneFrom(Number o) throws UnsupportedOperationException { throw Throwables.rejectUnsupportedOperation(); }
	}
}
