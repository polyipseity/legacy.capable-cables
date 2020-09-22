package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.components.rendering;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.components.rendering.IUIComponentRendererMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.rendering.UIComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;
import com.google.common.collect.ImmutableMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class UIComponentRendererMinecraft<C extends IUIComponent & IUIComponentMinecraft>
		extends UIComponentRenderer<C>
		implements IUIComponentRendererMinecraft<C> {
	@UIRendererConstructor(type = UIRendererConstructor.EnumConstructorType.MAPPINGS__CONTAINER_CLASS)
	public UIComponentRendererMinecraft(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings, Class<C> containerClass) { super(mappings, containerClass); }

	public UIComponentRendererMinecraft(Class<C> containerClass) { super(ImmutableMap.of(), containerClass); }

	@Override
	public void render(IUIComponentContext context, C container, EnumRenderStage stage, double partialTicks) {}
}
