package etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs;

import javax.annotation.meta.When;
import java.util.function.Function;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.variables.Constants.GROUP;

public interface IStrictEquals {
	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.ALWAYS)
	boolean equals(Object o);


	/* SECTION static methods */

	@SuppressWarnings("varargs")
	@SafeVarargs
	static <O> boolean isEquals(O t, Object o, boolean equalsSuper, Function<O, Boolean>... v) {
		if (t == o) return true;
		if (!(equalsSuper && t.getClass().isAssignableFrom(o.getClass()))) return false;

		O oc = castUnchecked(o);
		for (Function<O, Boolean> vp : v) if (!vp.apply(oc)) return false;
		return true;
	}
}