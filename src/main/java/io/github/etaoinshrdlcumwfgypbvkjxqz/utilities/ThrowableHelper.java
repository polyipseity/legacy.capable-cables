package io.github.etaoinshrdlcumwfgypbvkjxqz.utilities;

import javax.annotation.Nullable;

public enum ThrowableHelper {
    ;
    public static StackTraceElement[] getStackTrace() { return new Throwable().getStackTrace(); }
    public static RuntimeException wrapUnhandledThrowable(final Throwable t) { throw wrapUnhandledThrowable(t, null); }
    public static RuntimeException wrapUnhandledThrowable(final Throwable t, @Nullable final String msg) { throw new RuntimeException(msg, t); }

    public static RuntimeException rejectAttempt(String msg) {
        StackTraceElement[] st = getStackTrace();
        //noinspection SpellCheckingInspection
        throw wrapUnhandledThrowable(new InstantiationException(String.format("%sttempted %s%s",
                st.length > 4 ? String.format("'%s' a", st[4]) : "A",
                msg,
                st.length > 3 ? String.format(" of '%s'", st[3]) : "")));
    }
    public static RuntimeException rejectInstantiation() { throw rejectAttempt("illegal instantiation"); }
    /* private static final Set<String> RAN_ONCE = new HashSet<>();
    public static synchronized void requireRunOnceOnly() { if (!RAN_ONCE.add(getStackTrace()[2].toString())) throw rejectAttempt("illegal second invocation"); } */
}
