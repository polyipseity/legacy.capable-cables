package $group__.client.ui.mvvm.minecraft.core;

import $group__.client.ui.mvvm.core.IUICommon;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IUICommonMinecraft
		extends IUICommon {
	void tick();

	void removed();
}
