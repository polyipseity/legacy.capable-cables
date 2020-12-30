package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.contexts;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.ui.components.contexts.IJAXBUIComponentBasedAdapterContext;
import jakarta.xml.bind.JAXBElement;

import javax.xml.namespace.QName;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class JAXBUIAbstractImmutableComponentBasedAdapterContext
		implements IJAXBUIComponentBasedAdapterContext {
	private final Map<String, Class<?>> aliases;
	private final Map<Class<?>, BiConsumer<? super IJAXBAdapterContext, @Nonnull ?>> objectHandlers;
	private final Map<QName, BiConsumer<? super IJAXBAdapterContext, ? super JAXBElement<?>>> elementHandlers;

	public JAXBUIAbstractImmutableComponentBasedAdapterContext(Map<? extends String, ? extends Class<?>> aliases,
	                                                           Map<? extends Class<?>, ? extends BiConsumer<@Nonnull ? super IJAXBAdapterContext, @Nonnull ?>> objectHandlers,
	                                                           Map<? extends QName, ? extends BiConsumer<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super JAXBElement<?>>> elementHandlers) {
		this.aliases = ImmutableMap.copyOf(aliases);
		this.objectHandlers = ImmutableMap.copyOf(objectHandlers);
		this.elementHandlers = ImmutableMap.copyOf(elementHandlers);
	}

	@Override
	public @Immutable Map<QName, BiConsumer<? super IJAXBAdapterContext, ? super JAXBElement<?>>> getElementHandlersView() {
		return ImmutableMap.copyOf(getElementHandlers());
	}

	@Override
	public @Immutable Map<Class<?>, BiConsumer<? super IJAXBAdapterContext, @Nonnull ?>> getObjectHandlersView() {
		return ImmutableMap.copyOf(getObjectHandlers());
	}

	@Override
	public @Immutable Map<String, Class<?>> getAliasesView() { return ImmutableMap.copyOf(getAliases()); }

	protected Map<String, Class<?>> getAliases() {
		return aliases;
	}

	protected Map<Class<?>, BiConsumer<@Nonnull ? super IJAXBAdapterContext, @Nonnull ?>> getObjectHandlers() {
		return objectHandlers;
	}

	protected Map<QName, BiConsumer<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super JAXBElement<?>>> getElementHandlers() {
		return elementHandlers;
	}
}
