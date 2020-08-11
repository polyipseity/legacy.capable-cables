package $group__.client.ui.mvvm.minecraft.core;

import $group__.client.ui.mvvm.core.models.IUIModel;
import $group__.client.ui.mvvm.core.viewmodels.IUIViewModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IUIViewModelMinecraft<M extends IUIModel>
		extends IUIViewModel<M>, IUICommonMinecraft {}
