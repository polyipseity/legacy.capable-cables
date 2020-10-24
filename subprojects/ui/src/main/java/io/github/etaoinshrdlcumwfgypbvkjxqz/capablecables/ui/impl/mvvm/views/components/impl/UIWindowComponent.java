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
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions.ShapeConstraintSupplier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISideType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AutoCloseableRotator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.LoggingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ConstantValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields.DelegatingBindingField;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class UIWindowComponent
		extends UIDefaultComponentContainer
		implements IUIReshapeExplicitly<RectangularShape> {
	// TODO make window scroll bars, maybe create a new component, and embed into this

	public static final @NonNls String PROPERTY_CONTROL_BAR_SIDE = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.window.control_bar.side";
	public static final @NonNls String PROPERTY_CONTROL_BAR_THICKNESS = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.window.control_bar.thickness";
	public static final @NonNls String PROPERTY_MINIMUM_VISIBLE_THICKNESS = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.window.minimum_visible_thickness";
	private static final INamespacePrefixedString PROPERTY_CONTROL_BAR_SIDE_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyControlBarSide());
	private static final INamespacePrefixedString PROPERTY_CONTROL_BAR_THICKNESS_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyControlBarThickness());
	private static final INamespacePrefixedString PROPERTY_MINIMUM_VISIBLE_THICKNESS_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyMinimumVisibleThickness());
	@UIProperty(PROPERTY_CONTROL_BAR_SIDE)
	private final IBindingField<EnumUISide> controlBarSide;
	@UIProperty(PROPERTY_CONTROL_BAR_THICKNESS)
	private final IBindingField<Double> controlBarThickness;
	@UIProperty(PROPERTY_MINIMUM_VISIBLE_THICKNESS)
	private final IBindingField<Double> minimumVisibleThickness;

	@SuppressWarnings({"OverridableMethodCallDuringObjectConstruction", "rawtypes", "RedundantSuppression", "ThisEscapedInObjectConstruction"})
	@UIComponentConstructor
	public UIWindowComponent(UIComponentConstructor.IArguments arguments) {
		super(arguments);

		@SuppressWarnings("ThisEscapedInObjectConstruction") OptionalWeakReference<UIWindowComponent> weakThis = new OptionalWeakReference<>(this);
		IShapeDescriptor<?> sd = getShapeDescriptor();
		modifyShape(() -> {
			sd.getConstraintsRef().add(new ShapeConstraintSupplier(
					ConstantValue.of(0D), ConstantValue.of(0D),
					() -> weakThis.getOptional()
							.flatMap(thisObj -> thisObj.getManager()
									.map(IUIComponent::getShape)
									.map(Shape::getBounds2D)
									.map(RectangularShape::getMaxX)
									.map(n -> n - thisObj.getMinimumVisibleThickness().getValue()
											.orElseThrow(IllegalStateException::new)))
							.orElse(null),
					() -> weakThis.getOptional()
							.flatMap(thisObj -> thisObj.getManager()
									.map(IUIComponent::getShape)
									.map(Shape::getBounds2D)
									.map(RectangularShape::getMaxY)
									.map(n -> n - thisObj.getMinimumVisibleThickness().getValue()
											.orElseThrow(IllegalStateException::new)))
							.orElse(null),
					() -> weakThis.getOptional()
							.map(UIWindowComponent::getMinimumVisibleThickness)
							.flatMap(IBindingField::getValue)
							.orElse(null),
					() -> weakThis.getOptional()
							.map(UIWindowComponent::getMinimumVisibleThickness)
							.flatMap(IBindingField::getValue)
							.orElse(null),
					ConstantValue.getEmpty(), ConstantValue.getEmpty()));
			return false;
		});

		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.controlBarSide = IUIPropertyMappingValue.createBindingField(EnumUISide.class, true,
				EnumUISide.UP, mappings.get(getPropertyControlBarSideLocation()));
		this.controlBarThickness = IUIPropertyMappingValue.createBindingField(Double.class, false,
				10D, mappings.get(getPropertyControlBarThicknessLocation()));
		this.minimumVisibleThickness = new MinimumVisibleThicknessBindingField(this,
				IUIPropertyMappingValue.createBindingField(Double.class, false,
						10D, mappings.get(UIWindowComponent.getPropertyMinimumVisibleThicknessLocation())));

		addEventListener(EnumUIEventDOMType.FOCUS_IN_POST.getEventType(), new UIFunctionalEventListener<IUIEventFocus>(e ->
				getParent().orElseThrow(InternalError::new).moveChildToTop(this)), true);
	}

	protected IBindingField<Double> getMinimumVisibleThickness() {
		return minimumVisibleThickness;
	}

	public static INamespacePrefixedString getPropertyControlBarSideLocation() {
		return PROPERTY_CONTROL_BAR_SIDE_LOCATION;
	}

	public static INamespacePrefixedString getPropertyControlBarThicknessLocation() {
		return PROPERTY_CONTROL_BAR_THICKNESS_LOCATION;
	}

	public static INamespacePrefixedString getPropertyMinimumVisibleThicknessLocation() {
		return PROPERTY_MINIMUM_VISIBLE_THICKNESS_LOCATION;
	}

	public static String getPropertyMinimumVisibleThickness() {
		return PROPERTY_MINIMUM_VISIBLE_THICKNESS;
	}

	public static String getPropertyControlBarSide() {
		return PROPERTY_CONTROL_BAR_SIDE;
	}

	public static String getPropertyControlBarThickness() {
		return PROPERTY_CONTROL_BAR_THICKNESS;
	}

	@Override
	public void transformChildren(AffineTransform transform) {
		super.transformChildren(transform);
		Point2D translation = getControlBarSide().getValue()
				.filter(side -> side.getType() == EnumUISideType.LOCATION)
				.map(side -> {
					// COMMENT variable-capture-less
					Point2D translation2 = new Point2D.Double();
					side.getAxis().setCoordinate(translation2, IField.getValueNonnull(getControlBarThickness()));
					return translation2;
				})
				.orElseGet(Point2D.Double::new);
		transform.translate(translation.getX(), translation.getY());
	}

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	private final AutoCloseableRotator<ModifyShapeDescriptorObserver, RuntimeException> modifyShapeDescriptorObserver =
			new AutoCloseableRotator<>(() -> new ModifyShapeDescriptorObserver(this, UIConfiguration.getInstance().getLogger()), Disposable::dispose);

	protected IBindingField<EnumUISide> getControlBarSide() {
		return controlBarSide;
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

	protected IBindingField<Double> getControlBarThickness() {
		return controlBarThickness;
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
						getControlBarSide(), getControlBarThickness(),
						getMinimumVisibleThickness()
				));
	}

	@Override
	public void cleanupBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.cleanupBindings(binderObserverSupplier);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
				() -> ImmutableBinderAction.unbind(
						getControlBarSide(), getControlBarThickness(),
						getMinimumVisibleThickness()
				));
	}

	protected static class MinimumVisibleThicknessBindingField extends DelegatingBindingField<Double> {
		private final OptionalWeakReference<UIWindowComponent> owner;

		public MinimumVisibleThicknessBindingField(UIWindowComponent owner, IBindingField<Double> delegate) {
			super(delegate);
			this.owner = new OptionalWeakReference<>(owner);
		}

		@Override
		public void setValue(@Nullable Double value) {
			super.setValue(value);
			getOwner().ifPresent(IUIReshapeExplicitly::refresh);
		}

		public Optional<? extends UIWindowComponent> getOwner() {
			return owner.getOptional();
		}
	}
}
