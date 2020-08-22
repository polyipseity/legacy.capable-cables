package $group__.client.ui.mvvm.minecraft.core.views;

import $group__.client.ui.mvvm.core.views.components.IUIComponentManager;
import $group__.client.ui.mvvm.core.views.components.IUIViewComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public interface IUIViewComponentMinecraft<S extends Shape, M extends IUIComponentManager<S>>
		extends IUIViewComponent<S, M>, IUIViewMinecraft<S> {}
