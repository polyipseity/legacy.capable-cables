package $group__.client.ui.minecraft.core.mvvm;

import $group__.client.ui.core.mvvm.models.IUIModel;
import $group__.client.ui.core.mvvm.viewmodels.IUIViewModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IUIViewModelMinecraft<M extends IUIModel>
		extends IUIViewModel<M>, IUICommonMinecraft {}
