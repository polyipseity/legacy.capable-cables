package $group__.ui.minecraft.core.mvvm.views.components;

import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.ui.core.mvvm.views.components.rendering.IUIComponentRendererContainer;
import $group__.ui.minecraft.core.mvvm.views.components.rendering.IUIComponentRendererMinecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IUIComponentMinecraft
		extends IUIComponentRendererContainer<IUIComponentRendererMinecraft<?>> {
	default void tick(IAffineTransformStack stack) {}
}
