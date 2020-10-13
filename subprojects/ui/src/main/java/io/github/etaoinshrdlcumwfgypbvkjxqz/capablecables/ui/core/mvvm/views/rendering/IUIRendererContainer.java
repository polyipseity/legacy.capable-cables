package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.INamed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.traits.IHasBinding;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.AnnotationUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public interface IUIRendererContainer<R extends IUIRenderer<?>>
		extends INamed, IHasBinding {
	Optional<? extends R> getRenderer();

	@Deprecated
		// COMMENT unchecked, use one of the checked variants
	void setRenderer(@Nullable R renderer);

	static <C extends IUIRendererContainer<? super R>, R extends IUIRenderer<? super C>> void setRendererChecked(C container, R renderer) { container.setRenderer(renderer); }

	Class<? extends R> getDefaultRendererClass();

	static IUIRenderer<?> createDefaultRenderer(IUIRendererContainer<?> rendererContainer)
			throws Throwable {
		Class<?> clazz = rendererContainer.getDefaultRendererClass();
		Constructor<?> constructor = AnnotationUtilities.getElementAnnotatedWith(UIRendererConstructor.class, Arrays.asList(clazz.getDeclaredConstructors()));
		MethodHandle constructorHandle = InvokeUtilities.IMPL_LOOKUP.unreflectConstructor(constructor);

		UIRendererConstructor.IArguments argument = new UIRendererConstructor.ImmutableArguments(
				ImmutableMap.of(),
				rendererContainer.getContainer()
						.orElseThrow(IllegalStateException::new).getClass()
		);

		return (IUIRenderer<?>) constructorHandle.invoke(argument);
	}

	Optional<? extends IUIRendererContainerContainer<?>> getContainer();

	enum StaticHolder {
		;

		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

		public static <R extends IUIRenderer<?>> void setRendererImpl(Object container,
		                                                              @Nullable R renderer,
		                                                              Consumer<? super R> setter,
		                                                              @Nullable IUIRenderer<?> previousRenderer) {
			if (!(renderer == null || renderer.getGenericClass().isInstance(container)))
				throw new IllegalArgumentException(
						new LogMessageBuilder()
								.addMarkers(UIMarkers.getInstance()::getMarkerUIComponentRenderer)
								.addKeyValue("container", container).addKeyValue("renderer", renderer).addKeyValue("setter", setter)
								.addMessages(() -> getResourceBundle().getString("renderer.set.impl.instance_of.fail"))
								.build()
				);
			if (previousRenderer != null)
				previousRenderer.onRendererRemoved();
			setter.accept(renderer);
			if (renderer != null)
				renderer.onRendererAdded(CastUtilities.castUnchecked(container));
		}

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
	}
}
