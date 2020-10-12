package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.handlers;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.RendererPlaceholder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.IParserContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.AnnotationUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;
import java.util.Arrays;

public class UIDefaultComponentParserRendererPlaceholderHandler
		extends UIAbstractComponentParserHandler<Object, RendererPlaceholder, Throwable> {
	@Override
	@SuppressWarnings("deprecation")
	public void accept0(IParserContext context, Object container, RendererPlaceholder object)
			throws Throwable {
		if (!(container instanceof IUIRendererContainer))
			return;
		IUIRendererContainer<?> rendererContainer = ((IUIRendererContainer<?>) container);
		UIRendererConstructor.IArguments argument = new UIRendererConstructor.ImmutableArguments(object.getName(), ImmutableMap.of(), rendererContainer.getClass());

		Class<?> clazz = rendererContainer.getDefaultRendererClass();
		Constructor<?> constructor = AnnotationUtilities.getElementAnnotatedWith(UIRendererConstructor.class, Arrays.asList(clazz.getDeclaredConstructors()));
		MethodHandle constructorHandle = InvokeUtilities.IMPL_LOOKUP.unreflectConstructor(constructor);

		IUIRenderer<?> ret = (IUIRenderer<?>) constructorHandle.invoke(argument);

		rendererContainer.setRenderer(CastUtilities.castUnchecked(ret)); // COMMENT setRenderer should check
	}
}
