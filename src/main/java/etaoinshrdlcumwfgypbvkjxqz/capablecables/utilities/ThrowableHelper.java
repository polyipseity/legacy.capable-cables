package etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Arrays;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.GrammarHelper.appendSuffixIfPlural;

@Immutable
public enum ThrowableHelper {
    ;
    public static StackTraceElement[] getStackTrace() { return new Throwable().getStackTrace(); }
    public static RuntimeException wrapUnhandledThrowable(Throwable t) { throw wrapUnhandledThrowable(t, null); }
    public static RuntimeException wrapUnhandledThrowable(Throwable t, @Nullable String msg) { throw new RuntimeException(msg, t); }

    public static String rejectObjectsString(String msg, Object... objects) { return String.format("Reject %s: %s", msg, Arrays.toString(objects)); }
    public static RuntimeException rejectAttempt(String msg) {
        StackTraceElement[] st = getStackTrace();
        //noinspection SpellCheckingInspection
        throw wrapUnhandledThrowable(new InstantiationException(String.format("%sttempted %s%s",
                st.length > 4 ? String.format("'%s' a", st[4]) : "A",
                msg,
                st.length > 3 ? String.format(" of '%s'", st[3]) : "")));
    }

    public static RuntimeException rejectInstantiation() { throw rejectAttempt("illegal instantiation"); }
    public static RuntimeException rejectArguments(Object... args) { throw new IllegalArgumentException(rejectObjectsString(String.format("illegal argument%s", appendSuffixIfPlural(args.length, "s")), args)); }
    /* private static final Set<String> RAN_ONCE = Sets.newConcurrentHashSet();
    public static void requireRunOnceOnly() { if (!RAN_ONCE.add(getStackTrace()[2].toString())) throw rejectAttempt("illegal second invocation"); } */
}
