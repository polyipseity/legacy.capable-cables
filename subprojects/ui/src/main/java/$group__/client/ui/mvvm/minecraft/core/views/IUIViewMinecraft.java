package $group__.client.ui.mvvm.minecraft.core.views;

import $group__.client.ui.mvvm.core.structures.IShapeDescriptor;
import $group__.client.ui.mvvm.core.views.IUIView;
import $group__.client.ui.mvvm.minecraft.core.IUICommonMinecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;

@OnlyIn(Dist.CLIENT)
public interface IUIViewMinecraft<SD extends IShapeDescriptor<?>>
		extends IUIView<SD>, IUICommonMinecraft {
	void render(Point2D cursorPosition, double partialTicks);
}
