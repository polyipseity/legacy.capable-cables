package $group__.$modId__.utilities.helpers;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public enum Loggers {
	/* MARK empty */;


	/* SECTION static variables */

	// CODE "{1:}, stacktrace:\n{2:}"
	public static final BiFunction<Supplier<? extends String>, Throwable, Supplier<String>> FORMATTER_WITH_THROWABLE = (s, t) -> () -> s.get() + ", stacktrace:\n" + getStackTrace(t);


	// CODE "Unable to set {1:} '{2:}'{3:nonnull: of object '{3:nullable}'} of class '{4:}' {5:false:in}accessible"
	public static final BiFunction<Supplier<? extends String>, AccessibleObject, BiFunction<Object, Class<?>, Function<Boolean, Supplier<String>>>> FORMATTER_REFLECTION_UNABLE_TO_SET_ACCESSIBLE = (ad, a) -> (@Nullable Object o, Class<?> c) -> b -> () -> "Unable to set " + ad.get() + " '" + a + "'" + (o == null ? StringUtils.EMPTY : " of object '" + o + "'") + " of class '" + c.toGenericString() + "' " + (b ? StringUtils.EMPTY : "in") + "accessible";

	// CODE "Unable to set field '{1:}'{2:nonnull: of object '{2:nullable}'} of class '{3:}' to value '{4:nullable}'"
	public static final BiFunction<Field, Object, BiFunction<Class<?>, Object, Supplier<String>>> FORMATTER_REFLECTION_UNABLE_TO_SET_FIELD = (Field f, @Nullable Object o) -> (Class<?> c, @Nullable Object v) -> () -> "Unable to set field '" + f.toGenericString() + "'" + (o == null ? StringUtils.EMPTY : " of object '" + o + "'") + " of class '" + c.toGenericString() + "' to value '" + v + "'";
}
