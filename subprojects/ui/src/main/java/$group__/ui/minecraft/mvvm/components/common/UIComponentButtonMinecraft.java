package $group__.ui.minecraft.mvvm.components.common;

import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.rendering.IUIComponentRendererContainer;
import $group__.ui.core.parsers.components.UIComponentConstructor;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import $group__.ui.minecraft.core.mvvm.views.components.rendering.IUIComponentRendererMinecraft;
import $group__.ui.mvvm.views.components.common.UIComponentButton;
import $group__.ui.mvvm.views.components.rendering.UIComponentRendererContainer;
import $group__.ui.utilities.minecraft.DrawingUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Map;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class UIComponentButtonMinecraft
		extends UIComponentButton
		implements IUIComponentMinecraft {
	protected final IUIComponentRendererContainer<IUIComponentRendererMinecraft> rendererContainer =
			new UIComponentRendererContainer<>((container, stack, cursorPosition, partialTicks, pre) -> {
				// TODO move color to the renderer
				if (pre) {
					AffineTransform transform = stack.getDelegated().peek();
					Shape transformed = transform.createTransformedShape(getShapeDescriptor().getShapeOutput());
					if (getButtonStates().contains(IButtonState.PRESSING)) {
						DrawingUtilities.drawShape(transformed, true, getColorPressed().getValue(), 0);
						DrawingUtilities.drawShape(transformed, false, getColorPressedBorder().getValue(), 0);
					} else if (getButtonStates().contains(IButtonState.HOVERING)) {
						DrawingUtilities.drawShape(transformed, true, getColorHovering().getValue(), 0);
						DrawingUtilities.drawShape(transformed, false, getColorHoveringBorder().getValue(), 0);
					} else {
						DrawingUtilities.drawShape(transformed, true, getColorBase().getValue(), 0);
						DrawingUtilities.drawShape(transformed, false, getColorBaseBorder().getValue(), 0);
					}
				}
			});

	@UIComponentConstructor(type = UIComponentConstructor.ConstructorType.SHAPE_DESCRIPTOR__MAPPING)
	public UIComponentButtonMinecraft(IShapeDescriptor<?> shapeDescriptor, Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping) { super(shapeDescriptor, mapping); }

	@Override
	public Optional<? extends IUIComponentRendererMinecraft> getRenderer() { return getRendererContainer().getRenderer(); }

	@Override
	public void setRenderer(@Nullable IUIComponentRendererMinecraft renderer) { getRendererContainer().setRenderer(renderer); }

	protected IUIComponentRendererContainer<IUIComponentRendererMinecraft> getRendererContainer() { return rendererContainer; }
}
