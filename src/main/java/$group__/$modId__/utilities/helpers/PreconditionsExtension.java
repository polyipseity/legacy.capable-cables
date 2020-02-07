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

public enum PreconditionsExtension {
	/* MARK empty */;


	/* SECTION static variables */

	private static final ConcurrentMap<Class<?>, String> RAN_ONCE = MAP_MAKER_MULTI_THREAD.makeMap();


	/* SECTION static methods */

	public static void checkArgumentTypes(Class<?>[] types, Object... args) {
		int typesLength = types.length;
		checkElementIndex(typesLength, args.length);
		for (int i = 0; i < typesLength; i++) {
			@Nullable Object arg = args[i];
			checkArgument(arg == null || types[i].isAssignableFrom(arg.getClass()));
		}
	}

	public static void checkArrayContentType(Class<?> type, Object... array) { for (@Nullable Object o : array) checkArgument(o == null || type.isAssignableFrom(o.getClass())); }


	public static void requireRunOnceOnly(Logger logger) throws IllegalStateException {
		String sts = getCurrentStackTraceString();

		@Nullable String stsO = RAN_ONCE.put(getCallerClass(), sts);
		if (stsO != null) {
			logger.error(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Illegal second invocation, previous stacktrace:{}{}", lineSeparator(), stsO));
			throw throw_(new IllegalStateException(rejectAttemptString("illegal second invocation", stsO)));
		}

		logger.error(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("First ONLY invocation, stacktrace:{}{}", lineSeparator(), sts));
	}
}
