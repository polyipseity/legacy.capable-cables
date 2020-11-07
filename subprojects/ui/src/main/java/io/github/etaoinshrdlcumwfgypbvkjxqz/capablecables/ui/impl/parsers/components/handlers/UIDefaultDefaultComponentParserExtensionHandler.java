package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components.handlers;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.AnyContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.Extension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIExtensionConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.contexts.IUIAbstractDefaultComponentParserContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.contexts.IUIDefaultComponentParserContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components.UIDefaultComponentParser;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components.contexts.UIImmutableDefaultComponentParserContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.AnnotationUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingConsumer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;

public class UIDefaultDefaultComponentParserExtensionHandler
		extends UIAbstractDefaultParserObjectHandler<IUIDefaultComponentParserContext, Extension> {
	@Override
	@SuppressWarnings("deprecation")
	public void accept(@Nonnull IUIDefaultComponentParserContext context, @Nonnull Extension object) {
		context.getContainer().ifPresent(container -> {
			if (!(container instanceof IExtensionContainer))
				return;
			Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = UIDefaultComponentParser.createMappings(object.getProperty());
			UIExtensionConstructor.IArguments argument = new UIExtensionConstructor.ImmutableArguments(mappings, container.getClass());

			Class<?> clazz = AssertionUtilities.assertNonnull(context.getAliasesView().get(object.getClazz()));
			Constructor<?> constructor = AnnotationUtilities.getElementAnnotatedWith(UIExtensionConstructor.class,
					Arrays.asList(clazz.getDeclaredConstructors()));
			MethodHandle constructorHandle;
			try {
				constructorHandle = InvokeUtilities.getImplLookup().unreflectConstructor(constructor);
			} catch (IllegalAccessException e) {
				throw ThrowableUtilities.propagate(e);
			}

			IUIExtension<?, ?> ret;
			try {
				ret = (IUIExtension<?, ?>) constructorHandle.invoke(argument);
			} catch (Throwable throwable) {
				throw ThrowableUtilities.propagate(throwable);
			}
			((IExtensionContainer<?>) container).addExtension(CastUtilities.castUnchecked(ret)); // COMMENT addExtension should check

			// COMMENT add other stuff after adding the extension
			IUIDefaultComponentParserContext extensionContext = new UIImmutableDefaultComponentParserContext(context.getAliasesView(), context.getHandlersView(), context.getView().orElse(null), ret);
			Iterables.concat(
					ImmutableSet.of(object.getRendererContainer()),
					object.getAnyContainer()
							.<Iterable<Object>>map(AnyContainer::getAny)
							.orElseGet(ImmutableSet::of))
					.forEach(IThrowingConsumer.executeNow(any -> {
						assert any != null;
						IUIAbstractDefaultComponentParserContext.findHandler(extensionContext, any)
								.ifPresent(IThrowingConsumer.executeNow(handler -> {
									assert handler != null;
									handler.accept(
											extensionContext,
											CastUtilities.castUnchecked(any) // COMMENT should not throw
									);
								}));
					}));
		});
	}
}
