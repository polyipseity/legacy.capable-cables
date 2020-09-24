package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Arrays;
import java.util.Optional;

public enum StackTraceUtilities {
	;

	public static Class<?> getCurrentClass() { return getClassStackTrace(1); }

	public static Class<?> getClassStackTrace(int depth) { return getClassStackTrace()[1 + depth]; }

	public static Class<?>[] getClassStackTrace() {
		Class<?>[] r = SecurityManagerHolder.getInstance().getClassContext();
		return Arrays.copyOfRange(r, 1, r.length);
	}

	public static Class<?> getCallerClass() { return getClassStackTrace(2); }

	public static Optional<StackTraceElement> getCallerStackTraceElement() {
		/* COMMENT
		0 - this method
		1 - caller of this method
		2 - caller of the caller of this method
		 */
		return Optional.of(getCurrentStackTraceElements())
				.filter(stacktrace -> stacktrace.length > 2)
				.map(stacktrace -> stacktrace[2]);
	}

	public static StackTraceElement[] getCurrentStackTraceElements() {
		StackTraceElement[] ret = ThrowableUtilities.create().getStackTrace();
		/* COMMENT
		0 - create()
		1 - this method
		2 - caller of this method
		 */
		return Arrays.copyOfRange(ret, Math.min(2, ret.length), ret.length);
	}

	public static Optional<StackTraceElement> getCurrentStackTraceElement() {
		/* COMMENT
		0 - this method
		1 - caller of this method
		 */
		return Optional.of(getCurrentStackTraceElements())
				.filter(stacktrace -> stacktrace.length > 1)
				.map(stacktrace -> stacktrace[1]);
	}

	public static String getCurrentStackTraceString() { return ExceptionUtils.getStackTrace(ThrowableUtilities.create()); }
}
