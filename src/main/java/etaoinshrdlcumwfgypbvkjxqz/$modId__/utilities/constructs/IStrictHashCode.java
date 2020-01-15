package etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs;

import javax.annotation.meta.When;
import java.util.Objects;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.variables.Constants.GROUP;

public interface IStrictHashCode {
	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.ALWAYS)
	int hashCode();


	/* SECTION static methods */

	static int getHashCode(Object t, int hashCodeSuper, Object... v) {
		if (t.getClass().getSuperclass() == Object.class) return Objects.hash(v);
		else return Objects.hash(hashCodeSuper, v);
	}
}
