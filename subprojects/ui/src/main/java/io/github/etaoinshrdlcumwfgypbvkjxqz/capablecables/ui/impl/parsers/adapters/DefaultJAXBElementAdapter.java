package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBElementAdapter;
import jakarta.xml.bind.JAXBElement;

import java.util.function.BiFunction;

public class DefaultJAXBElementAdapter<L, R>
		extends AbstractJAXBAdapter<JAXBElement<L>, R>
		implements IJAXBElementAdapter<L, R> {
	public DefaultJAXBElementAdapter(BiFunction<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super JAXBElement<L>, @Nonnull ? extends R> leftToRightFunction,
	                                 BiFunction<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super R, @Nonnull ? extends JAXBElement<L>> rightToLeftFunction) {
		super(leftToRightFunction, rightToLeftFunction);
	}
}
