package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.IUISubInfrastructureMinecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.awt.geom.Point2D;

@OnlyIn(Dist.CLIENT)
public interface IUIViewMinecraft<S extends Shape>
		extends IUIView<S>, IUISubInfrastructureMinecraft {
	void render(Point2D cursorPosition, double partialTicks);
}
