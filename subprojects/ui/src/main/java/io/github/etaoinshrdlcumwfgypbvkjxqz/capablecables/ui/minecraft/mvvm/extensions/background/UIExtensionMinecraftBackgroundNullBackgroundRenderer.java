package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.extensions.background;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.extensions.IUIExtensionMinecraftBackground;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.utilities.UIMinecraftBackgrounds;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.rendering.NullUIRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;

@OnlyIn(Dist.CLIENT)
public class UIExtensionMinecraftBackgroundNullBackgroundRenderer
		extends NullUIRenderer<IUIExtensionMinecraftBackground>
		implements IUIExtensionMinecraftBackground.IBackgroundRenderer {

	@UIRendererConstructor
	public UIExtensionMinecraftBackgroundNullBackgroundRenderer(UIRendererConstructor.IArguments arguments) { super(arguments); }

	@Override
	public void render(Screen screen, Point2D mouse, double partialTicks) { UIMinecraftBackgrounds.notifyBackgroundDrawn(screen); }
}
