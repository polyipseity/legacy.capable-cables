package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.handlers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.Renderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.IConstructorType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.IParserContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.DefaultUIComponentParser;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IConsumer3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;

import java.lang.invoke.MethodHandle;
import java.util.Map;
import java.util.Optional;

public class DefaultUIComponentParserRendererHandler
		implements IConsumer3<IParserContext, Object, Renderer, Throwable> {
	@Override
	@SuppressWarnings("deprecation")
	public void accept(IParserContext context, Object container, Renderer object)
			throws Throwable {
		if (!(container instanceof IUIRendererContainer))
			return;
		IUIRendererContainer<?> rendererContainer = ((IUIRendererContainer<?>) container);
		Class<?> oc = Optional.ofNullable(object.getClazz())
				.<Class<?>>map(classAlias -> AssertionUtilities.assertNonnull(context.getAliases().get(classAlias)))
				.orElseGet(rendererContainer::getDefaultRendererClass);
		UIRendererConstructor.EnumConstructorType ct = IConstructorType.StaticHolder.getConstructorType(oc, UIRendererConstructor.class, UIRendererConstructor::type);
		MethodHandle mh = InvokeUtilities.IMPL_LOOKUP.findConstructor(oc, ct.getMethodType());
		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = DefaultUIComponentParser.createMappings(object.getProperty());
		IUIRenderer<?> ret;
		switch (ct) {
			case MAPPINGS__CONTAINER_CLASS:
				ret = (IUIRenderer<?>) mh.invoke(mappings, container.getClass());
				break;
			case MAPPINGS:
				ret = (IUIRenderer<?>) mh.invoke(mappings);
				break;
			case CONTAINER_CLASS:
				ret = (IUIRenderer<?>) mh.invoke(container.getClass());
				break;
			case NO_ARGS:
				ret = (IUIRenderer<?>) mh.invoke();
				break;
			default:
				throw new AssertionError();
		}
		rendererContainer.setRenderer(CastUtilities.castUnchecked(ret)); // COMMENT setRenderer should check
	}
}
