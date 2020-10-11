package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.views.components.rendering;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.rendering.IUIComponentRendererMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.rendering.NullUIComponentRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NullUIComponentRendererMinecraft<C extends IUIComponent & IUIComponentMinecraft>
		extends NullUIComponentRenderer<C>
		implements IUIComponentRendererMinecraft<C> {
	@UIRendererConstructor
	public NullUIComponentRendererMinecraft(UIRendererConstructor.IArguments arguments) { super(arguments); }

	@Override
	public void render(IUIComponentContext context, C container, EnumRenderStage stage, double partialTicks) {}
}
