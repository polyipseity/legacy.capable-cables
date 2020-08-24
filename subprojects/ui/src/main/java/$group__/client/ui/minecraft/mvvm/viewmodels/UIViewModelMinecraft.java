package $group__.client.ui.minecraft.mvvm.viewmodels;

import $group__.client.ui.core.mvvm.models.IUIModel;
import $group__.client.ui.minecraft.core.mvvm.IUIViewModelMinecraft;
import $group__.client.ui.mvvm.viewmodels.UIViewModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIViewModelMinecraft<M extends IUIModel>
		extends UIViewModel<M>
		implements IUIViewModelMinecraft<M> {
	public UIViewModelMinecraft(M model) { super(model); }

	@Override
	public void initialize() {}

	@Override
	public void tick() {}

	@Override
	public void removed() {}
}
