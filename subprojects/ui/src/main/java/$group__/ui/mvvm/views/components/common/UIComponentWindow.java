package $group__.ui.mvvm.views.components.common;

import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.IUIReshapeExplicitly;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.mvvm.views.events.IUIEventFocus;
import $group__.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import $group__.ui.core.parsers.components.UIComponentConstructor;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.events.bus.UIEventBusEntryPoint;
import $group__.ui.events.ui.UIEventListener;
import $group__.ui.mvvm.views.components.UIComponentContainer;
import $group__.ui.mvvm.views.events.bus.EventUIComponent;
import $group__.ui.structures.shapes.interactions.ShapeConstraintSupplier;
import $group__.utilities.functions.ConstantSupplier;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.reactive.DisposableObserverAuto;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.RectangularShape;
import java.lang.ref.WeakReference;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class UIComponentWindow
		extends UIComponentContainer
		implements IUIReshapeExplicitly<RectangularShape> {
	// TODO make window scroll bars, maybe create a new component, and embed into this
	// TODO make value not hardcoded through themes
	public static final int
			WINDOW_DRAG_BAR_THICKNESS = 10, // COMMENT internal top
			WINDOW_VISIBLE_MINIMUM = 10;

	@SuppressWarnings("OverridableMethodCallDuringObjectConstruction")
	@UIComponentConstructor(type = UIComponentConstructor.EnumConstructorType.MAPPINGS__ID__SHAPE_DESCRIPTOR)
	public UIComponentWindow(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings, @Nullable String id, IShapeDescriptor<RectangularShape> shapeDescriptor) {
		super(mappings, id, shapeDescriptor);

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
					ConstantSupplier.empty(), ConstantSupplier.empty()));
			return false;
		});

		addEventListener(EnumUIEventDOMType.FOCUS_IN_POST.getEventType(), new UIEventListener.Functional<IUIEventFocus>(e ->
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

	protected AtomicReference<ObserverEventUIShapeDescriptorModify> getObserverEventUIShapeDescriptorModify() { return observerEventUIShapeDescriptorModify; }

	@Override
	public void transformChildren(IAffineTransformStack stack) {
		super.transformChildren(stack);
		stack.element().translate(0, WINDOW_DRAG_BAR_THICKNESS); // TODO move
	}

	@Override
	public void initialize(IAffineTransformStack stack) {
		UIEventBusEntryPoint.<EventUIComponent.ShapeDescriptorModify>getEventBus()
				.subscribe(getObserverEventUIShapeDescriptorModify().accumulateAndGet(new ObserverEventUIShapeDescriptorModify(), (p, n) -> {
					if (p != null)
						p.dispose();
					return n;
				}));
		IUIReshapeExplicitly.refresh(this);
	}

	@Override
	public void removed(IAffineTransformStack stack) { Optional.ofNullable(getObserverEventUIShapeDescriptorModify().getAndSet(null)).ifPresent(DisposableObserver::dispose); }

	public class ObserverEventUIShapeDescriptorModify
			extends DisposableObserverAuto<EventUIComponent.ShapeDescriptorModify> {
		@Override
		@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
		public void onNext(EventUIComponent.ShapeDescriptorModify event) {
			if (event.getStage().isPost() && getParent().filter(p -> p.equals(event.getComponent())).isPresent())
				IUIReshapeExplicitly.refresh(UIComponentWindow.this);
		}
	}
}
