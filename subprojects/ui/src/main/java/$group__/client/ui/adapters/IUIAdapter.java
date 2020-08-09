package $group__.client.ui.adapters;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@FunctionalInterface
@OnlyIn(Dist.CLIENT)
public interface IUIAdapter<M extends IGuiViewComponentManager> {
	M getManager();
}
