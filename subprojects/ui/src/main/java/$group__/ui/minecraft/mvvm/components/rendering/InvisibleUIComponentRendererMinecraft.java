package $group__.ui.minecraft.mvvm.components.rendering;

import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.minecraft.core.mvvm.views.components.rendering.IUIComponentRendererMinecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;

@OnlyIn(Dist.CLIENT)
public class InvisibleUIComponentRendererMinecraft
		implements IUIComponentRendererMinecraft {
	@Override
	public void render(IUIComponent container, IAffineTransformStack stack, Point2D cursorPosition, double partialTicks, boolean pre) {}
}
