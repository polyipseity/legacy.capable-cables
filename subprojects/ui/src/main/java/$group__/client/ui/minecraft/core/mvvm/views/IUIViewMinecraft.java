package $group__.client.ui.minecraft.core.mvvm.views;

import $group__.client.ui.core.mvvm.views.IUIView;
import $group__.client.ui.minecraft.core.mvvm.IUICommonMinecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.awt.geom.Point2D;

@OnlyIn(Dist.CLIENT)
public interface IUIViewMinecraft<S extends Shape>
		extends IUIView<S>, IUICommonMinecraft {
	void render(Point2D cursorPosition, double partialTicks);
}
