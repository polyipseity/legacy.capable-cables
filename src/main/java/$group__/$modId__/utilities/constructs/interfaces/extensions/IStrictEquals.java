package $group__.$modId__.utilities.constructs.interfaces.extensions;

import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;

import javax.annotation.meta.When;
import java.util.Arrays;
import java.util.function.Function;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Reflections.getLowerAndIntermediateSuperclasses;
import static $group__.$modId__.utilities.helpers.Reflections.isMemberStatic;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

public interface IStrictEquals {
	/* SECTION static methods */

	@SuppressWarnings("varargs")
	@SafeVarargs
	static <O> boolean isEqual(O thisObj, Object other, boolean equalsSuper, Function<? super O, ? extends Boolean>... equals) {
		if (thisObj == other) // COMMENT simple equals
			return true;

		Class<?> tc = thisObj.getClass(), oc = other.getClass();
		if (!((equalsSuper || tc.getSuperclass() == Object.class) && // COMMENT check equals for super
				tc.isAssignableFrom(oc)) || // COMMENT instanceof
				getLowerAndIntermediateSuperclasses(oc, castUncheckedUnboxedNonnull(tc)).stream().flatMap(ic -> Arrays.stream(ic.getDeclaredFields())).anyMatch(f -> !isMemberStatic(f)) || // COMMENT not equals if there are additional instance fields in other
				equals.length == 0) // COMMENT no fields are immutable
			return false;

		O o1 = castUncheckedUnboxedNonnull(other);
		return Arrays.stream(equals).allMatch(ef -> ef.apply(o1));
	}


	/* SECTION methods */

	@Override
	@OverridingStatus(group = GROUP, when = When.ALWAYS)
	boolean equals(Object o);
}
