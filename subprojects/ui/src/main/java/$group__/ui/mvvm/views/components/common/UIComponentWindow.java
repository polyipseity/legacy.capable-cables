package $group__.ui.mvvm.views.components.common;

import $group__.ui.UIConfiguration;
import $group__.ui.core.binding.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.IUIReshapeExplicitly;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.mvvm.views.events.IUIEventFocus;
import $group__.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import $group__.ui.core.parsers.components.UIComponentConstructor;
import $group__.ui.core.structures.IAffineTransformStack;
import $group__.ui.core.structures.IUIComponentContext;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.events.bus.UIEventBusEntryPoint;
import $group__.ui.events.ui.UIEventListener;
import $group__.ui.mvvm.views.components.UIComponentContainer;
import $group__.ui.mvvm.views.events.bus.UIComponentBusEvent;
import $group__.ui.structures.shapes.interactions.ShapeConstraintSupplier;
import $group__.utilities.functions.ConstantSupplier;
import $group__.utilities.reactive.LoggingDisposableObserver;
import $group__.utilities.references.OptionalWeakReference;
import $group__.utilities.structures.INamespacePrefixedString;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.RectangularShape;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class UIComponentWindow
		extends UIComponentContainer
		implements IUIReshapeExplicitly<RectangularShape> {
	// TODO make window scroll bars, maybe create a new component, and embed into this
	// TODO make value not hardcoded through themes
	public static final int
			WINDOW_DRAG_BAR_THICKNESS = 10, // COMMENT internal top
			WINDOW_VISIBLE_MINIMUM = 10;

	protected final AtomicReference<UICBEMSDObserver> observerEventUIShapeDescriptorModify = new AtomicReference<>();

	@SuppressWarnings("unchecked")
	@Override
	public IShapeDescriptor<RectangularShape> getShapeDescriptor() {
		return (IShapeDescriptor<RectangularShape>) super.getShapeDescriptor(); // COMMENT should be safe, see constructor
	}

	@Override
	public boolean reshape(Predicate<? super IShapeDescriptor<? super RectangularShape>> action) throws ConcurrentModificationException {
		return IUIComponent.reshapeComponent(this, getShapeDescriptor(), action);
		// TODO resizing logic
	}

	@SuppressWarnings("OverridableMethodCallDuringObjectConstruction")
	@UIComponentConstructor(type = UIComponentConstructor.EnumConstructorType.MAPPINGS__ID__SHAPE_DESCRIPTOR)
	public UIComponentWindow(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings, @Nullable String id, IShapeDescriptor<RectangularShape> shapeDescriptor) {
		super(mappings, id, shapeDescriptor);

		IShapeDescriptor<?> sd = getShapeDescriptor();
		modifyShape(() -> {
			OptionalWeakReference<IUIComponent> selfRef = new OptionalWeakReference<>(this);
			sd.getConstraintsRef().add(new ShapeConstraintSupplier(
					ConstantSupplier.of(0d), ConstantSupplier.of(0d),
					() -> selfRef.getOptional()
							.flatMap(IUIComponent::getManager)
							.map(IUIComponentManager::getShapeDescriptor)
							.map(IShapeDescriptor::getShapeOutput)
							.map(Shape::getBounds2D)
							.map(RectangularShape::getMaxX)
							.map(n -> n - WINDOW_VISIBLE_MINIMUM)
							.orElse(null),
					() -> selfRef.getOptional()
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

	@Override
	public void initialize(IUIComponentContext context) {
		UIEventBusEntryPoint.<UIComponentBusEvent.ModifyShapeDescriptor>getEventBus()
				.subscribe(getObserverEventUIShapeDescriptorModify().accumulateAndGet(new UICBEMSDObserver(), (p, n) -> {
					if (p != null)
						p.dispose();
					return n;
				}));
		IUIReshapeExplicitly.refresh(this);
	}

	@Override
	public void transformChildren(IAffineTransformStack stack) {
		super.transformChildren(stack);
		stack.element().translate(0, WINDOW_DRAG_BAR_THICKNESS); // TODO move
	}

	protected AtomicReference<UICBEMSDObserver> getObserverEventUIShapeDescriptorModify() { return observerEventUIShapeDescriptorModify; }

	@Override
	public void removed(IUIComponentContext context) { Optional.ofNullable(getObserverEventUIShapeDescriptorModify().getAndSet(null)).ifPresent(DisposableObserver::dispose); }

	public class UICBEMSDObserver
			extends LoggingDisposableObserver<UIComponentBusEvent.ModifyShapeDescriptor> {
		public UICBEMSDObserver() { super(UIConfiguration.getInstance().getLogger()); }

		@Override
		@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
		public void onNext(UIComponentBusEvent.ModifyShapeDescriptor event) {
			super.onNext(event);
			if (event.getStage().isPost() && getParent().filter(p -> p.equals(event.getComponent())).isPresent())
				IUIReshapeExplicitly.refresh(UIComponentWindow.this);
		}
	}
}
