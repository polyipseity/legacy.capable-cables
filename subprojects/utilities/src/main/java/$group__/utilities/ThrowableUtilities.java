package $group__.utilities;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_LARGE;
import static $group__.utilities.DynamicUtilities.UNSAFE;
import static $group__.utilities.DynamicUtilities.getCallerClass;
import static $group__.utilities.LoggerUtilities.EnumMessages.FACTORY_SIMPLE_MESSAGE;
import static $group__.utilities.LoggerUtilities.EnumMessages.SUFFIX_WITH_THROWABLE;
import static $group__.utilities.ThrowableUtilities.ThrowableCatcher.*;

public enum ThrowableUtilities {
	;

	public static RuntimeException wrap(Throwable t) { return new RuntimeException("Wrapped throwable", t); }

	public static RuntimeException propagate(Throwable t) throws RuntimeException {
		UNSAFE.throwException(t);
		throw new InternalError();
	}

	public static String getCurrentStackTraceString() { return ExceptionUtils.getStackTrace(create()); }

	public static Throwable create() { return new Throwable("Instantiated throwable"); }

	public enum ThrowableCatcher {
		;

		public static final ThreadLocal<Throwable> CAUGHT_THROWABLE = new ThreadLocal<>();

		public static void catch_(Throwable t, @Nullable Logger logger) {
			log(t, logger);
			CAUGHT_THROWABLE.set(t);
		}

		public static void log(Throwable t, @Nullable Logger logger) {
			if (logger == null) t.printStackTrace();
			else logger.catching(Level.DEBUG, t);
		}

		public static void clear() { CAUGHT_THROWABLE.set(null); }

		@SuppressWarnings("ConstantConditions")
		public static RuntimeException rethrow() throws RuntimeException {
			rethrow(false);
			throw new InternalError();
		}

		public static void rethrow(boolean nullable) {
			if (nullable) retrieve().ifPresent(ThrowableUtilities::propagate);
			else throw propagate(retrieve().orElseThrow(InternalError::new));
		}

		public static Optional<Throwable> retrieve() { return Optional.ofNullable(CAUGHT_THROWABLE.get()); }

		public static void acceptIfCaught(Consumer<? super Throwable> consumer) {
			if (caught()) consumer.accept(retrieve().orElseThrow(InternalError::new));
		}

		public static boolean caught() { return retrieve().isPresent(); }
	}

	public enum Try {
		;

		public static void run(RunnableWithThrowable runnable, @Nullable Logger logger) {
			clear();
			try {
				runnable.run();
			} catch (Throwable t) {
				catch_(t, logger);
			}
		}

		public static <V> Optional<V> call(CallableWithThrowable<V> callable, @Nullable Logger logger) {
			clear();
			try {
				return Optional.ofNullable(callable.call());
			} catch (Throwable t) {
				catch_(t, logger);
				return Optional.empty();
			}
		}

		public static <V> Optional<V> withLogging(Supplier<? extends Optional<V>> delegation, @Nullable Logger logger) {
			Optional<V> r = delegation.get();
			if (logger != null)
				acceptIfCaught(t -> logger.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(FACTORY_SIMPLE_MESSAGE.makeMessage("Failed try"), t)));
			return r;
		}

		@FunctionalInterface
		public interface RunnableWithThrowable {
			void run() throws Throwable;
		}

		@FunctionalInterface
		public interface CallableWithThrowable<V> {
			@Nullable
			V call() throws Throwable;
		}
	}

	public enum BecauseOf {
		;

		public static ClassCastException classCast(Object obj, Class<?> clazz) throws ClassCastException { throw new ClassCastException("'" + obj + "' cannot be casted to '" + clazz.toGenericString() + '\''); }

		public static RuntimeException instantiation() throws RuntimeException { throw propagate(new InstantiationException(getCallerClass().toGenericString())); }

		public static IllegalArgumentException illegalArgument(String message, Object... arguments) throws IllegalArgumentException {
			assert arguments.length % 2 == 0;
			StringBuilder msg = new StringBuilder(INITIAL_CAPACITY_LARGE);
			msg.append(message).append(": ");
			boolean comma = false, value = false;
			for (Object argument : arguments) {
				if (value) {
					msg.append(": ");
				} else {
					if (comma)
						msg.append(", ");
					else
						comma = true;
				}
				msg.append(argument);
				value = !value;
			}
			throw new IllegalArgumentException(msg.toString());
		}
	}
}
