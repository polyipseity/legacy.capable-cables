package $group__.$modId__.utilities.constructs.interfaces.extensions;

import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;

import javax.annotation.meta.When;
import java.util.Objects;

import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static com.google.common.collect.Lists.asList;

public interface IStrictHashCode {
	/* SECTION methods */

	@Override
	@OverridingStatus(group = GROUP, when = When.ALWAYS)
	int hashCode();


	/* SECTION static methods */

	static int getHashCode(Object t, int hashCodeSuper, Object... v) {
		if (t.getClass().getSuperclass() == Object.class) {
			if (v.length == 0) // COMMENT no fields are immutable
				return hashCodeSuper;
			else
				return Objects.hash(v); // COMMENT ignore Object hashCode
		}
		else return Objects.hash(asList(hashCodeSuper, v).toArray());
	}
}
