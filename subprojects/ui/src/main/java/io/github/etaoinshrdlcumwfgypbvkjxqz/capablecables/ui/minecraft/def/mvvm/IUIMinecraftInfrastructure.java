package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.def.mvvm;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.IUIInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.def.mvvm.views.IUIMinecraftView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBinder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IUIMinecraftInfrastructure<V extends IUIMinecraftView<?>, VM extends IUIMinecraftViewModel<?>, B extends IBinder>
		extends IUIInfrastructure<V, VM, B> {
	void tick();
}
