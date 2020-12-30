package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.def;

import java.util.function.Supplier;

@FunctionalInterface
public interface ICompatibilitySupplier<T>
		extends Supplier<T>, com.google.common.base.Supplier<T>, org.apache.logging.log4j.util.Supplier<T> {
	static <T> ICompatibilitySupplier<T> of(ICompatibilitySupplier<T> lambda) {
		return lambda;
	}
}
