package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.views.components.extensions.relocatable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.utilities.MinecraftDrawingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions.relocatable.UIAbstractComponentUserRelocatableExtensionPreviewingRelocatingRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.awt.geom.Rectangle2D;

@OnlyIn(Dist.CLIENT)
public class UIComponentUserRelocatableMinecraftExtensionPreviewingRelocatingRenderer
		extends UIAbstractComponentUserRelocatableExtensionPreviewingRelocatingRenderer {
	@UIRendererConstructor
	public UIComponentUserRelocatableMinecraftExtensionPreviewingRelocatingRenderer(UIRendererConstructor.IArguments arguments) {
		super(arguments);
	}

	@Override
	public void render0(IUIComponentContext context, Rectangle2D rectangle) {
		MinecraftDrawingUtilities.drawRectangle(IUIComponentContext.getCurrentTransform(context),
				rectangle,
				Color.DARK_GRAY.getRGB(),
				0); // TODO customize
	}
}
