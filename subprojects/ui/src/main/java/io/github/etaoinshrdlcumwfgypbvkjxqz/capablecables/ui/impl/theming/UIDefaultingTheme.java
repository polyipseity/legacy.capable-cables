package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.theming;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming.IUITheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

public class UIDefaultingTheme
		implements IUITheme {
	@Override
	public void apply(Iterable<? extends IUIRendererContainer<?>> rendererContainers) {
		applyDefaultRenderers(rendererContainers);
	}

	@SuppressWarnings("deprecation")
	public static void applyDefaultRenderers(Iterable<? extends IUIRendererContainer<?>> rendererContainers) {
		rendererContainers.forEach(rendererContainer -> {
			try {
				rendererContainer.setRenderer(
						CastUtilities.castUnchecked(IUIRendererContainer.createDefaultRenderer(rendererContainer)) // COMMENT setRenderer should check
				);
			} catch (Throwable throwable) {
				throw ThrowableUtilities.propagate(throwable);
			}
		});
	}
}
