package etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import com.google.common.collect.Sets;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Set;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.GrammarHelper.appendSuffixIfPlural;

public enum ThrowableHelper {
    ;
    public static StackTraceElement[] getStackTrace() { return new Throwable().getStackTrace(); }
    public static RuntimeException wrapUnhandledThrowable(Throwable t) { throw wrapUnhandledThrowable(t, null); }
    public static RuntimeException wrapUnhandledThrowable(Throwable t, @Nullable String msg) { throw new RuntimeException(msg, t); }

    public static String rejectAttemptString(String msg, @Nullable Object from, @Nullable Object to) {
        //noinspection SpellCheckingInspection
        return String.format("%sttempted %s%s",
                from == null ? "A" : String.format("'%s' a", from),
                msg,
                to == null ? "" : String.format(" of '%s'", to));
    }
    public static String rejectAttemptString(String msg, StackTraceElement[] st) { return rejectAttemptString(msg, st.length > 3 ? st[3] : null,  st.length > 2 ? st[2] : null); }
    public static String rejectAttemptString(String msg) {
        StackTraceElement[] st = getStackTrace();
        return rejectAttemptString(msg, st.length > 4 ? st[4] : null, st.length > 3 ? st[3] : null);
    }

    public static String rejectObjectsString(String msg, Object... objects) { return String.format("Reject %s: %s", msg, Arrays.toString(objects)); }


    public static RuntimeException rejectInstantiation() { throw wrapUnhandledThrowable(new InstantiationException(rejectAttemptString("illegal instantiation"))); }
    private static final Set<String> RAN_ONCE = Sets.newConcurrentHashSet();
    public static void requireRunOnceOnly() {
        StackTraceElement[] st = getStackTrace();
        if (st.length <= 2 || RAN_ONCE.add(st[2].toString())) return;
        throw new IllegalStateException(rejectAttemptString("illegal second invocation", st));
    }
    public static UnsupportedOperationException rejectUnsupportedOperation() { throw new UnsupportedOperationException(rejectAttemptString("unsupported operation")); }

    public static IllegalArgumentException rejectArguments(Object... args) { throw new IllegalArgumentException(rejectObjectsString(String.format("illegal argument%s", appendSuffixIfPlural(args.length, "s")), args)); }
    public static IllegalArgumentException rejectArguments(Throwable cause, Object... args) { throw new IllegalArgumentException(rejectObjectsString(String.format("illegal argument%s", appendSuffixIfPlural(args.length, "s")), args), cause); }
}
