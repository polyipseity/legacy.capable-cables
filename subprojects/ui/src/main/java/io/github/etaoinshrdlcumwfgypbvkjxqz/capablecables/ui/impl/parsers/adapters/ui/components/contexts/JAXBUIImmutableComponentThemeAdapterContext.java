package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.contexts;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.ui.components.contexts.IJAXBUIComponentThemeAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.theming.UILambdaTheme;
import jakarta.xml.bind.JAXBElement;

import javax.xml.namespace.QName;
import java.util.Map;
import java.util.function.BiConsumer;

public final class JAXBUIImmutableComponentThemeAdapterContext
		extends JAXBUIAbstractImmutableComponentBasedAdapterContext
		implements IJAXBUIComponentThemeAdapterContext {
	private final UILambdaTheme.Builder builder;

	public JAXBUIImmutableComponentThemeAdapterContext(Map<? extends String, ? extends Class<?>> aliases,
	                                                   Map<? extends Class<?>, ? extends BiConsumer<? super IJAXBAdapterContext, @Nonnull ?>> objectHandlers,
	                                                   Map<? extends QName, ? extends BiConsumer<? super IJAXBAdapterContext, ? super JAXBElement<?>>> elementHandlers,
	                                                   UILambdaTheme.Builder builder) {
		super(aliases, objectHandlers, elementHandlers);
		this.builder = builder;
	}

	@Override
	public UILambdaTheme.Builder getBuilder() { return builder; }
}
