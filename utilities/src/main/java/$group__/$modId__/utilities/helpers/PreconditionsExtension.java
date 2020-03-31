package $group__.$modId__.utilities.helpers;

import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentMap;

import static $group__.$modId__.utilities.helpers.Dynamics.getCallerClass;
import static $group__.$modId__.utilities.helpers.specific.Loggers.EnumMessages.FACTORY_PARAMETERIZED_MESSAGE;
import static $group__.$modId__.utilities.helpers.specific.MapsExtension.MAP_MAKER_MULTI_THREAD;
import static $group__.$modId__.utilities.helpers.specific.Throwables.*;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;
import static java.lang.System.lineSeparator;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public enum PreconditionsExtension {
	/* MARK empty */;


	/* SECTION static variables */

	private static final ConcurrentMap<Class<?>, Throwable> RAN_ONCE = MAP_MAKER_MULTI_THREAD.makeMap();


	/* SECTION static methods */

	public static void checkArgumentTypes(Class<?>[] types, Object... args) {
		int typesLength = types.length;
		checkElementIndex(typesLength - 1, args.length);
		for (int i = 0; i < typesLength; i++) {
			@Nullable Object arg = args[i];
			checkArgument(arg == null || types[i].isAssignableFrom(arg.getClass()));
		}
	}

	public static void checkArrayContentType(Class<?> type, Object... array) { for (@Nullable Object o : array) checkArgument(o == null || type.isAssignableFrom(o.getClass())); }


	public static void requireRunOnceOnly(@Nullable Logger logger) throws IllegalStateException {
		Throwable t = newThrowable();

		@Nullable Throwable t1 = RAN_ONCE.put(getCallerClass(), t);
		if (t1 != null) {
			logger.error(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Illegal second invocation, previous stacktrace:{}{}", lineSeparator(), getStackTrace(t1)));
			throw throw_(new IllegalStateException(rejectAttemptString("illegal second invocation", t.getStackTrace())));
		}

		logger.error(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("First ONLY invocation, stacktrace:{}{}", lineSeparator(), t));
	}
}
