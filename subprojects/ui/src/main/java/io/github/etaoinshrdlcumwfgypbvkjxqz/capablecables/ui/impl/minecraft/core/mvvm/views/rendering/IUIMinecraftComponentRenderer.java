package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.rendering;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IUIMinecraftComponentRenderer<C extends IUIComponent & IUIComponentMinecraft>
		extends IUIComponentRenderer<C> {
	void render(IUIComponentContext context, EnumRenderStage stage, C component, double partialTicks);

	@OnlyIn(Dist.CLIENT)
	enum EnumRenderStage {
		PRE_CHILDREN,
		POST_CHILDREN,
		;

		public boolean isPreChildren() { return this == PRE_CHILDREN; }

		public boolean isPostChildren() { return this == POST_CHILDREN; }
	}
}
