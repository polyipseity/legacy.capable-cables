package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.handlers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.Renderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.IParserContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.DefaultUIComponentParser;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.AnnotationUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class UIDefaultComponentParserRendererHandler
		extends UIAbstractComponentParserHandler<Object, Renderer, Throwable> {
	@Override
	@SuppressWarnings("deprecation")
	public void accept0(IParserContext context, Object container, Renderer object)
			throws Throwable {
		if (!(container instanceof IUIRendererContainer))
			return;
		IUIRendererContainer<?> rendererContainer = ((IUIRendererContainer<?>) container);
		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = DefaultUIComponentParser.createMappings(object.getProperty());
		UIRendererConstructor.IArguments argument = new UIRendererConstructor.ImmutableArguments(object.getName(), mappings, rendererContainer.getClass());

		Class<?> clazz = Optional.ofNullable(object.getClazz())
				.<Class<?>>map(classAlias -> AssertionUtilities.assertNonnull(context.getAliasesView().get(classAlias)))
				.orElseGet(rendererContainer::getDefaultRendererClass);
		Constructor<?> constructor = AnnotationUtilities.getElementAnnotatedWith(UIRendererConstructor.class, Arrays.asList(clazz.getDeclaredConstructors()));
		MethodHandle constructorHandle = InvokeUtilities.IMPL_LOOKUP.unreflectConstructor(constructor);

		IUIRenderer<?> ret = (IUIRenderer<?>) constructorHandle.invoke(argument);

		rendererContainer.setRenderer(CastUtilities.castUnchecked(ret)); // COMMENT setRenderer should check
	}
}
