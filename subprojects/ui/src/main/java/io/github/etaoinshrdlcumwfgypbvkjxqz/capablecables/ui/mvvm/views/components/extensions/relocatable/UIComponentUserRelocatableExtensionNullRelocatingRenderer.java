package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions.relocatable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserRelocatableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;

public class UIComponentUserRelocatableExtensionNullRelocatingRenderer
		extends AbstractUIComponentUserRelocatableExtensionRelocatingRenderer {
	@UIRendererConstructor
	public UIComponentUserRelocatableExtensionNullRelocatingRenderer(UIRendererConstructor.IArguments arguments) {
		super(arguments);
	}

	@Override
	public void render(IUIComponentContext context, IUIComponentUserRelocatableExtension.IRelocateData data) {}
}
