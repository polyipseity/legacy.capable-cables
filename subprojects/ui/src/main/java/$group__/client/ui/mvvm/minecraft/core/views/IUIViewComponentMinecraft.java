package $group__.client.ui.mvvm.minecraft.core.views;

import $group__.client.ui.mvvm.core.structures.IShapeDescriptor;
import $group__.client.ui.mvvm.core.views.components.IUIComponentManager;
import $group__.client.ui.mvvm.core.views.components.IUIViewComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IUIViewComponentMinecraft<SD extends IShapeDescriptor<?>, M extends IUIComponentManager<? extends SD>>
		extends IUIViewComponent<SD, M>, IUIViewMinecraft<SD> {}
