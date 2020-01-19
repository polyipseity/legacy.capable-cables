package $group__.$modId__.utilities.constructs.interfaces.basic;

import $group__.$modId__.utilities.constructs.interfaces.IStructure;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;

import javax.annotation.meta.When;
import java.util.Collection;

import static $group__.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Miscellaneous.Comparables.greaterThan;
import static $group__.$modId__.utilities.helpers.Miscellaneous.Comparables.lessThan;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static java.util.Arrays.asList;

public interface IOperable<T extends IOperable<T, A>, A> {
	/* SECTION methods */

	T negate();

	T sum(Collection<? extends A> o);

	T max(Collection<? extends A> o);

	T min(Collection<? extends A> o);


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


	/* SECTION static classes */

	interface INumberOperable<T extends INumberOperable<T>> extends IOperable<T, Number>, IStructure<T>, Comparable<Number> {
		/* SECTION methods */

		@OverridingStatus(group = GROUP, when = When.MAYBE)
		default T newInstanceFrom(Number o) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }


		@Override
		@OverridingStatus(group = GROUP, when = When.MAYBE)
		default T max(Collection<? extends Number> o) {
			T r = castUnchecked(clone());
			for (Number t : o) if (lessThan(r, t)) r = r.newInstanceFrom(t);
			return r;
		}

		@Override
		@OverridingStatus(group = GROUP, when = When.MAYBE)
		default T min(Collection<? extends Number> o) {
			T r = castUnchecked(clone());
			for (Number t : o) if (greaterThan(r, t)) r.newInstanceFrom(t);
			return r;
		}


		@Override
		String toString();

		@Override
		int hashCode();

		@Override
		boolean equals(Object o);

		T clone();
	}
}
