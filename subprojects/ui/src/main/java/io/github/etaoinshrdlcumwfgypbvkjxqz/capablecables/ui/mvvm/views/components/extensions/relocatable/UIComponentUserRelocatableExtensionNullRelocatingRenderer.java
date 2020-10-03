package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions.relocatable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserRelocatableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;

import java.util.Map;

public class UIComponentUserRelocatableExtensionNullRelocatingRenderer
		extends AbstractUIComponentUserRelocatableExtensionRelocatingRenderer {
	@UIRendererConstructor(type = UIRendererConstructor.EnumConstructorType.MAPPINGS)
	public UIComponentUserRelocatableExtensionNullRelocatingRenderer(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings) {
		super(mappings);
	}

	@Override
	public void render(IUIComponentContext context, IUIComponentUserRelocatableExtension.IRelocateData data) {}
}
