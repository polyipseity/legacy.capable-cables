package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.AlwaysNull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;

import java.util.function.*;

public enum FunctionUtilities {
	;

	private static final Consumer<?> EMPTY_CONSUMER = t -> {};
	private static final BiConsumer<?, ?> EMPTY_BI_CONSUMER = (t, u) -> {};
	private static final Runnable EMPTY_RUNNABLE = () -> {};
	private static final Predicate<?> ALWAYS_FALSE_PREDICATE = t -> false;
	private static final Predicate<?> ALWAYS_TRUE_PREDICATE = t -> true;
	private static final BiPredicate<?, ?> ALWAYS_FALSE_BI_PREDICATE = (t, u) -> false;
	private static final BiPredicate<?, ?> ALWAYS_TRUE_BI_PREDICATE = (t, u) -> true;
	private static final BooleanSupplier ALWAYS_FALSE_BOOLEAN_SUPPLIER = () -> false;
	private static final BooleanSupplier ALWAYS_TRUE_BOOLEAN_SUPPLIER = () -> true;

	@SuppressWarnings("unchecked") // COMMENT always safe as it accepts any 'Object'
	public static <T> Consumer<T> getEmptyConsumer() { return (Consumer<T>) EMPTY_CONSUMER; }

	@SuppressWarnings("unchecked") // COMMENT always safe as it accepts any 'Object'
	public static <T, U> BiConsumer<T, U> getEmptyBiConsumer() { return (BiConsumer<T, U>) EMPTY_BI_CONSUMER; }

	public static Runnable getEmptyRunnable() {
		return EMPTY_RUNNABLE;
	}

	public static <T> Predicate<T> notPredicate(Predicate<T> function) { return function.negate(); }

	@SuppressWarnings("unchecked") // COMMENT always safe as it accepts any 'Object'
	public static <T> Predicate<T> getAlwaysTruePredicate() { return (Predicate<T>) ALWAYS_TRUE_PREDICATE; }

	@SuppressWarnings("unchecked") // COMMENT always safe as it accepts any 'Object'
	public static <T> Predicate<T> getAlwaysFalsePredicate() { return (Predicate<T>) ALWAYS_FALSE_PREDICATE; }

	@SuppressWarnings("unchecked") // COMMENT always safe as it accepts any 'Object'
	public static <T, U> BiPredicate<T, U> getAlwaysTrueBiPredicate() { return (BiPredicate<T, U>) ALWAYS_TRUE_BI_PREDICATE; }

	@SuppressWarnings("unchecked") // COMMENT always safe as it accepts any 'Object'
	public static <T, U> BiPredicate<T, U> getAlwaysFalseBiPredicate() { return (BiPredicate<T, U>) ALWAYS_FALSE_BI_PREDICATE; }

	public static Supplier<@AlwaysNull Void> asVoidSupplier(Runnable function) {
		return () -> {
			function.run();
			return null;
		};
	}

	public static <T> Function<T, @AlwaysNull Void> asVoidFunction(Consumer<T> function) {
		return t -> {
			function.accept(t);
			return null;
		};
	}

	public static <T> T accept(T t, Consumer<@Nonnull ? super T> action) {
		action.accept(t);
		return t;
	}

	public static <T, R> R apply(T t, Function<@Nonnull ? super T, ? extends R> action) {
		return action.apply(t);
	}

	public static BooleanSupplier getAlwaysTrueBooleanSupplier() {
		return ALWAYS_TRUE_BOOLEAN_SUPPLIER;
	}

	public static BooleanSupplier getAlwaysFalseBooleanSupplier() {
		return ALWAYS_FALSE_BOOLEAN_SUPPLIER;
	}
}
