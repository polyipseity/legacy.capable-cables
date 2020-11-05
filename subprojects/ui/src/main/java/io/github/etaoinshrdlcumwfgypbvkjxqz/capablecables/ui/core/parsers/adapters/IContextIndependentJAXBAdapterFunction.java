package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters;

import javax.annotation.Nullable;
import java.util.function.BiFunction;

@FunctionalInterface
public interface IContextIndependentJAXBAdapterFunction<T, R>
		extends BiFunction<IJAXBAdapterContext, T, R> {
	static <T, R> IContextIndependentJAXBAdapterFunction<T, R> of(IContextIndependentJAXBAdapterFunction<T, R> lambda) {
		return lambda;
	}

	@Override
	@Nullable
	default R apply(IJAXBAdapterContext context, T t) {
		return apply0(t);
	}

	@Nullable
	R apply0(T t);
}
