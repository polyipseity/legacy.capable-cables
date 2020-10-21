package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views;

import com.google.common.cache.CacheLoader;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationController;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.INamedTrackers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming.IUIThemeStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.controllers.UIDefaultAnimationController;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.UIAbstractSubInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.naming.ConcurrentConfigurableNamedTracker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.naming.LoadingNamedTrackers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.theming.UIArrayThemeStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.theming.UIEmptyTheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ConcurrencyUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.FunctionUtilities;

import java.awt.*;

public abstract class UIAbstractView<S extends Shape>
		extends UIAbstractSubInfrastructure<IUIViewContext>
		implements IUIView<S> {
	private final IUIAnimationController animationController = new UIDefaultAnimationController();
	private final INamedTrackers namedTrackers = new LoadingNamedTrackers(CacheLoader.from(() ->
			new ConcurrentConfigurableNamedTracker<>(builder ->
					builder.weakValues() // COMMENT use weak values - the trackers do not own OUR objects
							.concurrencyLevel(ConcurrencyUtilities.getNormalThreadThreadCount())
							.initialCapacity(CapacityUtilities.getInitialCapacityLarge()))));
	private final IUIThemeStack themeStack;

	public UIAbstractView() {
		this.themeStack = new UIArrayThemeStack(FunctionUtilities.getEmptyConsumer(), CapacityUtilities.getInitialCapacitySmall());
		this.themeStack.push(new UIEmptyTheme());
	}

	@Override
	public IUIAnimationController getAnimationController() { return animationController; }

	@Override
	public INamedTrackers getNamedTrackers() { return namedTrackers; }

	@Override
	public IUIThemeStack getThemeStack() { return themeStack; }
}