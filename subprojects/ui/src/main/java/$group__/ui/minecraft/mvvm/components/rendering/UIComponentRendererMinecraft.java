package $group__.ui.minecraft.mvvm.components.rendering;

import $group__.ui.core.binding.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.parsers.components.UIRendererConstructor;
import $group__.ui.core.structures.IUIComponentContext;
import $group__.ui.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import $group__.ui.minecraft.core.mvvm.views.components.rendering.IUIComponentRendererMinecraft;
import $group__.ui.mvvm.views.components.rendering.UIComponentRenderer;
import $group__.utilities.interfaces.INamespacePrefixedString;
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
