package $group__.ui.minecraft.mvvm.viewmodels;

import $group__.ui.core.mvvm.models.IUIModel;
import $group__.ui.minecraft.core.mvvm.IUIViewModelMinecraft;
import $group__.ui.mvvm.viewmodels.UIViewModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIViewModelMinecraft<M extends IUIModel>
		extends UIViewModel<M>
		implements IUIViewModelMinecraft<M> {
	public UIViewModelMinecraft(M model) { super(model); }

	@Override
	public void tick() {}
}
