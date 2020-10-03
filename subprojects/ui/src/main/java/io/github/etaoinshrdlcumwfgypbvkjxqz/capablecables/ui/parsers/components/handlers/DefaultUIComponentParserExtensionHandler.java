package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.handlers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.AnyContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.Extension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.IConstructorType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.IParserContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIExtensionConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.DefaultUIComponentParser;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IConsumer3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingConsumer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;

import java.lang.invoke.MethodHandle;
import java.util.Map;

public class DefaultUIComponentParserExtensionHandler
		implements IConsumer3<IParserContext, Object, Extension, Throwable> {
	@Override
	@SuppressWarnings("deprecation")
	public void accept(IParserContext context, Object container, Extension object)
			throws Throwable {
		if (!(container instanceof IExtensionContainer))
			return;
		Class<?> oc = AssertionUtilities.assertNonnull(context.getAliases().get(object.getClazz()));
		UIExtensionConstructor.EnumConstructorType ct = IConstructorType.StaticHolder.getConstructorType(oc, UIExtensionConstructor.class, UIExtensionConstructor::type);
		MethodHandle mh = InvokeUtilities.IMPL_LOOKUP.findConstructor(oc, ct.getMethodType());
		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = DefaultUIComponentParser.createMappings(object.getProperty());
		IUIExtension<?, ?> ret;
		switch (ct) {
			case MAPPINGS__CONTAINER_CLASS:
				ret = (IUIExtension<?, ?>) mh.invoke(mappings, container.getClass());
				break;
			case MAPPINGS:
				ret = (IUIExtension<?, ?>) mh.invoke(mappings);
				break;
			case CONTAINER_CLASS:
				ret = (IUIExtension<?, ?>) mh.invoke(container.getClass());
				break;
			case NO_ARGS:
				ret = (IUIExtension<?, ?>) mh.invoke();
				break;
			default:
				throw new AssertionError();
		}
		((IExtensionContainer<?>) container).addExtension(CastUtilities.castUnchecked(ret)); // COMMENT addExtension should check
		// COMMENT add other stuff after adding the extension
		Iterables.concat(
				object.getRenderer()
						.map(ImmutableSet::of)
						.orElseGet(ImmutableSet::of),
				object.getAnyContainer()
						.map(AnyContainer::getAny)
						.orElseGet(ImmutableList::of))
				.forEach(IThrowingConsumer.executeNow(any -> IParserContext.StaticHolder.findHandler(context, any)
						.ifPresent(IThrowingConsumer.executeNow(handler -> handler.accept(
								context,
								CastUtilities.castUnchecked(ret), // COMMENT may throw
								CastUtilities.castUnchecked(any) // COMMENT should not throw
						)))));
	}
}
