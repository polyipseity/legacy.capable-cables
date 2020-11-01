package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventFocus;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIFunctionalEventListener;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.UIDefaultComponentContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIComponentModifyShapeDescriptorBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUIRotation;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISideType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AutoCloseableRotator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.LoggingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBinderAction;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Logger;

import java.awt.*;
import java.awt.geom.*;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class UIWindowComponent
		extends UIDefaultComponentContainer
		implements IUIReshapeExplicitly<RectangularShape> {
	// TODO make window scroll bars, maybe create a new component, and embed into this

	public static final @NonNls String PROPERTY_CONTROLS_SIDE = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.window.controls.side";
	public static final @NonNls String PROPERTY_CONTROLS_THICKNESS = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.window.controls.thickness";
	public static final @NonNls String PROPERTY_CONTROLS_DIRECTION = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.window.controls.direction";
	private static final INamespacePrefixedString PROPERTY_CONTROLS_SIDE_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyControlsSide());
	private static final INamespacePrefixedString PROPERTY_CONTROLS_THICKNESS_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyControlsThickness());
	private static final INamespacePrefixedString PROPERTY_CONTROLS_DIRECTION_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyControlsDirection());

	@UIProperty(PROPERTY_CONTROLS_SIDE)
	private final IBindingField<EnumUISide> controlsSide;
	@UIProperty(PROPERTY_CONTROLS_THICKNESS)
	private final IBindingField<Double> controlsThickness;
	@UIProperty(PROPERTY_CONTROLS_DIRECTION)
	private final IBindingField<EnumUIRotation> controlsDirection;

	@SuppressWarnings({"OverridableMethodCallDuringObjectConstruction", "rawtypes", "RedundantSuppression", "ThisEscapedInObjectConstruction"})
	@UIComponentConstructor
	public UIWindowComponent(UIComponentConstructor.IArguments arguments) {
		super(arguments);

		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.controlsSide = IUIPropertyMappingValue.createBindingField(EnumUISide.class, EnumUISide.UP,
				mappings.get(getPropertyControlsSideLocation()));
		this.controlsThickness = IUIPropertyMappingValue.createBindingField(Double.class, 10D,
				mappings.get(getPropertyControlsThicknessLocation()));
		this.controlsDirection = IUIPropertyMappingValue.createBindingField(EnumUIRotation.class, EnumUIRotation.CLOCKWISE,
				mappings.get(getPropertyControlsDirectionLocation()));

		addEventListener(EnumUIEventDOMType.FOCUS_IN_POST.getEventType(), new UIFunctionalEventListener<IUIEventFocus>(e ->
				getParent().orElseThrow(InternalError::new).moveChildToTop(this)), true);
	}

	public static INamespacePrefixedString getPropertyControlsSideLocation() {
		return PROPERTY_CONTROLS_SIDE_LOCATION;
	}

	public static INamespacePrefixedString getPropertyControlsThicknessLocation() {
		return PROPERTY_CONTROLS_THICKNESS_LOCATION;
	}

	public static INamespacePrefixedString getPropertyControlsDirectionLocation() {
		return PROPERTY_CONTROLS_DIRECTION_LOCATION;
	}

	public static String getPropertyControlsDirection() {
		return PROPERTY_CONTROLS_DIRECTION;
	}

	public static String getPropertyControlsSide() {
		return PROPERTY_CONTROLS_SIDE;
	}

	public static String getPropertyControlsThickness() {
		return PROPERTY_CONTROLS_THICKNESS;
	}

	@Override
	public void transformChildren(AffineTransform transform) {
		super.transformChildren(transform);
		Point2D translation = new Point2D.Double();

		EnumUISide controlsSide = getControlsSide().getValue();
		if (controlsSide.getType() == EnumUISideType.LOCATION)
			controlsSide.getAxis().setCoordinate(translation, getControlsThickness().getValue());

		transform.translate(translation.getX(), translation.getY());
	}

	protected IBindingField<EnumUISide> getControlsSide() {
		return controlsSide;
	}

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	private final AutoCloseableRotator<ModifyShapeDescriptorObserver, RuntimeException> modifyShapeDescriptorObserver =
			new AutoCloseableRotator<>(() -> new ModifyShapeDescriptorObserver(this, UIConfiguration.getInstance().getLogger()), Disposable::dispose);

	protected IBindingField<Double> getControlsThickness() {
		return controlsThickness;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IShapeDescriptor<RectangularShape> getShapeDescriptor() {
		return (IShapeDescriptor<RectangularShape>) super.getShapeDescriptor(); // COMMENT should be safe, see constructor
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
	public void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.initializeBindings(binderObserverSupplier);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
				() -> ImmutableBinderAction.bind(
						getControlsSide(), getControlsThickness(), getControlsDirection()
				));
	}

	@Override
	public boolean reshape(Predicate<? super IShapeDescriptor<? super RectangularShape>> action) throws ConcurrentModificationException {
		return IUIComponent.reshapeComponent(this, getShapeDescriptor(), action);
		// TODO resizing based on min size and preferred size
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

	@Override
	public void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.initializeBindings(binderObserverSupplier);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
				() -> ImmutableBinderAction.bind(
						getControlBarSide(), getControlBarThickness()
				));
	}

	@Override
	public void cleanupBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.cleanupBindings(binderObserverSupplier);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
				() -> ImmutableBinderAction.unbind(
						getControlBarSide(), getControlBarThickness()
				));
	}
}
