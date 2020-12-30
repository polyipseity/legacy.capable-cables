package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.ui.components.contexts;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.IJAXBAdapterContext;
import jakarta.xml.bind.JAXBElement;

import javax.xml.namespace.QName;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public interface IJAXBUIComponentBasedAdapterContext {
	@SuppressWarnings("unchecked")
	static <T> Optional<BiConsumer<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super T>> findHandler(IJAXBUIComponentBasedAdapterContext instance,
	                                                                                                      T any) {
		if (any instanceof JAXBElement)
			return Optional.ofNullable(
					(BiConsumer<? super IJAXBAdapterContext, ? super T>) instance.getElementHandlersView().get(((JAXBElement<?>) any).getName()) // COMMENT safe by contract
			);
		else
			return Optional.ofNullable(
					(BiConsumer<? super IJAXBAdapterContext, ? super T>) instance.getObjectHandlersView().get(any.getClass()) // COMMENT safe by contract
			);
	}

	@Immutable Map<QName, BiConsumer<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super JAXBElement<?>>> getElementHandlersView();

	@Immutable Map<Class<?>, BiConsumer<@Nonnull ? super IJAXBAdapterContext, @Nonnull ?>> getObjectHandlersView();

	@Immutable Map<String, Class<?>> getAliasesView();
}
