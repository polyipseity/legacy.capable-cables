package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.contexts;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.ui.components.contexts.IUIDefaultComponentParserContext;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public final class UIImmutableDefaultComponentParserContext
		extends UIAbstractImmutableAbstractDefaultComponentParserContext<BiConsumer<@Nonnull ? super IUIDefaultComponentParserContext, @Nonnull ?>>
		implements IUIDefaultComponentParserContext {
	private final @Nullable
	IUIViewComponent<?, ?> view;
	@Nullable
	private final Object container;

	public UIImmutableDefaultComponentParserContext(Map<? extends String, ? extends Class<?>> aliases,
	                                                Map<? extends Class<?>, ? extends BiConsumer<@Nonnull ? super IUIDefaultComponentParserContext, @Nonnull ?>> handlers,
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
