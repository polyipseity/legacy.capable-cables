package $group__.$modId__.utilities.constructs.interfaces.basic;

import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;

import javax.annotation.meta.When;

import static $group__.$modId__.utilities.helpers.Throwables.rejectArguments;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

public interface IStrictToString {
	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.ALWAYS)
	String toString();


	/* SECTION static methods */

	static String getToStringString(Object o, String stringSuper, Object[]... vars) {
		String classN = o.getClass().getName();
		StringBuilder sb = new StringBuilder(classN).append("{");
		for (Object[] var : vars) {
			if (var.length != 2) throw rejectArguments((Object) vars);
			sb.append(var[0]).append("=").append(var[1]);
		}
		return sb.append("}").append(stringSuper.replaceFirst(classN, "")).toString();
	}
}
