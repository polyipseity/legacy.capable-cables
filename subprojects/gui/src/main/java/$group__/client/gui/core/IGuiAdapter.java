package $group__.client.gui.core;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@FunctionalInterface
@OnlyIn(Dist.CLIENT)
public interface IGuiAdapter<M extends IGuiComponent.IManager<?, ?, ?, ?, ?>> {
	M getManager();
}
