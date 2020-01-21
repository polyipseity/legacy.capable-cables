package $group__.$modId__.utilities.helpers;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static $group__.$modId__.utilities.helpers.Grammar.appendSuffixIfPlural;
import static $group__.$modId__.utilities.variables.References.LOGGER;

@SuppressWarnings("SpellCheckingInspection")
public enum Throwables {
	/* MARK empty */ ;


	/* SECTION static variables */

	private static final Set<String> RAN_ONCE = Sets.newConcurrentHashSet();
	protected static final ConcurrentHashMap<String, String> RAN_ONCE_STACKTRACE_STRINGS = new ConcurrentHashMap<>();


	/* SECTION static methods */

	public static <T extends Throwable> T throw_(T t) throws T { throw t; }

	public static Throwable newThrowable() { return new Throwable("Instantiated throwable"); }


	public static StackTraceElement[] getStackTrace() { return newThrowable().getStackTrace(); }


	public static RuntimeException wrapUnhandledThrowable(Throwable t) throws RuntimeException { return wrapUnhandledThrowable(t, null); }

	public static RuntimeException wrapUnhandledThrowable(Throwable t, @Nullable String msg) throws RuntimeException { return new RuntimeException(msg, t); }


	public static String rejectAttemptString(String msg, @Nullable Object from, @Nullable Object to) { return (from == null ? "A" : "'" + from + "' a") + "ttempted " + msg + (to == null ? "" : " of '" + to + "'"); }

	public static String rejectAttemptString(String msg, StackTraceElement[] st) { return rejectAttemptString(msg, st.length > 3 ? st[3] : null, st.length > 2 ? st[2] : null); }

	public static String rejectAttemptString(String msg) {
		StackTraceElement[] st = getStackTrace();
		return rejectAttemptString(msg, st.length > 4 ? st[4] : null, st.length > 3 ? st[3] : null);
	}


	public static String rejectObjectsString(String msg, Object... objects) { return "Reject " + msg + ": " + Arrays.toString(objects); }


	public static RuntimeException rejectInstantiation(@Nullable String msg) throws RuntimeException { throw wrapUnhandledThrowable(new InstantiationException(rejectAttemptString("illegal instantiation" + (msg == null ? "" : ": " + msg)))); }

	public static RuntimeException rejectInstantiation() throws RuntimeException { throw rejectInstantiation(null); }


	public static void requireRunOnceOnly() throws IllegalStateException {
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


	public static UnsupportedOperationException rejectUnsupportedOperation() throws UnsupportedOperationException { throw rejectUnsupportedOperation(null); }

	public static UnsupportedOperationException rejectUnsupportedOperation(@Nullable Throwable cause) throws UnsupportedOperationException { throw throw_(new UnsupportedOperationException(rejectAttemptString("unsupported operation"), cause)); }


	public static IllegalArgumentException rejectArguments(Object... args) throws IllegalArgumentException { throw throw_(new IllegalArgumentException(rejectObjectsString("illegal argument" + appendSuffixIfPlural(args.length, "s"), args))); }

	public static IllegalArgumentException rejectArguments(Throwable cause, Object... args) throws IllegalArgumentException { throw throw_(new IllegalArgumentException(rejectObjectsString("illegal argument" + appendSuffixIfPlural(args.length, "s"), args), cause)); }


	public static IllegalArgumentException rejectIndexOutOfBounds(Number index, Number bound) throws IllegalArgumentException { throw rejectArguments(new IndexOutOfBoundsException("index: " + index + ", bound: " + bound), index);
	}

	public static InternalError unexpected(@Nullable String msg, @Nullable Throwable t) throws InternalError {
		throw throw_(new InternalError(msg, t));
	}

	public static InternalError unexpected(Throwable t) throws InternalError { throw unexpected(null, t); }

	public static InternalError unexpected(String msg) throws InternalError { throw unexpected(msg, null); }

	public static InternalError unexpected() throws InternalError { throw unexpected(null, null); }


	public static InterruptedException interrupt(@Nullable String msg) throws InterruptedException { throw throw_(new InterruptedException(msg)); }

	public static InterruptedException interrupt() throws InterruptedException { throw interrupt(null); }
}
