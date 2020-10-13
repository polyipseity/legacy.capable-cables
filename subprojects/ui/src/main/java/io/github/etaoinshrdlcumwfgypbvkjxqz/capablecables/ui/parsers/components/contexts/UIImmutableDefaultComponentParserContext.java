package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.contexts;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.NonnullBiConsumer;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

public final class UIImmutableDefaultComponentParserContext
		extends UIAbstractImmutableAbstractDefaultComponentParserContext<NonnullBiConsumer<? super IUIDefaultComponentParserContext, ?>>
		implements IUIDefaultComponentParserContext {
	private final @Nullable
	IUIViewComponent<?, ?> view;
	@Nullable
	private final Object container;

	public UIImmutableDefaultComponentParserContext(Map<? extends String, ? extends Class<?>> aliases,
	                                                Map<? extends Class<?>, ? extends NonnullBiConsumer<? super IUIDefaultComponentParserContext, ?>> handlers,
	                                                @Nullable IUIViewComponent<?, ?> view,
	                                                @Nullable Object container) {
		super(aliases, handlers);
		this.view = view;
		this.container = container;
	}

	@Override
	public Optional<?> getContainer() { return Optional.ofNullable(container); }

	@Override
	public Optional<? extends IUIViewComponent<?, ?>> getView() { return Optional.ofNullable(view); }
}
