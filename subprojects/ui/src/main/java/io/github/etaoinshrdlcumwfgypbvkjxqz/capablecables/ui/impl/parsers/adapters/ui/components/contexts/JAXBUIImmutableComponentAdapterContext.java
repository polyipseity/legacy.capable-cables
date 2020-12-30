package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.contexts;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.ui.components.contexts.IJAXBUIComponentAdapterContext;
import jakarta.xml.bind.JAXBElement;

import javax.xml.namespace.QName;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public final class JAXBUIImmutableComponentAdapterContext
		extends JAXBUIAbstractImmutableComponentBasedAdapterContext
		implements IJAXBUIComponentAdapterContext {
	private final @Nullable IUIViewComponent<?, ?> view;
	@Nullable
	private final Object container;

	public JAXBUIImmutableComponentAdapterContext(Map<? extends String, ? extends Class<?>> aliases,
	                                              Map<? extends Class<?>, ? extends BiConsumer<? super IJAXBAdapterContext, @Nonnull ?>> objectHandlers,
	                                              Map<? extends QName, ? extends BiConsumer<? super IJAXBAdapterContext, ? super JAXBElement<?>>> elementHandlers,
	                                              @Nullable IUIViewComponent<?, ?> view,
	                                              @Nullable Object container) {
		super(aliases, objectHandlers, elementHandlers);
		this.view = view;
		this.container = container;
	}

	@Override
	public Optional<?> getContainer() { return Optional.ofNullable(container); }

	@Override
	public Optional<? extends IUIViewComponent<?, ?>> getView() { return Optional.ofNullable(view); }
}
