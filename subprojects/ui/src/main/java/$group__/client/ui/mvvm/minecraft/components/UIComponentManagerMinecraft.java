package $group__.client.ui.mvvm.minecraft.components;

import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.structures.IShapeDescriptor;
import $group__.client.ui.mvvm.core.structures.IUIPropertyMappingValue;
import $group__.client.ui.mvvm.core.views.components.parsers.UIConstructor;
import $group__.client.ui.mvvm.minecraft.core.views.IUIComponentMinecraft;
import $group__.client.ui.mvvm.structures.ShapeDescriptor;
import $group__.client.ui.mvvm.structures.UIAnchorSet;
import $group__.client.ui.mvvm.views.components.UIComponentManager;
import $group__.utilities.functions.IFunction4;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class UIComponentManagerMinecraft
		extends UIComponentManager<IShapeDescriptor<? extends Rectangle2D>>
		implements IUIComponentMinecraft {
	protected final List<IFunction4<? super IAffineTransformStack, ? super Point2D, ? super Double, ? super Boolean, ? extends Boolean>> scheduledActions = new LinkedList<>();

	@UIConstructor
	public UIComponentManagerMinecraft(Map<String, IUIPropertyMappingValue> propertyMapping) { super(propertyMapping); }

	@Override
	protected IShapeDescriptor<? extends Rectangle2D> createShapeDescriptor() { return new ShapeDescriptor.Rectangular<>(getShapePlaceholderView(), new UIAnchorSet<>(this::getShapeDescriptor)); }

	@Override
	public void render(final IAffineTransformStack stack, Point2D cursorPosition, double partialTicks, boolean pre) { getScheduledActions().removeIf(a -> a.apply(stack, cursorPosition, partialTicks, pre)); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IFunction4<? super IAffineTransformStack, ? super Point2D, ? super Double, ? super Boolean, ? extends Boolean>> getScheduledActions() { return scheduledActions; }

	@Override
	public void schedule(IFunction4<? super IAffineTransformStack, ? super Point2D, ? super Double, ? super Boolean, ? extends Boolean> action) { getScheduledActions().add(action); }

	@Override
	public void crop(final IAffineTransformStack stack, EnumCropMethod method, boolean push, Point2D mouse, double partialTicks) {}
}
