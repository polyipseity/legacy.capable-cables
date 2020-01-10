package etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public enum Types {
	;
	public static boolean isClassAbstract(Class<?> c) { return c.isInterface() || Modifier.isAbstract(c.getModifiers()); }

	public static String getMethodNameDescriptor(Method m) { return m.getName() + org.objectweb.asm.Type.getMethodDescriptor(m); }

	public static boolean isFormerOverriddenByLatter(Method a, Method b) {
		if (!getMethodNameDescriptor(a).equals(getMethodNameDescriptor(b))) return false;
		Class<?>[] ap = a.getParameterTypes();
		Class<?>[] bp = a.getParameterTypes();
		if (ap.length != bp.length) return false;
		for (int i = 0; i < ap.length; i++) { if (!ap[i].isAssignableFrom(bp[i])) return false; }
		return true;
	}


	public static Type[] getGenericSuperclassActualTypeArguments(Class<?> c) { return ((ParameterizedType) c.getGenericSuperclass()).getActualTypeArguments(); }


	@SuppressWarnings("unchecked")
	public static <T extends Type> T getGenericSuperclassActualTypeArgument(Class<?> c, int i) { return (T) getGenericSuperclassActualTypeArguments(c)[i]; }
}
