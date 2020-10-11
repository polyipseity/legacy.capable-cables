package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions;

import java.util.function.*;

public enum FunctionUtilities {
	;

	private static final Consumer<?> EMPTY_CONSUMER = t -> {};
	private static final Predicate<?> ALWAYS_FALSE_PREDICATE = t -> false;
	private static final Predicate<?> ALWAYS_TRUE_PREDICATE = t -> true;
	private static final BiPredicate<?, ?> ALWAYS_FALSE_BI_PREDICATE = (t, u) -> false;
	private static final BiPredicate<?, ?> ALWAYS_TRUE_BI_PREDICATE = (t, u) -> true;
	private static final Predicate<Boolean> PREDICATE_IDENTITY = t -> t;

	@SuppressWarnings("unchecked") // COMMENT always safe as it accepts any 'Object'
	public static <T> Consumer<T> getEmptyConsumer() { return (Consumer<T>) EMPTY_CONSUMER; }

	public static <T> Predicate<T> notPredicate(Predicate<T> function) { return function.negate(); }

	@SuppressWarnings("unchecked") // COMMENT always safe as it accepts any 'Object'
	public static <T> Predicate<T> alwaysTruePredicate() { return (Predicate<T>) ALWAYS_TRUE_PREDICATE; }

	@SuppressWarnings("unchecked") // COMMENT always safe as it accepts any 'Object'
	public static <T> Predicate<T> alwaysFalsePredicate() { return (Predicate<T>) ALWAYS_FALSE_PREDICATE; }

	@SuppressWarnings("unchecked") // COMMENT always safe as it accepts any 'Object'
	public static <T, U> BiPredicate<T, U> alwaysTrueBiPredicate() { return (BiPredicate<T, U>) ALWAYS_TRUE_BI_PREDICATE; }

	@SuppressWarnings("unchecked") // COMMENT always safe as it accepts any 'Object'
	public static <T, U> BiPredicate<T, U> alwaysFalseBiPredicate() { return (BiPredicate<T, U>) ALWAYS_FALSE_BI_PREDICATE; }

	public static Supplier<Void> asVoidSupplier(Runnable function) {
		return () -> {
			function.run();
			return null;
		};
	}

	public static <T> Function<T, Void> asVoidFunction(Consumer<T> function) {
		return t -> {
			function.accept(t);
			return null;
		};
	}

	public static Predicate<Boolean> getPredicateIdentity() { return PREDICATE_IDENTITY; }
}
