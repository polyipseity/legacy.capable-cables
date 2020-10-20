package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components.handlers;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.Renderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components.UIDefaultComponentParser;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components.contexts.IUIDefaultComponentThemeParserContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.AnnotationUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class UIDefaultDefaultComponentThemeParserRendererHandler
		extends UIAbstractDefaultParserObjectHandler<IUIDefaultComponentThemeParserContext, Renderer> {
	@SuppressWarnings("deprecation")
	@Override
	public void acceptNonnull(IUIDefaultComponentThemeParserContext context, Renderer object) {
		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = UIDefaultComponentParser.createMappings(object.getProperty());

		// COMMENT we should try to prepare as much as possible beforehand
		String name = object.getName();
		Optional<Class<?>> rawClass = object.getClazz()
				.map(classAlias -> AssertionUtilities.assertNonnull(context.getAliasesView().get(classAlias)));

		context.getBuilder()
				.addRendererContainerAppliers(ImmutableList.of(
						rendererContainerMap ->
								Optional.ofNullable(rendererContainerMap.get(name))
										.ifPresent(rendererContainer -> {
											Class<?> clazz = rawClass
													.orElseGet(rendererContainer::getDefaultRendererClass);
											Constructor<?> constructor = AnnotationUtilities.getElementAnnotatedWith(UIRendererConstructor.class, Arrays.asList(clazz.getDeclaredConstructors()));
											MethodHandle constructorHandle;
											try {
												constructorHandle = InvokeUtilities.getImplLookup().unreflectConstructor(constructor);
											} catch (IllegalAccessException e) {
												throw ThrowableUtilities.propagate(e);
											}

											UIRendererConstructor.IArguments argument = new UIRendererConstructor.ImmutableArguments(
													mappings,
													rendererContainer.getContainer()
															.orElseThrow(IllegalStateException::new).getClass()
											);

											IUIRenderer<?> ret;
											try {
												ret = (IUIRenderer<?>) constructorHandle.invoke(argument);
											} catch (Throwable throwable) {
												throw ThrowableUtilities.propagate(throwable);
											}

											rendererContainer.setRenderer(CastUtilities.castUnchecked(ret)); // COMMENT setRenderer should check
										})
				));
	}
}
