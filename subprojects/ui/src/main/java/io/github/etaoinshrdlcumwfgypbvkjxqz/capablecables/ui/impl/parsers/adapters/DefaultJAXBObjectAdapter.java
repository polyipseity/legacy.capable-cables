package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBObjectAdapter;

import java.util.function.BiFunction;

public class DefaultJAXBObjectAdapter<L, R>
		extends AbstractJAXBAdapter<L, R>
		implements IJAXBObjectAdapter<L, R> {
	public DefaultJAXBObjectAdapter(BiFunction<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super L, @Nonnull ? extends R> leftToRightFunction,
	                                BiFunction<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super R, @Nonnull ? extends L> rightToLeftFunction) {
		super(leftToRightFunction, rightToLeftFunction);
	}
}
