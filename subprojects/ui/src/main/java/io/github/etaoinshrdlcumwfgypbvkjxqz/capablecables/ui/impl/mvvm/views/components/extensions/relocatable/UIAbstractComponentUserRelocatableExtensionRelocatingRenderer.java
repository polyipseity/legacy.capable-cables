package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.relocatable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserRelocatableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRenderer;

public abstract class UIAbstractComponentUserRelocatableExtensionRelocatingRenderer
		extends UIDefaultRenderer<IUIComponentUserRelocatableExtension<?>>
		implements IUIComponentUserRelocatableExtension.IRelocatingRenderer {
	public UIAbstractComponentUserRelocatableExtensionRelocatingRenderer(UIRendererConstructor.IArguments arguments) {
		super(arguments);
	}
}
