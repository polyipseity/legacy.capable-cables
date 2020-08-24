package $group__.client.ui.minecraft.core.mvvm;

import $group__.client.ui.core.mvvm.IUICommon;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IUICommonMinecraft
		extends IUICommon {
	void initialize();

	void tick();

	void removed();
}
