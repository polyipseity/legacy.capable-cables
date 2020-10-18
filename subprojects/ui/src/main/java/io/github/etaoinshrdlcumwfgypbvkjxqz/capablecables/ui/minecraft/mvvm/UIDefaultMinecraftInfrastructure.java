package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.IUIMinecraftInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.IUIMinecraftViewModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.IUIMinecraftView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.UIDefaultInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.IBinder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIDefaultMinecraftInfrastructure<V extends IUIMinecraftView<?>, VM extends IUIMinecraftViewModel<?>, B extends IBinder>
		extends UIDefaultInfrastructure<V, VM, B>
		implements IUIMinecraftInfrastructure<V, VM, B> {
	public UIDefaultMinecraftInfrastructure(V view, VM viewModel, B binder) { super(view, viewModel, binder); }

	@Override
	public void initialize() {
		getViewModel().initialize();
		getView().initialize();
	}

	@Override
	public void tick() {
		getViewModel().tick();
		getView().tick();
	}

	@Override
	public void removed() {
		getViewModel().removed();
		getView().removed();
	}
}
