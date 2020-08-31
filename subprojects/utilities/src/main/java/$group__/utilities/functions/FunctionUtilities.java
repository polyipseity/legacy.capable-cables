package $group__.utilities.functions;

import java.util.function.Consumer;

public enum FunctionUtilities {
	;

	private static final Consumer<Object> EMPTY_CONSUMER = t -> {};

	@SuppressWarnings("unchecked")
	public static <T> Consumer<T> getEmptyConsumer() {
		return (Consumer<T>) EMPTY_CONSUMER; // COMMENT always safe as it accepts any 'Object'
	}
}
