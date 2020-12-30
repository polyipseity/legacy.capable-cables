package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.handlers;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.Renderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIRendererArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.ui.components.contexts.IJAXBUIComponentThemeAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.construction.UIImmutableRendererArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.JAXBUIComponentUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.AnnotationUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class JAXBUIDefaultComponentThemeAdapterRendererHandler
		extends JAXBUIAbstractSubContextualAdapterHandler<Renderer, IJAXBUIComponentThemeAdapterContext> {
	public JAXBUIDefaultComponentThemeAdapterRendererHandler() {
		super(IJAXBUIComponentThemeAdapterContext.class);
	}

	@Override
	@SuppressWarnings({"deprecation", "cast"})
	protected void accept0(IJAXBAdapterContext context, IJAXBUIComponentThemeAdapterContext subContext, Renderer left) {
		// COMMENT we should try to prepare as much as possible beforehand
		Map<IIdentifier, IUIPropertyMappingValue> mappings = JAXBUIComponentUtilities.createMappings(context, left.getProperty().iterator());
		String name = left.getName();
		Optional<Class<?>> rawClass = left.getClazz()
				.map(classAlias -> AssertionUtilities.assertNonnull(subContext.getAliasesView().get(classAlias)));

		subContext.getBuilder()
				.addRendererContainerAppliers(ImmutableList.<Consumer<@Nonnull ? super Map<? super String, ? extends IUIRendererContainer<?>>>>of(
						rendererContainerMap ->
								Optional.ofNullable(rendererContainerMap.get(name))
										.ifPresent(rendererContainer -> {
											Class<?> clazz = rawClass
													.orElseGet(rendererContainer::getDefaultRendererClass);
											Constructor<?> constructor = AnnotationUtilities.getElementAnnotatedWith(UIRendererConstructor.class, ImmutableList.copyOf(clazz.getDeclaredConstructors()).iterator());
											MethodHandle constructorHandle;
											try {
												constructorHandle = InvokeUtilities.getImplLookup().unreflectConstructor(constructor);
											} catch (IllegalAccessException e) {
												throw ThrowableUtilities.propagate(e);
											}
											constructorHandle = constructorHandle.asType(constructorHandle.type().changeReturnType(IUIRenderer.class));

											IUIRendererArguments argument = UIImmutableRendererArguments.of(
													mappings,
													rendererContainer.getContainer()
															.orElseThrow(IllegalStateException::new).getClass()
											);

											IUIRenderer<?> ret;
											try {
												ret = (IUIRenderer<?>) constructorHandle.invokeExact((IUIRendererArguments) argument);
											} catch (Throwable throwable) {
												throw ThrowableUtilities.propagate(throwable);
											}

											rendererContainer.setRenderer(CastUtilities.castUnchecked(ret)); // COMMENT setRenderer should check
										})
				).iterator());
	}
}
