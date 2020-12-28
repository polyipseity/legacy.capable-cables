package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.mvvm.extensions.background;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIRendererArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.utilities.UIMinecraftBackgrounds;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;

@OnlyIn(Dist.CLIENT)
public class UIMinecraftBackgroundExtensionDirtBackgroundRenderer
		extends UIMinecraftBackgroundExtensionEmptyBackgroundRenderer {
	@UIRendererConstructor
	public UIMinecraftBackgroundExtensionDirtBackgroundRenderer(IUIRendererArguments arguments) {
		super(arguments);
	}

	@Override
	public void render(Screen screen, Point2D mouse, double partialTicks) { UIMinecraftBackgrounds.renderDirtBackgroundAndNotify(screen.getMinecraft(), screen.width, screen.height, 0); }
}
