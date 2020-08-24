package $group__.ui.minecraft.mvvm.components;

import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.parsers.UIComponentConstructor;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.minecraft.core.mvvm.views.IUIComponentMinecraft;
import $group__.ui.mvvm.views.components.UIComponentManager;
import $group__.utilities.interfaces.INamespacePrefixedString;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class UIComponentManagerMinecraft
		extends UIComponentManager<Rectangle2D>
		implements IUIComponentMinecraft {
	@UIComponentConstructor(type = UIComponentConstructor.ConstructorType.SHAPE_DESCRIPTOR__MAPPING)
	public UIComponentManagerMinecraft(IShapeDescriptor<Rectangle2D> shapeDescriptor, Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping) { super(shapeDescriptor, mapping); }

	@Override
	public void crop(IAffineTransformStack stack, EnumCropMethod method, boolean push, Point2D mouse, double partialTicks) { IUIComponentMinecraft.crop(this, stack, method, push, mouse, partialTicks); }
}
