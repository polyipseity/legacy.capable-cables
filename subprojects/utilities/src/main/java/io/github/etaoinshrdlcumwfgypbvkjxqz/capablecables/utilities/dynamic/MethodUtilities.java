package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic;

import java.lang.reflect.Method;
import java.util.stream.IntStream;

public enum MethodUtilities {
	;

	public static boolean overrides(Method overrider, Method overridden) {
		if (!(overridden.getName().equals(overrider.getName()) && overridden.getReturnType().isAssignableFrom(overrider.getReturnType())))
			return false;
		Class<?>[] np = overridden.getParameterTypes(), gp = overrider.getParameterTypes();
		return np.length == gp.length && IntStream.range(0, np.length).allMatch(i -> gp[i].isAssignableFrom(np[i]));
	}
}
