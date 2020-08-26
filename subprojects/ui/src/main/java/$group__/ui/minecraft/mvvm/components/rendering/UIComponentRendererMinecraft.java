package $group__.ui.minecraft.mvvm.components.rendering;

import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.minecraft.core.mvvm.views.components.rendering.IUIComponentRendererMinecraft;
import $group__.ui.mvvm.views.components.rendering.UIComponentRenderer;
import $group__.utilities.interfaces.INamespacePrefixedString;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class UIComponentRendererMinecraft<C extends IUIComponent>
		extends UIComponentRenderer<C>
		implements IUIComponentRendererMinecraft<C> {
	// TODO PARSER constructor
	public UIComponentRendererMinecraft(Class<C> containerClass, Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping) { super(containerClass, mapping); }

	@Override
	public void render(C container, IAffineTransformStack stack, Point2D cursorPosition, double partialTicks, boolean pre) {}
}
