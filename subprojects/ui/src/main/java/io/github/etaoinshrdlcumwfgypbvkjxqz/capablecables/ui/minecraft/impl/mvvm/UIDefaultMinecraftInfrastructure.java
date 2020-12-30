package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.mvvm;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.IUIInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.UIDefaultInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.def.mvvm.IUIMinecraftInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.def.mvvm.IUIMinecraftViewModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.def.mvvm.views.IUIMinecraftView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBinder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIDefaultMinecraftInfrastructure<V extends IUIMinecraftView<?>, VM extends IUIMinecraftViewModel<?>, B extends IBinder>
		extends UIDefaultInfrastructure<V, VM, B>
		implements IUIMinecraftInfrastructure<V, VM, B> {
	public static <V extends IUIMinecraftView<?>,
			VM extends IUIMinecraftViewModel<?>,
			B extends IBinder> UIDefaultMinecraftInfrastructure<V, VM, B> of(V view,
	                                                                         VM viewModel,
	                                                                         B binder) {
		return IUIInfrastructure.create(UIDefaultMinecraftInfrastructure::new, view, viewModel, binder);
	}

	protected UIDefaultMinecraftInfrastructure() {}

	@Override
	public void tick() {
		getViewModel().tick();
		getView().tick();
	}
}
