package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.mvvm.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.adapters.IUIAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.IUIMinecraftInfrastructure;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractScreenAdapter<I extends IUIMinecraftInfrastructure<?, ?, ?>>
		extends Screen
		implements IUIAdapter<I> {
	protected AbstractScreenAdapter(ITextComponent title) { super(title); }

	@SuppressWarnings("unused")
	public abstract boolean hasContainer();
}
