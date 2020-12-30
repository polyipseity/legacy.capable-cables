package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.relocatable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIRendererArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.extensions.IUIComponentUserRelocatableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRenderer;

public abstract class UIAbstractComponentUserRelocatableExtensionRelocatingRenderer
		extends UIDefaultRenderer<IUIComponentUserRelocatableExtension<?>>
		implements IUIComponentUserRelocatableExtension.IRelocatingRenderer {
	public UIAbstractComponentUserRelocatableExtensionRelocatingRenderer(IUIRendererArguments arguments) {
		super(arguments);
	}
}
