package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.IParserContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.IUIComponentParser;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IConsumer3;

import java.util.Map;

@Immutable
public class ImmutableParserContext
		implements IParserContext {
	protected final IUIComponentParser.EnumHandlerType handlingType;
	protected final Map<String, Class<?>> aliases;
	protected final Map<IUIComponentParser.EnumHandlerType, ? extends Map<Class<?>, IConsumer3<? super IParserContext, ?, ?, ?>>> handlers;

	@SuppressWarnings("unchecked")
	public ImmutableParserContext(IUIComponentParser.EnumHandlerType handlingType,
	                              Map<String, Class<?>> aliases,
	                              Map<IUIComponentParser.EnumHandlerType, ? extends Map<Class<?>, IConsumer3<? super IParserContext, ?, ?, ?>>> handlers) {
		this.handlingType = handlingType;
		this.aliases = ImmutableMap.copyOf(aliases);
		this.handlers = (Map<IUIComponentParser.EnumHandlerType, ? extends Map<Class<?>, IConsumer3<? super IParserContext, ?, ?, ?>>>) MapUtilities.immutableDeepCopyOf(handlers); // COMMENT should be safe
	}

	@Override
	public IUIComponentParser.EnumHandlerType getHandlingType() { return handlingType; }

	@Override
	public @Immutable Map<String, Class<?>> getAliasesView() { return aliases; }

	@Override
	public @Immutable Map<IUIComponentParser.EnumHandlerType, ? extends Map<Class<?>, IConsumer3<? super IParserContext, ?, ?, ?>>> getHandlersView() { return handlers; }
}
