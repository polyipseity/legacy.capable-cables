package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views;

import com.google.common.cache.CacheLoader;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.animations.controllers.DefaultUIAnimationController;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationController;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.INamedTrackers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming.IUIThemeStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.UIAbstractSubInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.naming.ConcurrentConfigurableNamedTracker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.naming.LoadingNamedTrackers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.theming.UIArrayThemeStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.theming.UINullTheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ConcurrencyUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.FunctionUtilities;

import java.awt.*;

public abstract class UIView<S extends Shape>
		extends UIAbstractSubInfrastructure<IUIViewContext>
		implements IUIView<S> {
	private final IUIAnimationController animationController = new DefaultUIAnimationController();
	private final INamedTrackers namedTrackers = new LoadingNamedTrackers(CacheLoader.from(() ->
			new ConcurrentConfigurableNamedTracker<>(builder ->
					builder.weakValues() // COMMENT use weak values - the trackers do not own OUR objects
							.concurrencyLevel(ConcurrencyUtilities.getNormalThreadThreadCount())
							.initialCapacity(CapacityUtilities.getInitialCapacityLarge()))));
	private final IUIThemeStack themeStack;

	public UIView() {
		this.themeStack = new UIArrayThemeStack(FunctionUtilities.getEmptyConsumer(), CapacityUtilities.getInitialCapacitySmall());
		this.themeStack.push(UINullTheme.getInstance());
	}

	@Override
	public IUIAnimationController getAnimationController() { return animationController; }

	@Override
	public INamedTrackers getNamedTrackers() { return namedTrackers; }

	@Override
	public IUIThemeStack getThemeStack() { return themeStack; }
}
