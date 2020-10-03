package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.views.components.extensions.relocatable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.utilities.MinecraftDrawingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions.relocatable.AbstractUIComponentUserRelocatableExtensionPreviewingRelocatingRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class UIComponentUserRelocatableMinecraftExtensionPreviewingRelocatingRenderer
		extends AbstractUIComponentUserRelocatableExtensionPreviewingRelocatingRenderer {
	@UIRendererConstructor(type = UIRendererConstructor.EnumConstructorType.MAPPINGS)
	public UIComponentUserRelocatableMinecraftExtensionPreviewingRelocatingRenderer(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings) {
		super(mappings);
	}

	@Override
	public void render0(IUIComponentContext context, Rectangle2D rectangle) {
		MinecraftDrawingUtilities.drawRectangle(context.getTransformStack().element(),
				rectangle,
				Color.DARK_GRAY.getRGB(),
				0); // TODO customize
	}
}
