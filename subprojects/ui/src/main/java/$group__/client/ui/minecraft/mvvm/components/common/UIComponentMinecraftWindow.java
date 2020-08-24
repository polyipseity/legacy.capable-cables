package $group__.client.ui.minecraft.mvvm.components.common;

import $group__.client.ui.core.mvvm.binding.IBindingField;
import $group__.client.ui.core.mvvm.binding.IHasBinding;
import $group__.client.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.client.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.client.ui.core.mvvm.views.IUIReshapeExplicitly;
import $group__.client.ui.core.mvvm.views.components.IUIComponent;
import $group__.client.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.client.ui.core.mvvm.views.components.parsers.UIComponentConstructor;
import $group__.client.ui.core.mvvm.views.components.parsers.UIProperty;
import $group__.client.ui.core.mvvm.views.events.IUIEventFocus;
import $group__.client.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.client.ui.events.bus.UIEventBusEntryPoint;
import $group__.client.ui.events.ui.UIEventListener;
import $group__.client.ui.minecraft.core.mvvm.views.IUIComponentMinecraft;
import $group__.client.ui.mvvm.views.components.UIComponentContainer;
import $group__.client.ui.mvvm.views.events.bus.EventUIComponent;
import $group__.client.ui.mvvm.views.events.ui.UIEventFocus;
import $group__.client.ui.structures.shapes.interactions.ShapeConstraintSupplier;
import $group__.client.ui.utilities.BindingUtilities;
import $group__.client.ui.utilities.minecraft.DrawingUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.functions.ConstantSupplier;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.reactive.DisposableObserverAuto;
import $group__.utilities.structures.NamespacePrefixedString;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.lang.ref.WeakReference;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

// TODO responsibility of this classes may need to be delegated to the view model via some means
@OnlyIn(Dist.CLIENT)
public class UIComponentMinecraftWindow
		extends UIComponentContainer
		implements IUIReshapeExplicitly<RectangularShape>, IUIComponentMinecraft {
	public static final String PROPERTY_COLOR_BACKGROUND = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "window.colors.background";
	public static final String PROPERTY_COLOR_BORDER = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "window.colors.border";

	public static final INamespacePrefixedString PROPERTY_COLOR_BACKGROUND_LOCATION = new NamespacePrefixedString(PROPERTY_COLOR_BACKGROUND);
	public static final INamespacePrefixedString PROPERTY_COLOR_BORDER_LOCATION = new NamespacePrefixedString(PROPERTY_COLOR_BORDER);

	// TODO make window scroll bars, maybe create a new component, and embed into this
	// TODO make value not hardcoded through themes
	public static final int
			WINDOW_DRAG_BAR_THICKNESS = 10, // COMMENT internal top
			WINDOW_VISIBLE_MINIMUM = 10;

	@UIProperty(PROPERTY_COLOR_BACKGROUND)
	protected final IBindingField<Color> colorBackground;
	@UIProperty(PROPERTY_COLOR_BORDER)
	protected final IBindingField<Color> colorBorder;

	@SuppressWarnings("OverridableMethodCallDuringObjectConstruction")
	@UIComponentConstructor(type = UIComponentConstructor.ConstructorType.SHAPE_DESCRIPTOR__MAPPING)
	public UIComponentMinecraftWindow(IShapeDescriptor<RectangularShape> shapeDescriptor, Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping) {
		super(shapeDescriptor, mapping);

		this.colorBackground = IHasBinding.createBindingField(Color.class,
				this.mapping.get(PROPERTY_COLOR_BACKGROUND_LOCATION), BindingUtilities.Deserializers::deserializeColor, Color.BLACK);
		this.colorBorder = IHasBinding.createBindingField(Color.class,
				this.mapping.get(PROPERTY_COLOR_BORDER_LOCATION), BindingUtilities.Deserializers::deserializeColor, Color.WHITE);

		IShapeDescriptor<?> sd = getShapeDescriptor();
		modifyShape(() -> {
			WeakReference<IUIComponent> selfRef = new WeakReference<>(this);
			sd.getConstraintsRef().add(new ShapeConstraintSupplier(
					ConstantSupplier.of(0d), ConstantSupplier.of(0d),
					() -> Optional.ofNullable(selfRef.get())
							.flatMap(IUIComponent::getManager)
							.map(IUIComponentManager::getShapeDescriptor)
							.map(IShapeDescriptor::getShapeOutput)
							.map(Shape::getBounds2D)
							.map(RectangularShape::getMaxX)
							.map(n -> n - WINDOW_VISIBLE_MINIMUM)
							.orElse(null),
					() -> Optional.ofNullable(selfRef.get())
							.flatMap(IUIComponent::getManager)
							.map(IUIComponentManager::getShapeDescriptor)
							.map(IShapeDescriptor::getShapeOutput)
							.map(Shape::getBounds2D)
							.map(RectangularShape::getMaxY)
							.map(n -> n - WINDOW_VISIBLE_MINIMUM)
							.orElse(null),
					ConstantSupplier.of((double) WINDOW_VISIBLE_MINIMUM), ConstantSupplier.of((double) WINDOW_VISIBLE_MINIMUM),
					ConstantSupplier.getNullSupplier(), ConstantSupplier.getNullSupplier()));
			return false;
		});

		addEventListener(UIEventFocus.TYPE_FOCUS_IN_POST, new UIEventListener.Functional<IUIEventFocus>(e ->
				getParent().orElseThrow(InternalError::new).moveChildToTop(this)), true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IShapeDescriptor<RectangularShape> getShapeDescriptor() {
		return (IShapeDescriptor<RectangularShape>) super.getShapeDescriptor(); // COMMENT should be safe, see constructor
	}

	@Override
	public boolean reshape(Function<? super IShapeDescriptor<? super RectangularShape>, ? extends Boolean> action) throws ConcurrentModificationException {
		return IUIComponent.reshapeComponent(this, getShapeDescriptor(), action);
		// TODO resizing logic
	}

	protected final AtomicReference<ObserverEventUIShapeDescriptorModify> observerEventUIShapeDescriptorModify = new AtomicReference<>();

	@Override
	public void initialize(IAffineTransformStack stack) {
		UIEventBusEntryPoint.<EventUIComponent.ShapeDescriptorModify>getEventBus()
				.subscribe(getObserverEventUIShapeDescriptorModify().accumulateAndGet(new ObserverEventUIShapeDescriptorModify(), (p, n) -> {
					Optional.ofNullable(p).ifPresent(DisposableObserver::dispose);
					return n;
				}));
		IUIReshapeExplicitly.refresh(this);
	}

	protected AtomicReference<ObserverEventUIShapeDescriptorModify> getObserverEventUIShapeDescriptorModify() { return observerEventUIShapeDescriptorModify; }

	@Override
	public void render(IAffineTransformStack stack, Point2D cursorPosition, double partialTicks, boolean pre) {
		AffineTransform transform = stack.getDelegated().peek();
		if (pre) {
			DrawingUtilities.drawShape(transform, getShapeDescriptor().getShapeOutput(), true, getColorBackground().getValue(), 0);
			DrawingUtilities.drawShape(transform, getShapeDescriptor().getShapeOutput(), true, getColorBorder().getValue(), 0);
		}
	}

	public IBindingField<Color> getColorBackground() { return colorBackground; }

	public IBindingField<Color> getColorBorder() { return colorBorder; }

	@Override
	public void crop(IAffineTransformStack stack, EnumCropMethod method, boolean push, Point2D mouse, double partialTicks) { IUIComponentMinecraft.crop(this, stack, method, push, mouse, partialTicks); }

	@Override
	public void removed(IAffineTransformStack stack) { Optional.ofNullable(getObserverEventUIShapeDescriptorModify().getAndSet(null)).ifPresent(DisposableObserver::dispose); }

	@Override
	public void transformChildren(IAffineTransformStack stack) {
		super.transformChildren(stack);
		stack.getDelegated().peek().translate(0, WINDOW_DRAG_BAR_THICKNESS); // TODO move
	}

	protected class ObserverEventUIShapeDescriptorModify
			extends DisposableObserverAuto<EventUIComponent.ShapeDescriptorModify> {
		@Override
		@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
		public void onNext(EventUIComponent.ShapeDescriptorModify event) {
			if (event.getStage() == EnumEventHookStage.POST && getParent().filter(p -> p.equals(event.getComponent())).isPresent())
				IUIReshapeExplicitly.refresh(UIComponentMinecraftWindow.this);
		}
	}
}
