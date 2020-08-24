package $group__.client.ui.minecraft.mvvm;

import $group__.client.ui.core.mvvm.binding.IBinder;
import $group__.client.ui.minecraft.core.mvvm.IUIInfrastructureMinecraft;
import $group__.client.ui.minecraft.core.mvvm.IUIViewModelMinecraft;
import $group__.client.ui.minecraft.core.mvvm.views.IUIViewMinecraft;
import $group__.client.ui.mvvm.UIInfrastructure;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIInfrastructureMinecraft<V extends IUIViewMinecraft<?>, VM extends IUIViewModelMinecraft<?>, B extends IBinder>
		extends UIInfrastructure<V, VM, B>
		implements IUIInfrastructureMinecraft<V, VM, B> {
	public UIInfrastructureMinecraft(V view, VM viewModel, B binder) { super(view, viewModel, binder); }
}
