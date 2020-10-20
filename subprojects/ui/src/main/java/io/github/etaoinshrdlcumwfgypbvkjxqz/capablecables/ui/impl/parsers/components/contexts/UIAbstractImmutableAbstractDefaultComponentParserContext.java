package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components.contexts;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;

import java.util.Map;

public abstract class UIAbstractImmutableAbstractDefaultComponentParserContext<H>
		implements IUIAbstractDefaultComponentParserContext<H> {
	private final @Immutable Map<String, Class<?>> aliases;
	private final @Immutable Map<Class<?>, H> handlers;

	public UIAbstractImmutableAbstractDefaultComponentParserContext(Map<? extends String, ? extends Class<?>> aliases,
	                                                                Map<? extends Class<?>, ? extends H> handlers) {
		this.aliases = ImmutableMap.copyOf(aliases);
		this.handlers = ImmutableMap.copyOf(handlers);
	}

	@Override
	public @Immutable Map<Class<?>, H> getHandlersView() { return handlers; }

	@Override
	public @Immutable Map<String, Class<?>> getAliasesView() { return aliases; }
}
