package $group__.utilities;

import $group__.utilities.collections.MapUtilities;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

import static $group__.utilities.DynamicUtilities.getCallerStackTraceElement;
import static $group__.utilities.LoggerUtilities.EnumMessages.FACTORY_PARAMETERIZED_MESSAGE;
import static $group__.utilities.LoggerUtilities.EnumMessages.FACTORY_SIMPLE_MESSAGE;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;
import static java.lang.System.lineSeparator;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public enum PreconditionUtilities {
	;

	private static final Map<StackTraceElement, Throwable> RAN_ONCE = MapUtilities.newMapMakerNormalThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_LARGE).makeMap();


	public static void checkArgumentTypes(Class<?>[] types, Object... args) {
		int typesLength = types.length;
		checkElementIndex(typesLength - 1, args.length);
		for (int i = 0; i < typesLength; ++i) {
			@Nullable Object arg = args[i];
			checkArgument(arg == null || types[i].isInstance(arg));
		}
	}

	public static void checkArrayContentType(Class<?> type, Object... array) {
		for (@Nullable Object o : array)
			checkArgument(o == null || type.isInstance(o));
	}


	public static void requireRunOnceOnly(@Nullable Logger logger) throws IllegalStateException {
		Throwable t = ThrowableUtilities.create();

		Optional<StackTraceElement> st = getCallerStackTraceElement();
		if (!st.isPresent()) {
			if (logger != null)
				logger.warn(() -> FACTORY_SIMPLE_MESSAGE.makeMessage("Unable to obtain stacktrace"));
			return;
		}
		@Nullable Throwable t1;
		synchronized (st.toString().intern()) {
			t1 = RAN_ONCE.put(st.get(), t);
		}
		if (t1 != null) {
			if (logger != null)
				logger.error(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Illegal second invocation, previous stacktrace:{}{}", lineSeparator(), getStackTrace(t1)));
			throw new IllegalStateException("Illegal second invocation", t1);
		}

		if (logger != null)
			logger.debug(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("First ONLY invocation, stacktrace:{}{}", lineSeparator(), getStackTrace(t)));
	}
}
