package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.handlers;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.AnyContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.Extension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.IParserContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIExtensionConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.DefaultUIComponentParser;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.AnnotationUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingConsumer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;

public class UIDefaultComponentParserExtensionHandler
		extends UIAbstractComponentParserHandler<Object, Extension, Throwable> {
	@Override
	@SuppressWarnings("deprecation")
	public void accept0(IParserContext context, Object container, Extension object)
			throws Throwable {
		if (!(container instanceof IExtensionContainer))
			return;
		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = DefaultUIComponentParser.createMappings(object.getProperty());
		UIExtensionConstructor.IArguments argument = new UIExtensionConstructor.ImmutableArguments(mappings, container.getClass());

		Class<?> clazz = AssertionUtilities.assertNonnull(context.getAliasesView().get(object.getClazz()));
		Constructor<?> constructor = AnnotationUtilities.getElementAnnotatedWith(UIExtensionConstructor.class,
				Arrays.asList(clazz.getDeclaredConstructors()));
		MethodHandle constructorHandle = InvokeUtilities.IMPL_LOOKUP.unreflectConstructor(constructor);

		IUIExtension<?, ?> ret = (IUIExtension<?, ?>) constructorHandle.invoke(argument);
		((IExtensionContainer<?>) container).addExtension(CastUtilities.castUnchecked(ret)); // COMMENT addExtension should check
		// COMMENT add other stuff after adding the extension
		Iterables.concat(
				ImmutableSet.of(object.getRendererPlaceholder()),
				object.getAnyContainer()
						.<Iterable<Object>>map(AnyContainer::getAny)
						.orElseGet(ImmutableSet::of))
				.forEach(IThrowingConsumer.executeNow(any -> {
					assert any != null;
					IParserContext.StaticHolder.findHandler(context, any)
							.ifPresent(IThrowingConsumer.executeNow(handler -> {
								assert handler != null;
								handler.accept(
										context,
										CastUtilities.castUnchecked(ret), // COMMENT may throw
										CastUtilities.castUnchecked(any) // COMMENT should not throw
								);
							}));
				}));
	}
}
