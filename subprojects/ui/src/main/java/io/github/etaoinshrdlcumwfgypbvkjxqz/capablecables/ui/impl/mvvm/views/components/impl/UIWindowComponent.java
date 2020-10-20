package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventFocus;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIFunctionalEventListener;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.UIDefaultComponentContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIComponentModifyShapeDescriptorBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions.ShapeConstraintSupplier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AutoCloseableRotator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.ConstantSupplier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.LoggingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.reactivex.rxjava3.disposables.Disposable;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.RectangularShape;
import java.util.ConcurrentModificationException;
import java.util.Optional;
import java.util.function.Predicate;

public class UIWindowComponent
		extends UIDefaultComponentContainer
		implements IUIReshapeExplicitly<RectangularShape> {
	// TODO make window scroll bars, maybe create a new component, and embed into this
	// TODO make value not hardcoded through themes
	public static final int WINDOW_DRAG_BAR_THICKNESS = 10; // COMMENT internal top
	public static final int WINDOW_VISIBLE_MINIMUM = 10;

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	private final AutoCloseableRotator<ModifyShapeDescriptorObserver, RuntimeException> modifyShapeDescriptorObserver =
			new AutoCloseableRotator<>(() -> new ModifyShapeDescriptorObserver(this, UIConfiguration.getInstance().getLogger()), Disposable::dispose);

	@SuppressWarnings({"OverridableMethodCallDuringObjectConstruction", "rawtypes", "RedundantSuppression"})
	@UIComponentConstructor
	public UIWindowComponent(UIComponentConstructor.IArguments arguments) {
		super(arguments);

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
							.map(n -> n - getWindowVisibleMinimum())
							.orElse(null),
					() -> selfRef.getOptional()
							.flatMap(IUIComponent::getManager)
							.map(IUIComponentManager::getShapeDescriptor)
							.map(IShapeDescriptor::getShapeOutput)
							.map(Shape::getBounds2D)
							.map(RectangularShape::getMaxY)
							.map(n -> n - getWindowVisibleMinimum())
							.orElse(null),
					ConstantSupplier.of((double) getWindowVisibleMinimum()), ConstantSupplier.of((double) getWindowVisibleMinimum()),
					ConstantSupplier.empty(), ConstantSupplier.empty()));
			return false;
		});

		addEventListener(EnumUIEventDOMType.FOCUS_IN_POST.getEventType(), new UIFunctionalEventListener<IUIEventFocus>(e ->
				getParent().orElseThrow(InternalError::new).moveChildToTop(this)), true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IShapeDescriptor<RectangularShape> getShapeDescriptor() {
		return (IShapeDescriptor<RectangularShape>) super.getShapeDescriptor(); // COMMENT should be safe, see constructor
	}

	public static int getWindowVisibleMinimum() {
		return WINDOW_VISIBLE_MINIMUM;
	}

	@Override
	public void initialize(IUIComponentContext context) {
		UIEventBusEntryPoint.<UIComponentModifyShapeDescriptorBusEvent>getEventBus()
				.subscribe(getModifyShapeDescriptorObserver().get());
		IUIReshapeExplicitly.refresh(this);
	}

	protected AutoCloseableRotator<ModifyShapeDescriptorObserver, RuntimeException> getModifyShapeDescriptorObserver() { return modifyShapeDescriptorObserver; }

	@Override
	public void removed(IUIComponentContext context) {
		getModifyShapeDescriptorObserver().close();
	}

	@Override
	public void transformChildren(AffineTransform transform) {
		super.transformChildren(transform);
		transform.translate(0, getWindowDragBarThickness()); // TODO move
	}

	public static int getWindowDragBarThickness() {
		return WINDOW_DRAG_BAR_THICKNESS;
	}

	@Override
	public boolean reshape(Predicate<? super IShapeDescriptor<? super RectangularShape>> action) throws ConcurrentModificationException {
		return IUIComponent.reshapeComponent(this, getShapeDescriptor(), action);
		// TODO resizing logic
	}

	protected static class ModifyShapeDescriptorObserver
			extends LoggingDisposableObserver<UIComponentModifyShapeDescriptorBusEvent> {
		private final OptionalWeakReference<UIWindowComponent> owner;

		public ModifyShapeDescriptorObserver(UIWindowComponent owner, Logger logger) {
			super(logger);
			this.owner = new OptionalWeakReference<>(owner);
		}

		@Override
		@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
		public void onNext(UIComponentModifyShapeDescriptorBusEvent event) {
			super.onNext(event);
			if (event.getStage().isPost())
				getOwner()
						.filter(owner -> owner.getParent().filter(p -> p.equals(event.getComponent())).isPresent())
						.ifPresent(IUIReshapeExplicitly::refresh);
		}

		protected Optional<? extends UIWindowComponent> getOwner() { return owner.getOptional(); }
	}
}
