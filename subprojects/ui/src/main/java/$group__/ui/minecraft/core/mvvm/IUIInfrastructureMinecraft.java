package $group__.ui.minecraft.core.mvvm;

import $group__.ui.core.mvvm.IUIInfrastructure;
import $group__.ui.core.mvvm.binding.IBinder;
import $group__.ui.minecraft.core.mvvm.views.IUIViewMinecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IUIInfrastructureMinecraft<V extends IUIViewMinecraft<?>, VM extends IUIViewModelMinecraft<?>, B extends IBinder>
		extends IUIInfrastructure<V, VM, B> {
	default void initialize() {
		getViewModel().initialize();
		getView().initialize();
	}

	default void tick() {
		getViewModel().tick();
		getView().tick();
	}

	default void removed() {
		getViewModel().removed();
		getView().removed();
	}
}
