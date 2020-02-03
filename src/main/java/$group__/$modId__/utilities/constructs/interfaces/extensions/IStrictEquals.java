package $group__.$modId__.utilities.constructs.interfaces.extensions;

import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable;
import $group__.$modId__.utilities.helpers.Reflections.Classes.AccessibleObjectAdapter.FieldAdapter;

import javax.annotation.meta.When;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

import static $group__.$modId__.utilities.helpers.Casts.castChecked;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Reflections.Classes.AccessibleObjectAdapter.FieldAdapter.getWithLogging;
import static $group__.$modId__.utilities.helpers.Reflections.Classes.AccessibleObjectAdapter.setAccessibleWithLogging;
import static $group__.$modId__.utilities.helpers.Reflections.*;
import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static $group__.$modId__.utilities.variables.Globals.caughtThrowableStatic;

public interface IStrictEquals {
	/* SECTION static methods */

	static <T, O> boolean isEqual(T thisObj, O other, Function<? super O, ? extends Boolean> equalsSuper) {
		if (thisObj == other) // COMMENT simple equals
			return true;

		Class<?> tc = thisObj.getClass(), oc = other.getClass();
		if (!((equalsSuper.apply(other) || tc.getSuperclass() == Object.class) && // COMMENT check equals for super
				tc.isAssignableFrom(oc)) || // COMMENT instanceof
				getLowerAndIntermediateSuperclasses(oc, castUncheckedUnboxedNonnull(tc)).stream().flatMap(ic -> Arrays.stream(ic.getDeclaredFields())).anyMatch(f -> !isMemberStatic(f))) // COMMENT not equals if there are additional instance fields in other
			return false;

		final boolean[] r = {false};
		@SuppressWarnings({"rawtypes", "RedundantSuppression"})
		boolean i = castChecked(thisObj, IImmutablizable.class).map(IImmutablizable::isImmutable).orElse(false);
		return getThisAndSuperclasses(tc).stream().allMatch(c -> {
			for (Field var : c.getDeclaredFields()) {
				if (isMemberStatic(var) || !(i || isMemberFinal(var))) continue;
				FieldAdapter varF = FieldAdapter.of(var);

				setAccessibleWithLogging(varF, "field", thisObj, c, true);
				Object vt = getWithLogging(varF, FieldAdapter::get, thisObj, c);
				if (caughtThrowableStatic()) return false;
				Object vo = getWithLogging(varF, FieldAdapter::get, other, c);
				if (caughtThrowableStatic() || !Objects.equals(vt, vo)) return false;

				r[0] = true;
			}
			return true;
		}) && r[0];
	}


	/* SECTION methods */

	@Override
	@OverridingStatus(group = GROUP, when = When.ALWAYS)
	boolean equals(Object o);
}
