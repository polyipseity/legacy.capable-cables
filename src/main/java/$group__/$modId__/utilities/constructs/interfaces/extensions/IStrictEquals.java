package $group__.$modId__.utilities.constructs.interfaces.extensions;

import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;

import javax.annotation.meta.When;
import java.lang.reflect.Field;
import java.util.function.Function;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Reflections.getLowerAndIntermediateSuperclasses;
import static $group__.$modId__.utilities.helpers.Reflections.isMemberStatic;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

public interface IStrictEquals {
	/* SECTION methods */

	@Override
	@OverridingStatus(group = GROUP, when = When.ALWAYS)
	boolean equals(Object o);


	/* SECTION static methods */

	@SuppressWarnings("varargs")
	@SafeVarargs
	static <O> boolean isEqual(O thisObj, Object other, boolean equalsSuper, Function<? super O, ? extends Boolean>... equals) {
		if (thisObj == other)
			return true; // COMMENT simple equals

		Class<?> tc = thisObj.getClass(),
				oc = other.getClass();
		if (!((equalsSuper || tc.getSuperclass() == Object.class) && // COMMENT check equals for super
				tc.isAssignableFrom(oc))) // COMMENT instanceof
			return false;
		for (Class<?> i : getLowerAndIntermediateSuperclasses(oc, castUncheckedUnboxedNonnull(tc)))
			for (Field if_ : i.getDeclaredFields())
				if (!isMemberStatic(if_)) // COMMENT not equals if there are additional instance fields in other
					return false;

		if (equals.length == 0) return false; // COMMENT no fields are immutable
		O o1 = castUncheckedUnboxedNonnull(other);
		for (Function<? super O, ? extends Boolean> ef : equals)
			if (!ef.apply(o1))
				return false;
		return true;
	}
}
