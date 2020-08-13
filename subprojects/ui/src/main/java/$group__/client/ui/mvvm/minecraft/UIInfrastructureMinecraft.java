package $group__.client.ui.mvvm.minecraft;

import $group__.client.ui.mvvm.UIInfrastructure;
import $group__.client.ui.mvvm.core.binding.IBinder;
import $group__.client.ui.mvvm.minecraft.core.IUIInfrastructureMinecraft;
import $group__.client.ui.mvvm.minecraft.core.IUIViewModelMinecraft;
import $group__.client.ui.mvvm.minecraft.core.views.IUIViewMinecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIInfrastructureMinecraft<V extends IUIViewMinecraft<?>, VM extends IUIViewModelMinecraft<?>, B extends IBinder>
		extends UIInfrastructure<V, VM, B>
		implements IUIInfrastructureMinecraft<V, VM, B> {
	public UIInfrastructureMinecraft(V view, VM viewModel, B binder) { super(view, viewModel, binder); }
}
