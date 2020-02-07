package $group__.$modId__.utilities.helpers.specific;

import $group__.$modId__.traits.basic.IThrowableCatcher;
import $group__.$modId__.traits.extensions.ICallableThrowable;
import $group__.$modId__.traits.extensions.IRunnableThrowable;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

import static $group__.$modId__.utilities.helpers.Dynamics.BRIDGE;
import static $group__.$modId__.utilities.helpers.Grammar.appendSuffixIfPlural;
import static $group__.$modId__.utilities.helpers.specific.Loggers.EnumMessages.FACTORY_SIMPLE_MESSAGE;
import static $group__.$modId__.utilities.helpers.specific.Loggers.EnumMessages.SUFFIX_WITH_THROWABLE;

@SuppressWarnings("SpellCheckingInspection")
public enum Throwables implements IThrowableCatcher {
	/* SECTION enums */
	INSTANCE;


	/* SECTION static variables */

	public static final ThreadLocal<Throwable> CAUGHT_THROWABLE = new ThreadLocal<>();


	/* SECTION static methods */

	public static RuntimeException throwThrowable(Throwable t) {
		BRIDGE.throwException(t);
		throw unexpected();
	}

	public static Throwable newThrowable() { return new Throwable("Instantiated throwable"); }

	public static RuntimeException wrapThrowable(Throwable t) { return new RuntimeException("Wrapped throwable", t); }


	public static String getCurrentStackTraceString() { return ExceptionUtils.getStackTrace(newThrowable()); }


	public static <T> Optional<T> tryCall(ICallableThrowable<T> callable, Logger logger) {
		clearCaughtThrowableStatic();
		try {
			return Optional.ofNullable(callable.callT());
		} catch (Throwable t) {
			setCaughtThrowableStatic(t, logger);
			return Optional.empty();
		}
	}

	public static void tryRun(IRunnableThrowable runnable, Logger logger) {
		clearCaughtThrowableStatic();
		try {
			runnable.runT();
		} catch (Throwable t) {
			setCaughtThrowableStatic(t, logger);
		}
	}

	public static void consumeIfCaughtThrowable(Consumer<? super Throwable> consumer) { if (caughtThrowableStatic()) consumer.accept(getCaughtThrowableUnboxedNonnullStatic()); }

	public static <T> Optional<T> tryCallWithLogging(ICallableThrowable<T> callable, Logger logger) {
		Optional<T> r = tryCall(callable, logger);
		consumeIfCaughtThrowable(t -> logger.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(FACTORY_SIMPLE_MESSAGE.makeMessage("Failed callable"), t)));
		return r;
	}


	public static <T extends Throwable> T throw_(T t) throws T { throw t; }

	public static <T extends Throwable> T new_(T t) { return t; }


	public static void consumeCaught(Throwable t, Logger logger) { logger.debug(() -> SUFFIX_WITH_THROWABLE.makeMessage(FACTORY_SIMPLE_MESSAGE.makeMessage("Consumed throwable"), t)); }

	public static RuntimeException rejectInstantiation() throws RuntimeException { throw rejectInstantiation(null); }

	public static RuntimeException rejectInstantiation(@Nullable String msg) throws RuntimeException { throw throwThrowable(new InstantiationException(rejectAttemptString("illegal instantiation" + (msg == null ? StringsExtension.STRING_EMPTY : ": " + msg)))); }

	public static String rejectAttemptString(String msg) {
		StackTraceElement[] st = getStackTrace();
		return rejectAttemptString(msg, st.length > 4 ? st[4] : null, st.length > 3 ? st[3] : null);
	}

	public static InternalError unexpected() throws InternalError { throw unexpected(null, null); }

	public static StackTraceElement[] getStackTrace() { return newThrowable().getStackTrace(); }

	public static String rejectAttemptString(String msg, @Nullable Object from, @Nullable Object to) { return (from == null ? "A" : "'" + from + "' a") + "ttempted " + msg + (to == null ? StringsExtension.STRING_EMPTY : " of '" + to + '\''); }

	public static InternalError unexpected(@Nullable String msg, @Nullable Throwable t) throws InternalError {
		throw throw_(new InternalError(msg, t));
	}

	public static String rejectAttemptString(String msg, StackTraceElement[] st) { return rejectAttemptString(msg, st.length > 3 ? st[3] : null, st.length > 2 ? st[2] : null); }

	public static UnsupportedOperationException rejectUnsupportedOperation() throws UnsupportedOperationException { throw rejectUnsupportedOperation(null); }

	public static UnsupportedOperationException rejectUnsupportedOperation(@Nullable Throwable cause) throws UnsupportedOperationException { throw throw_(new UnsupportedOperationException(rejectAttemptString("unsupported operation"), cause)); }

	public static IllegalArgumentException rejectArguments(Object... args) throws IllegalArgumentException { throw throw_(new IllegalArgumentException(rejectObjectsString("illegal argument" + appendSuffixIfPlural(args.length, "s"), args))); }

	public static String rejectObjectsString(String msg, Object... objects) { return "Reject " + msg + ": " + Arrays.toString(objects); }

	public static IllegalArgumentException rejectIndexOutOfBounds(Number index, Number bound) throws IllegalArgumentException {
		throw rejectArguments(new IndexOutOfBoundsException("index: " + index + ", bound: " + bound), index);
	}

	public static IllegalArgumentException rejectArguments(Throwable cause, Object... args) throws IllegalArgumentException { throw throw_(new IllegalArgumentException(rejectObjectsString("illegal argument" + appendSuffixIfPlural(args.length, "s"), args), cause)); }

	public static InternalError unexpected(Throwable t) throws InternalError { throw unexpected(null, t); }

	public static InternalError unexpected(String msg) throws InternalError { throw unexpected(msg, null); }

	public static InterruptedException interrupt() throws InterruptedException { throw interrupt(null); }

	public static InterruptedException interrupt(@Nullable String msg) throws InterruptedException { throw throw_(new InterruptedException(msg)); }

	public static ClassCastException cast(Object o, Class<?> type) throws ClassCastException { throw throw_(new ClassCastException(o.getClass().getName() + " cannot be cast to " + type.getName())); }

	public static NullPointerException null_(String s) { throw throw_(new NullPointerException(s)); }


	public static Optional<Throwable> getCaughtThrowableStatic() { return INSTANCE.getCaughtThrowable(); }

	public static void setCaughtThrowableStatic(Throwable t, Logger logger) { INSTANCE.setCaughtThrowable(t, logger); }

	@Nullable
	public static Throwable getCaughtThrowableUnboxedStatic() { return INSTANCE.getCaughtThrowableUnboxed(); }

	public static Throwable getCaughtThrowableUnboxedNonnullStatic() { return INSTANCE.getCaughtThrowableUnboxedNonnull(); }

	public static boolean caughtThrowableStatic() { return INSTANCE.caughtThrowable(); }

	public static void clearCaughtThrowableStatic() { INSTANCE.clearCaughtThrowable(); }

	public static void rethrowCaughtThrowableStatic(boolean nullable) throws RuntimeException { INSTANCE.rethrowCaughtThrowable(nullable); }

	public static RuntimeException rethrowCaughtThrowableStatic() throws RuntimeException { throw INSTANCE.rethrowCaughtThrowable(); }


	/* SECTION methods */

	@Override
	public Optional<Throwable> getCaughtThrowable() { return Optional.ofNullable(CAUGHT_THROWABLE.get()); }

	public void setCaughtThrowable(Throwable t, Logger logger) {
		consumeCaught(t, logger);
		CAUGHT_THROWABLE.set(t);
	}

	@Override
	public void clearCaughtThrowable() { CAUGHT_THROWABLE.set(null); }
}
