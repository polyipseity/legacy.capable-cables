package $group__.ui.minecraft.mvvm.adapters;

import $group__.ui.core.mvvm.adapters.IUIAdapter;
import $group__.ui.minecraft.core.mvvm.IUIInfrastructureMinecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractScreenAdapter<I extends IUIInfrastructureMinecraft<?, ?, ?>>
		extends Screen
		implements IUIAdapter<I> {
	protected AbstractScreenAdapter(ITextComponent title) { super(title); }

	@SuppressWarnings("unused")
	public abstract boolean hasContainer();
}
