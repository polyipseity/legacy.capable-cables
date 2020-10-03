package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions.relocatable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserRelocatableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.rendering.NullUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;

import java.util.Map;

public abstract class AbstractUIComponentUserRelocatableExtensionRelocatingRenderer
		extends NullUIRenderer<IUIComponentUserRelocatableExtension<?>>
		implements IUIComponentUserRelocatableExtension.IRelocatingRenderer {
	public AbstractUIComponentUserRelocatableExtensionRelocatingRenderer(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings) {
		super(mappings, CastUtilities.castUnchecked(IUIComponentUserRelocatableExtension.class));
	}
}
