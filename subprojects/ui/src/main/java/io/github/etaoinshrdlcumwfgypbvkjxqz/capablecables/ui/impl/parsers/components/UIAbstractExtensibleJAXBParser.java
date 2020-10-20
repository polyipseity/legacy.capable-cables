package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.UIAbstractExtensibleParser;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.NonnullBiConsumer;
import jakarta.xml.bind.JAXBElement;

public abstract class UIAbstractExtensibleJAXBParser<T, R, P, C>
		extends UIAbstractExtensibleParser<T, R, P, NonnullBiConsumer<? super C, ?>, Class<?>> {
	public <O> void addObjectHandler(Class<O> discriminator, NonnullBiConsumer<? super C, ? extends O> handler) { addHandler(discriminator, handler); }

	@Override
	@Deprecated
	public void addHandler(Class<?> discriminator, NonnullBiConsumer<? super C, ?> handler) { super.addHandler(discriminator, handler); }

	public <O> void addElementHandler(Class<O> discriminator, NonnullBiConsumer<? super C, ? extends JAXBElement<? super O>> handler) { addHandler(discriminator, handler); }
}
