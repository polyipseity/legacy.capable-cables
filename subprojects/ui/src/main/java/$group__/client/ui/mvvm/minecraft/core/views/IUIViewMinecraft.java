package $group__.client.ui.mvvm.minecraft.core.views;

import $group__.client.ui.mvvm.core.views.IUIView;
import $group__.client.ui.mvvm.minecraft.core.IUICommonMinecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.awt.geom.Point2D;

@OnlyIn(Dist.CLIENT)
public interface IUIViewMinecraft<S extends Shape>
		extends IUIView<S>, IUICommonMinecraft {
	void render(Point2D cursorPosition, double partialTicks);
}
