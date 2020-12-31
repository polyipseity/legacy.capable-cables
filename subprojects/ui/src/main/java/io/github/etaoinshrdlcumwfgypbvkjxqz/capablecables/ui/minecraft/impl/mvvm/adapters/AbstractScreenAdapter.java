package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.mvvm.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.IUIInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.adapters.IUIAdapter;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractScreenAdapter<I extends IUIInfrastructure<?, ?, ?>>
		extends Screen
		implements IUIAdapter<I> {
	protected AbstractScreenAdapter(ITextComponent title) { super(title); }

	@SuppressWarnings("unused")
	public abstract boolean hasContainer();
}
