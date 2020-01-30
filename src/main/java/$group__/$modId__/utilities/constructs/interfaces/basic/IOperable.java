package $group__.$modId__.utilities.constructs.interfaces.basic;

import $group__.$modId__.utilities.constructs.interfaces.IStructureCloneable;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;

import javax.annotation.meta.When;
import java.util.concurrent.atomic.AtomicReference;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Comparables.greaterThan;
import static $group__.$modId__.utilities.helpers.Comparables.lessThan;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;
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

	interface INumberOperable<T extends INumberOperable<T>> extends IOperable<T, Number>, IStructureCloneable<T>, Comparable<Number> {
		/* SECTION methods */

		@Override
		@OverridingStatus(group = GROUP, when = When.MAYBE)
		default T max(Iterable<? extends Number> o) {
			AtomicReference<T> r = new AtomicReference<>(castUncheckedUnboxedNonnull(this));
			o.forEach(ot -> r.updateAndGet(rt -> lessThan(rt, ot) ? rt.cloneFrom(ot) : rt));
			return r.get();
		}

		@Override
		@OverridingStatus(group = GROUP, when = When.MAYBE)
		default T min(Iterable<? extends Number> o) {
			AtomicReference<T> r = new AtomicReference<>(castUncheckedUnboxedNonnull(this));
			o.forEach(ot -> r.updateAndGet(rt -> greaterThan(rt, ot) ? rt.cloneFrom(ot) : rt));
			return r.get();
		}

		@OverridingStatus(group = GROUP, when = When.MAYBE)
		default T cloneFrom(Number o) { throw rejectUnsupportedOperation(); }
	}
}
