package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.mvvm.viewmodels;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.models.IUIModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.viewmodels.UIDefaultViewModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.def.mvvm.IUIMinecraftViewModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIDefaultMinecraftViewModel<M extends IUIModel>
		extends UIDefaultViewModel<M>
		implements IUIMinecraftViewModel<M> {
	public UIDefaultMinecraftViewModel(M model) { super(model); }

	@Override
	public void tick() {}
}
