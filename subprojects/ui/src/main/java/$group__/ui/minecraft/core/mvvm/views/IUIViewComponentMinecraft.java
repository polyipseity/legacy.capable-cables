package $group__.ui.minecraft.core.mvvm.views;

import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.mvvm.views.components.IUIViewComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public interface IUIViewComponentMinecraft<S extends Shape, M extends IUIComponentManager<S>>
		extends IUIViewComponent<S, M>, IUIViewMinecraft<S> {}
