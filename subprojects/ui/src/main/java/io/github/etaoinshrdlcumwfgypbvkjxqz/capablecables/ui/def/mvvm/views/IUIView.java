package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.animations.IUIAnimationController;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.IUISubInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.naming.INamedTrackers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.theming.IUITheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.theming.IUIThemeStack;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * Operations not thread-safe.
 */
public interface IUIView<S extends Shape>
		extends IUISubInfrastructure<IUIViewContext>, IUIReshapeExplicitly<S> {
	static void registerRendererContainers(IUIView<?> instance, Iterator<? extends IUIRendererContainer<?>> rendererContainers) {
		IUIView.getNamedTrackers(instance).add(IUIRendererContainer.class, rendererContainers);
	}

	static INamedTrackers getNamedTrackers(IUIView<?> instance) {
		return instance.getCoordinator(INamedTrackers.class).orElseThrow(AssertionError::new);
	}

	static IUIAnimationController getAnimationController(IUIView<?> instance) {
		return instance.getCoordinator(IUIAnimationController.class).orElseThrow(AssertionError::new);
	}

	<C extends IUIViewCoordinator> Optional<? extends C> getCoordinator(Class<C> key);

	static IUIThemeStack getThemeStack(IUIView<?> instance) {
		return instance.getCoordinator(IUIThemeStack.class).orElseThrow(AssertionError::new);
	}

	static void unregisterRendererContainers(IUIView<?> instance, Iterator<? extends IUIRendererContainer<?>> rendererContainers) {
		IUIView.getNamedTrackers(instance).remove(IUIRendererContainer.class, rendererContainers);
	}

	static <T extends IUIView<?>> T withThemes(T instance, Iterator<? extends IUITheme> themes) {
		themes.forEachRemaining(getThemeStack(instance)::push);
		return instance;
	}

	IUIEventTarget getTargetAtPoint(Point2D point);

	Optional<? extends IUIEventTarget> changeFocus(@Nullable IUIEventTarget currentFocus, boolean next);

	@Immutable Map<Class<?>, IUIViewCoordinator> getCoordinatorMapView();

	void render();
}
