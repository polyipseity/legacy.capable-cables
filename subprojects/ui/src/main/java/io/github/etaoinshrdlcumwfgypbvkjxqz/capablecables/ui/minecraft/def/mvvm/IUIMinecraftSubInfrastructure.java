package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.def.mvvm;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.IUISubInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.IUISubInfrastructureContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IUIMinecraftSubInfrastructure<C extends IUISubInfrastructureContext>
		extends IUISubInfrastructure<C> {
	void tick();
}
