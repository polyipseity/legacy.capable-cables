package io.github.etaoinshrdlcumwfgypbvkjxqz.utilities;

public class ThrowHelper {
    private ThrowHelper() { throw rejectInstantiation(); }

    public static StackTraceElement[] getStackTrace() { return new Throwable().getStackTrace(); }
    public static RuntimeException wrapUnhandledThrowable(final Throwable t) { throw new RuntimeException(t); }

    public static RuntimeException rejectInstantiation() {
        StackTraceElement[] st = getStackTrace();
        throw wrapUnhandledThrowable(new InstantiationException(String.format("%sttempted illegal instantiation%s",
                st.length > 3 ? String.format("%s a", st[3]) : "A",
                st.length > 2 ? String.format(" of %s", st[2]) : "")));
    }
}
