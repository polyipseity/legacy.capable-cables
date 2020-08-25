package $group__.ui.minecraft.mvvm.components.common;

import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.rendering.IUIComponentRendererContainer;
import $group__.ui.core.parsers.components.UIComponentConstructor;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import $group__.ui.minecraft.core.mvvm.views.components.rendering.IUIComponentRendererMinecraft;
import $group__.ui.mvvm.views.components.common.UIComponentWindow;
import $group__.ui.mvvm.views.components.rendering.UIComponentRendererContainer;
import $group__.ui.utilities.minecraft.DrawingUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.awt.geom.AffineTransform;
import java.awt.geom.RectangularShape;
import java.util.Map;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class UIComponentWindowMinecraft
		extends UIComponentWindow
		implements IUIComponentMinecraft {
	protected final IUIComponentRendererContainer<IUIComponentRendererMinecraft> rendererContainer =
			new UIComponentRendererContainer<>((container, stack, cursorPosition, partialTicks, pre) -> {
				// TODO move color to the renderer
				AffineTransform transform = stack.getDelegated().peek();
				if (pre) {
					DrawingUtilities.drawShape(transform, getShapeDescriptor().getShapeOutput(), true, getColorBackground().getValue(), 0);
					DrawingUtilities.drawShape(transform, getShapeDescriptor().getShapeOutput(), true, getColorBorder().getValue(), 0);
				}
			});

	@UIComponentConstructor(type = UIComponentConstructor.ConstructorType.SHAPE_DESCRIPTOR__MAPPING)
	public UIComponentWindowMinecraft(IShapeDescriptor<RectangularShape> shapeDescriptor, Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping) { super(shapeDescriptor, mapping); }

	@Override
	public Optional<? extends IUIComponentRendererMinecraft> getRenderer() { return getRendererContainer().getRenderer(); }

	@Override
	public void setRenderer(@Nullable IUIComponentRendererMinecraft renderer) { getRendererContainer().setRenderer(renderer); }

	protected IUIComponentRendererContainer<IUIComponentRendererMinecraft> getRendererContainer() { return rendererContainer; }
}
