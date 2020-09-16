package $group__.ui.minecraft.core.mvvm;

import $group__.ui.core.mvvm.IUISubInfrastructure;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IUISubInfrastructureMinecraft
		extends IUISubInfrastructure {
	void tick();
}
