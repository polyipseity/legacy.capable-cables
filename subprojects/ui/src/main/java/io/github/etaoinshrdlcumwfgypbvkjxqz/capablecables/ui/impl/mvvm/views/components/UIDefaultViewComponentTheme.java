package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming.IUITheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

public class UIDefaultViewComponentTheme
		implements IUITheme {
	@SuppressWarnings("deprecation")
	@Override
	public void apply(Iterable<? extends IUIRendererContainer<?>> rendererContainers) {
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
