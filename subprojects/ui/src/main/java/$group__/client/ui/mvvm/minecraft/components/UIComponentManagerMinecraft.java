package $group__.client.ui.mvvm.minecraft.components;

import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.structures.IShapeDescriptor;
import $group__.client.ui.mvvm.core.structures.IUIPropertyMappingValue;
import $group__.client.ui.mvvm.core.views.components.parsers.UIConstructor;
import $group__.client.ui.mvvm.minecraft.core.views.IUIComponentMinecraft;
import $group__.client.ui.mvvm.structures.ShapeDescriptor;
import $group__.client.ui.mvvm.views.components.UIComponentManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class UIComponentManagerMinecraft
		extends UIComponentManager<IShapeDescriptor<? extends Rectangle2D>>
		implements IUIComponentMinecraft {
	@UIConstructor
	public UIComponentManagerMinecraft(Map<String, IUIPropertyMappingValue> propertyMapping) { super(propertyMapping); }

	@Override
	protected IShapeDescriptor<? extends Rectangle2D> createShapeDescriptor() { return new ShapeDescriptor.Rectangular<>(getShapePlaceholderView()); }

	@Override
	public void crop(IAffineTransformStack stack, EnumCropMethod method, boolean push, Point2D mouse, double partialTicks) {}
}
