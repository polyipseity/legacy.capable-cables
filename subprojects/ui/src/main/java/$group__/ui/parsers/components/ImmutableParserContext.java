package $group__.ui.parsers.components;

import $group__.ui.core.parsers.components.IParserContext;
import $group__.ui.core.parsers.components.IUIComponentParser;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.functions.IConsumer3;
import com.google.common.collect.ImmutableMap;

import javax.annotation.concurrent.Immutable;
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
	public Map<String, Class<?>> getAliases() { return aliases; }

	@Override
	public Map<IUIComponentParser.EnumHandlerType, ? extends Map<Class<?>, IConsumer3<? super IParserContext, ?, ?, ?>>> getHandlers() { return handlers; }
}
