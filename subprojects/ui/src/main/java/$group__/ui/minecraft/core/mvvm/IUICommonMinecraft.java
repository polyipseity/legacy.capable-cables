package $group__.ui.minecraft.core.mvvm;

import $group__.ui.core.mvvm.IUICommon;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IUICommonMinecraft
		extends IUICommon {
	void initialize();

	void tick();

	void removed();
}
