package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.relocatable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserRelocatableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;

public class UIComponentUserRelocatableExtensionEmptyRelocatingRenderer
		extends UIAbstractComponentUserRelocatableExtensionRelocatingRenderer {
	@UIRendererConstructor
	public UIComponentUserRelocatableExtensionEmptyRelocatingRenderer(UIRendererConstructor.IArguments arguments) {
		super(arguments);
	}

	@Override
	public void render(IUIComponentContext context, IUIComponentUserRelocatableExtension.IRelocateData data) {}
}
