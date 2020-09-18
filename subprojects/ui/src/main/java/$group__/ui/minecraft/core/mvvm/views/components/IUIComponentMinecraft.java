package $group__.ui.minecraft.core.mvvm.views.components;

import $group__.ui.core.mvvm.views.rendering.IUIRendererContainer;
import $group__.ui.core.structures.IUIComponentContext;
import $group__.ui.minecraft.core.mvvm.views.components.rendering.IUIComponentRendererMinecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IUIComponentMinecraft
		extends IUIRendererContainer<IUIComponentRendererMinecraft<?>> {
	default void tick(IUIComponentContext context) {}
}
