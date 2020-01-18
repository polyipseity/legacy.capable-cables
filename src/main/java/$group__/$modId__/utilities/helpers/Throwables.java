package $group__.$modId__.utilities.helpers;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.Nullable;
import java.rmi.UnexpectedException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static $group__.$modId__.utilities.helpers.Grammar.appendSuffixIfPlural;
import static $group__.$modId__.utilities.variables.References.LOGGER;

@SuppressWarnings("SpellCheckingInspection")
public enum Throwables {
	/* MARK empty */ ;


	/* SECTION static variables */

	private static final Set<String> RAN_ONCE = Sets.newConcurrentHashSet();
	protected static final Map<String, String> RAN_ONCE_STACKTRACE_STRINGS = new ConcurrentHashMap<>();


	/* SECTION static methods */

	public static <T extends Throwable> T throw_(T t) throws T { throw t; }

	public static Throwable newThrowable() { return new Throwable("Instantiated throwable"); }


	public static StackTraceElement[] getStackTrace() { return newThrowable().getStackTrace(); }


	public static RuntimeException wrapUnhandledThrowable(Throwable t) { return wrapUnhandledThrowable(t, null); }

	public static RuntimeException wrapUnhandledThrowable(Throwable t, @Nullable String msg) { return new RuntimeException(msg, t); }


	public static String rejectAttemptString(String msg, @Nullable Object from, @Nullable Object to) { return (from == null ? "A" : "'" + from + "' a") + "ttempted " + msg + (to == null ? "" : " of '" + to + "'"); }

	public static String rejectAttemptString(String msg, StackTraceElement[] st) { return rejectAttemptString(msg, st.length > 3 ? st[3] : null, st.length > 2 ? st[2] : null); }

	public static String rejectAttemptString(String msg) {
		StackTraceElement[] st = getStackTrace();
		return rejectAttemptString(msg, st.length > 4 ? st[4] : null, st.length > 3 ? st[3] : null);
	}


	public static String rejectObjectsString(String msg, Object... objects) { return "Reject " + msg + ": " + Arrays.toString(objects); }


	public static RuntimeException rejectInstantiation(@Nullable String msg) { throw wrapUnhandledThrowable(new InstantiationException(rejectAttemptString("illegal instantiation" + (msg == null ? "" : ": " + msg)))); }

	public static RuntimeException rejectInstantiation() { throw rejectInstantiation(null); }


	public static void requireRunOnceOnly() {
		Throwable t = newThrowable();
		StackTraceElement[] st = t.getStackTrace();
		if (st.length <= 2) return;
		String cs = st[2].toString();
		if (RAN_ONCE.add(cs)) {
			String sts = ExceptionUtils.getStackTrace(t);
			RAN_ONCE_STACKTRACE_STRINGS.put(cs, sts);
			LOGGER.debug("First ONLY invocation, stacktrace: {}", sts);
			return;
		}
		LOGGER.error("Illegal second invocation, previous stacktrace: {}", RAN_ONCE_STACKTRACE_STRINGS.get(cs));
		throw throw_(new IllegalStateException(rejectAttemptString("illegal second invocation", st)));
	}


	public static UnsupportedOperationException rejectUnsupportedOperation() { throw rejectUnsupportedOperation(null); }

	public static UnsupportedOperationException rejectUnsupportedOperation(@Nullable Throwable cause) { throw throw_(new UnsupportedOperationException(rejectAttemptString("unsupported operation"), cause)); }


	public static IllegalArgumentException rejectArguments(Object... args) { throw throw_(new IllegalArgumentException(rejectObjectsString("illegal argument" + appendSuffixIfPlural(args.length, "s"), args))); }

	public static IllegalArgumentException rejectArguments(Throwable cause, Object... args) { throw throw_(new IllegalArgumentException(rejectObjectsString("illegal argument" + appendSuffixIfPlural(args.length, "s"), args), cause)); }


	public static IllegalArgumentException rejectIndexOutOfBounds(Number index, Number bound) { throw rejectArguments(new IndexOutOfBoundsException("index: " + index + ", bound: " + bound), index); }


	public static RuntimeException unexpectedThrowable(Throwable t) { throw wrapUnhandledThrowable(new UnexpectedException("Unexpected throwable '" + t.getClass() + "': " + t, wrapUnhandledThrowable(t))); }
}
