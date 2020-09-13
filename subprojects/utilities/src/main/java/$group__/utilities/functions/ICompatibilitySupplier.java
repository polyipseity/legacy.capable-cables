package $group__.utilities.functions;

import java.util.function.Supplier;

@FunctionalInterface
public interface ICompatibilitySupplier<T>
		extends Supplier<T>, com.google.common.base.Supplier<T>, org.apache.logging.log4j.util.Supplier<T> {
	static <T> ICompatibilitySupplier<T> ofLambda(ICompatibilitySupplier<T> lambda) { return lambda; }
}
