package $group__.$modId__.utilities.constructs.interfaces.basic;

import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.variables.Globals;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.meta.When;
import java.util.regex.Pattern;

import static $group__.$modId__.utilities.helpers.Reflections.Unsafe.getMethod;
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
		String classN = o.getClass().getSimpleName();
		StringBuilder sb = new StringBuilder(classN).append("{");
		boolean f = false;
		for (Object[] var : vars) {
			if (var.length != 2) throw rejectArguments((Object) vars);
			if (f) sb.append(", ");

			Object v = var[1];
			Class<?> vc = v.getClass();
			sb.append(var[0]).append("=").append(getMethod(vc, "toString").get().orElseThrow(Globals::rethrowCaughtThrowableStatic).getDeclaringClass() == Object.class ? vc.getSimpleName() + "@" + Integer.toHexString(v.hashCode()) : v.toString());

			f = true;
		}
		return sb.append("}").append(stringSuper.replaceFirst(Pattern.quote(classN), StringUtils.EMPTY)).toString();
	}
}
