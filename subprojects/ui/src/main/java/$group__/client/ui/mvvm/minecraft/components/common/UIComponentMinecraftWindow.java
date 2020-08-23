package $group__.client.ui.mvvm.minecraft.components.common;

import $group__.client.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.client.ui.events.bus.EventBusEntryPoint;
import $group__.client.ui.events.ui.UIEventListener;
import $group__.client.ui.mvvm.core.binding.IBindingField;
import $group__.client.ui.mvvm.core.binding.IHasBinding;
import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.structures.IUIPropertyMappingValue;
import $group__.client.ui.mvvm.core.views.IUIReshapeExplicitly;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.components.IUIComponentManager;
import $group__.client.ui.mvvm.core.views.components.parsers.UIConstructor;
import $group__.client.ui.mvvm.core.views.components.parsers.UIProperty;
import $group__.client.ui.mvvm.core.views.events.IUIEventFocus;
import $group__.client.ui.mvvm.minecraft.core.views.IUIComponentMinecraft;
import $group__.client.ui.mvvm.views.components.UIComponentContainer;
import $group__.client.ui.mvvm.views.events.bus.EventUIComponent;
import $group__.client.ui.mvvm.views.events.ui.UIEventFocus;
import $group__.client.ui.structures.shapes.interactions.ShapeConstraintSupplier;
import $group__.client.ui.utilities.BindingUtilities;
import $group__.client.ui.utilities.minecraft.DrawingUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.functions.ConstantSupplier;
import $group__.utilities.reactive.DisposableObserverAuto;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraft.util.ResourceLocation;
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

	public static final ResourceLocation PROPERTY_COLOR_BACKGROUND_LOCATION = new ResourceLocation(PROPERTY_COLOR_BACKGROUND);
	public static final ResourceLocation PROPERTY_COLOR_BORDER_LOCATION = new ResourceLocation(PROPERTY_COLOR_BORDER);

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
	@UIConstructor
	public UIComponentMinecraftWindow(IShapeDescriptor<RectangularShape> shapeDescriptor, Map<ResourceLocation, IUIPropertyMappingValue> propertyMapping) {
		super(shapeDescriptor, propertyMapping);

		this.colorBackground = IHasBinding.createBindingField(Color.class,
				this.propertyMapping.get(PROPERTY_COLOR_BACKGROUND_LOCATION), BindingUtilities.Deserializers::deserializeColor, Color.BLACK);
		this.colorBorder = IHasBinding.createBindingField(Color.class,
				this.propertyMapping.get(PROPERTY_COLOR_BORDER_LOCATION), BindingUtilities.Deserializers::deserializeColor, Color.WHITE);

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
		EventBusEntryPoint.<EventUIComponent.ShapeDescriptorModify>getEventBus()
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
