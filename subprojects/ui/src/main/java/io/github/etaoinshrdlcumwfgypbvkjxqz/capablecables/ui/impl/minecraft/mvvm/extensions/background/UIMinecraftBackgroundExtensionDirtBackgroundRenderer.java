package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.extensions.background;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.annotations.ui.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.utilities.UIMinecraftBackgrounds;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;

@OnlyIn(Dist.CLIENT)
public class UIMinecraftBackgroundExtensionDirtBackgroundRenderer
		extends UIMinecraftBackgroundExtensionEmptyBackgroundRenderer {
	@UIRendererConstructor
	public UIMinecraftBackgroundExtensionDirtBackgroundRenderer(UIRendererConstructor.IArguments arguments) {
		super(arguments);
	}

	@Override
	public void render(Screen screen, Point2D mouse, double partialTicks) { UIMinecraftBackgrounds.renderDirtBackgroundAndNotify(screen.getMinecraft(), screen.width, screen.height, 0); }
}
