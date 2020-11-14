package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.handlers;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.Renderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.ui.components.contexts.IJAXBUIComponentThemeAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.annotations.ui.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.JAXBUIDefaultComponentAdapter;
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

public class JAXBUIDefaultComponentThemeAdapterRendererHandler
		extends JAXBUIAbstractSubContextualAdapterHandler<Renderer, IJAXBUIComponentThemeAdapterContext> {
	public JAXBUIDefaultComponentThemeAdapterRendererHandler() {
		super(IJAXBUIComponentThemeAdapterContext.class);
	}

	@Override
	@SuppressWarnings({"deprecation", "cast"})
	protected void accept0(IJAXBAdapterContext context, IJAXBUIComponentThemeAdapterContext subContext, Renderer left) {
		// COMMENT we should try to prepare as much as possible beforehand
		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = JAXBUIDefaultComponentAdapter.createMappings(context, left.getProperty());
		String name = left.getName();
		Optional<Class<?>> rawClass = left.getClazz()
				.map(classAlias -> AssertionUtilities.assertNonnull(subContext.getAliasesView().get(classAlias)));

		subContext.getBuilder()
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
											constructorHandle = constructorHandle.asType(constructorHandle.type().changeReturnType(IUIRenderer.class));

											UIRendererConstructor.IArguments argument = new UIRendererConstructor.ImmutableArguments(
													mappings,
													rendererContainer.getContainer()
															.orElseThrow(IllegalStateException::new).getClass()
											);

											IUIRenderer<?> ret;
											try {
												ret = (IUIRenderer<?>) constructorHandle.invokeExact((UIRendererConstructor.IArguments) argument);
											} catch (Throwable throwable) {
												throw ThrowableUtilities.propagate(throwable);
											}

											rendererContainer.setRenderer(CastUtilities.castUnchecked(ret)); // COMMENT setRenderer should check
										})
				));
	}
}
