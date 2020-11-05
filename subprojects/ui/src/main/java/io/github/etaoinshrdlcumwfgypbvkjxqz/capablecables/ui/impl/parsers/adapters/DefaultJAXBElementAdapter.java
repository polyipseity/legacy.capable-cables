package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBElementAdapter;
import jakarta.xml.bind.JAXBElement;

import java.util.function.BiFunction;

public class DefaultJAXBElementAdapter<L, R>
		extends AbstractJAXBAdapter<JAXBElement<L>, R>
		implements IJAXBElementAdapter<L, R> {
	public DefaultJAXBElementAdapter(BiFunction<? super IJAXBAdapterContext, ? super JAXBElement<L>, ? extends R> leftToRightFunction,
	                                 BiFunction<? super IJAXBAdapterContext, ? super R, ? extends JAXBElement<L>> rightToLeftFunction) {
		super(leftToRightFunction, rightToLeftFunction);
	}
}
