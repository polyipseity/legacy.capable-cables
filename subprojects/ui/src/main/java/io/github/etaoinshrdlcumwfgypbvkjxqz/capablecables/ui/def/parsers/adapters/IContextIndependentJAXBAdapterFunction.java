package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

import java.util.function.BiFunction;

@FunctionalInterface
public interface IContextIndependentJAXBAdapterFunction<T, R>
		extends BiFunction<@Nullable IJAXBAdapterContext, T, R> {
	static <T, R> IContextIndependentJAXBAdapterFunction<T, R> of(IContextIndependentJAXBAdapterFunction<T, R> lambda) {
		return lambda;
	}

	@Override
	default R apply(@Nullable IJAXBAdapterContext context, T t) {
		return apply0(t);
	}

	R apply0(T t);
}
