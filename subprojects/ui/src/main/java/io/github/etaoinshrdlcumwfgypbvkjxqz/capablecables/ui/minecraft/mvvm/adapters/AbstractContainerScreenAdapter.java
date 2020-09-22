package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.IUIInfrastructureMinecraft;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractContainerScreenAdapter<I extends IUIInfrastructureMinecraft<?, ?, ?>, C extends Container>
		extends AbstractScreenAdapter<I>
		implements IHasContainer<C> {
	protected AbstractContainerScreenAdapter(ITextComponent title) { super(title); }

	@Override
	public abstract C getContainer()
			throws UnsupportedOperationException;
}
