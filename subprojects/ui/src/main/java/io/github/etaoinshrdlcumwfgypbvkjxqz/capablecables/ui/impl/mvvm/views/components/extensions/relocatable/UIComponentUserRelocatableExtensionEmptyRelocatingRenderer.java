package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.relocatable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIRendererArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserRelocatableExtension;

public class UIComponentUserRelocatableExtensionEmptyRelocatingRenderer
		extends UIAbstractComponentUserRelocatableExtensionRelocatingRenderer {
	@UIRendererConstructor
	public UIComponentUserRelocatableExtensionEmptyRelocatingRenderer(IUIRendererArguments arguments) {
		super(arguments);
	}

	@Override
	public void render(IUIComponentContext context, IUIComponentUserRelocatableExtension.IRelocateData data) {}
}
