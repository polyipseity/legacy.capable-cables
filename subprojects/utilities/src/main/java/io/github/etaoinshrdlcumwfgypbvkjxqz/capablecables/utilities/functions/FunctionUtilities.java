package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

public enum FunctionUtilities {
	;

	private static final Consumer<Object> EMPTY_CONSUMER = t -> {};
	private static final Predicate<Object> ALWAYS_FALSE_PREDICATE = t -> false;
	private static final Predicate<Object> ALWAYS_TRUE_PREDICATE = t -> true;
	private static final BiPredicate<Object, Object> ALWAYS_FALSE_BI_PREDICATE = (t, u) -> false;
	private static final BiPredicate<Object, Object> ALWAYS_TRUE_BI_PREDICATE = (t, u) -> true;

	@SuppressWarnings("unchecked") // COMMENT always safe as it accepts any 'Object'
	public static <T> Consumer<T> getEmptyConsumer() { return (Consumer<T>) EMPTY_CONSUMER; }

	public static <T> Predicate<T> notPredicate(Predicate<T> instance) { return instance.negate(); }

	@SuppressWarnings("unchecked") // COMMENT always safe as it accepts any 'Object'
	public static <T> Predicate<T> alwaysTruePredicate() { return (Predicate<T>) ALWAYS_TRUE_PREDICATE; }

	@SuppressWarnings("unchecked") // COMMENT always safe as it accepts any 'Object'
	public static <T> Predicate<T> alwaysFalsePredicate() { return (Predicate<T>) ALWAYS_FALSE_PREDICATE; }

	@SuppressWarnings("unchecked") // COMMENT always safe as it accepts any 'Object'
	public static <T, U> BiPredicate<T, U> alwaysTrueBiPredicate() { return (BiPredicate<T, U>) ALWAYS_TRUE_BI_PREDICATE; }

	@SuppressWarnings("unchecked") // COMMENT always safe as it accepts any 'Object'
	public static <T, U> BiPredicate<T, U> alwaysFalseBiPredicate() { return (BiPredicate<T, U>) ALWAYS_FALSE_BI_PREDICATE; }
}
