package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.ui.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBObjectAdapter;
import jakarta.xml.bind.JAXBElement;

import javax.xml.namespace.QName;
import java.util.function.BiConsumer;

public interface IJAXBUIComponentBasedAdapter<L, R>
		extends IJAXBObjectAdapter<L, R> {
	<T> void addObjectHandler(Class<T> key, BiConsumer<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super T> handler);

	void addElementHandler(QName key, BiConsumer<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super JAXBElement<?>> handler);
}
