package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBObjectAdapter;

import java.util.function.BiFunction;

public class DefaultJAXBObjectAdapter<L, R>
		extends AbstractJAXBAdapter<L, R>
		implements IJAXBObjectAdapter<L, R> {
	public DefaultJAXBObjectAdapter(BiFunction<? super IJAXBAdapterContext, ? super L, ? extends R> leftToRightFunction,
	                                BiFunction<? super IJAXBAdapterContext, ? super R, ? extends L> rightToLeftFunction) {
		super(leftToRightFunction, rightToLeftFunction);
	}
}
