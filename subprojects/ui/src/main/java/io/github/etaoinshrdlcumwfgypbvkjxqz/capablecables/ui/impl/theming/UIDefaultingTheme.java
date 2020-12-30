package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.theming;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming.IUITheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.core.IThrowingConsumer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.util.Iterator;

public class UIDefaultingTheme
		implements IUITheme {
	@Override
	public void apply(Iterator<? extends IUIRendererContainer<?>> rendererContainers) {
		applyDefaultRenderers(rendererContainers);
	}

	@SuppressWarnings("deprecation")
	public static void applyDefaultRenderers(Iterator<? extends IUIRendererContainer<?>> rendererContainers) {
		try {
			rendererContainers.forEachRemaining(IThrowingConsumer.executeNow(rendererContainer ->
					rendererContainer.setRenderer(
							CastUtilities.castUnchecked(IUIRendererContainer.createDefaultRenderer(rendererContainer)) // COMMENT setRenderer should check
					)
			));
		} catch (Throwable throwable) {
			throw ThrowableUtilities.propagate(throwable);
		}
	}
}
