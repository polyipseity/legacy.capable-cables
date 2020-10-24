package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.resizable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.cursors.ICursor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserResizableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentCursorHandleProviderModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventMouse;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRendererInvokerModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIExtensionConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.cursors.EnumGLFWCursor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIFunctionalEventListener;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.modifiers.UIAbstractVirtualComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.AbstractContainerAwareExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IInputPointerDevice;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl.Optional2;
import io.reactivex.rxjava3.observers.DisposableObserver;
import org.jetbrains.annotations.NonNls;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class UITeleportingComponentUserResizableExtension<C extends IUIComponent & IUIReshapeExplicitly<? extends IShapeDescriptor<? extends RectangularShape>>>
		extends AbstractContainerAwareExtension<INamespacePrefixedString, IUIComponent, C>
		implements IUIComponentUserResizableExtension<C> {
	@NonNls
	public static final String PROPERTY_ACTIVATION_MOUSE_BUTTONS = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.teleporting.activation.mouse";
	@NonNls
	public static final String PROPERTY_RESIZE_BORDERS_DEFAULT_THICKNESS = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.teleporting.resize_borders.default_thickness";
	@NonNls
	public static final String PROPERTY_RESIZE_BORDERS = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.teleporting.resize_borders";
	private static final INamespacePrefixedString PROPERTY_ACTIVATION_MOUSE_BUTTONS_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyActivationMouseButtons());
	private static final INamespacePrefixedString PROPERTY_RESIZE_BORDERS_DEFAULT_THICKNESS_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyResizeBordersDefaultThickness());
	private static final INamespacePrefixedString PROPERTY_RESIZE_BORDERS_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyResizeBorders());
	@UIProperty(PROPERTY_ACTIVATION_MOUSE_BUTTONS)
	private final IBindingField<Set<Integer>> activationMouseButtons;
	@UIProperty(PROPERTY_RESIZE_BORDERS)
	private final IBindingField<Double> resizeBorderDefaultThickness;
	@UIProperty(PROPERTY_RESIZE_BORDERS_DEFAULT_THICKNESS)
	private final IBindingField<Map<EnumUISide, Double>> resizeBorders;

	@SuppressWarnings("unchecked")
	@UIExtensionConstructor
	public UITeleportingComponentUserResizableExtension(UIExtensionConstructor.IArguments arguments) {
		super((Class<C>) arguments.getContainerClass());

		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.activationMouseButtons = IUIPropertyMappingValue.<Set<Integer>>createBindingField(CastUtilities.castUnchecked(Set.class), false,
				() -> ImmutableSet.of(GLFW.GLFW_MOUSE_BUTTON_LEFT),
				mappings.get(getPropertyActivationMouseButtonsLocation()));
		this.resizeBorderDefaultThickness = IUIPropertyMappingValue.createBindingField(Double.class, false, 10D,
				mappings.get(getPropertyResizeBordersDefaultThicknessLocation()));
		this.resizeBorders = IUIPropertyMappingValue.<Map<EnumUISide, Double>>createBindingField(CastUtilities.castUnchecked(Map.class), false,
				ImmutableMap::of,
				mappings.get(getPropertyResizeBordersLocation()));
	}

	public static INamespacePrefixedString getPropertyActivationMouseButtonsLocation() {
		return PROPERTY_ACTIVATION_MOUSE_BUTTONS_LOCATION;
	}

	public static INamespacePrefixedString getPropertyResizeBordersDefaultThicknessLocation() {
		return PROPERTY_RESIZE_BORDERS_DEFAULT_THICKNESS_LOCATION;
	}

	public static INamespacePrefixedString getPropertyResizeBordersLocation() {
		return PROPERTY_RESIZE_BORDERS_LOCATION;
	}

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	private final Modifier modifier = new Modifier(this);
	private final AtomicReference<IUIRendererContainer<IResizingRenderer>> rendererContainerReference = new AtomicReference<>();
	@Nullable
	private IResizeData resizeData;
	@Nullable
	private Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier;

	public static String getPropertyActivationMouseButtons() {
		return PROPERTY_ACTIVATION_MOUSE_BUTTONS;
	}

	public static String getPropertyResizeBordersDefaultThickness() {
		return PROPERTY_RESIZE_BORDERS_DEFAULT_THICKNESS;
	}

	public static String getPropertyResizeBorders() {
		return PROPERTY_RESIZE_BORDERS;
	}

	@Override
	public Optional<? extends Shape> getResizeShape() {
		return getContainer()
				.map(IUIComponent::getShape)
				.map(Shape::getBounds2D)
				.map(containerShapeBounds -> {
					double defaultThickness = IField.getValueNonnull(getResizeBorderDefaultThickness());
					Map<EnumUISide, Double> borders = IField.getValueNonnull(getResizeBorders());
					Area resizeShape = new Area(
							EnumUISide.getEdges().stream().unordered()
									.map(side -> {
										EnumUISide oppositeSide = side.getOpposite().orElseThrow(AssertionError::new);
										Rectangle2D border = (Rectangle2D) containerShapeBounds.clone();

										// COMMENT we drag the opposite side to a given offset from our side, ignoring other sides
										oppositeSide.getSetter().accept(border,
												side.getInwardOperator().applyAsDouble(side.getGetter().applyAsDouble(border),
														-borders.getOrDefault(side, defaultThickness)));

										return border;
									})
									.reduce(containerShapeBounds, Rectangle2D::createUnion)
					);
					resizeShape.subtract(new Area(containerShapeBounds));
					return resizeShape;
				});
	}

	protected IBindingField<Double> getResizeBorderDefaultThickness() {
		return resizeBorderDefaultThickness;
	}

	protected IBindingField<Set<Integer>> getActivationMouseButtons() {
		return activationMouseButtons;
	}

	protected static Optional<ICursor> getCursor(Set<? extends EnumUISide> sides) {
		@Nullable ICursor cursor = null;
		if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.LEFT)
				|| sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.RIGHT))
			cursor = EnumGLFWCursor.EXTENSION_RESIZE_NW_SE_CURSOR;
		else if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.RIGHT)
				|| sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.LEFT))
			cursor = EnumGLFWCursor.EXTENSION_RESIZE_NE_SW_CURSOR;
		else if (sides.contains(EnumUISide.LEFT) || sides.contains(EnumUISide.RIGHT))
			cursor = EnumGLFWCursor.STANDARD_RESIZE_HORIZONTAL_CURSOR;
		else if (sides.contains(EnumUISide.UP) || sides.contains(EnumUISide.DOWN))
			cursor = EnumGLFWCursor.STANDARD_RESIZE_VERTICAL_CURSOR;
		return Optional.ofNullable(cursor);
	}

	@Override
	public IUIRendererContainer<IResizingRenderer> getRendererContainer()
			throws IllegalStateException { return Optional.ofNullable(getRendererContainerReference().get()).orElseThrow(IllegalStateException::new); }

	@Override
	public void initializeRendererContainer(@NonNls CharSequence name)
			throws IllegalStateException {
		IUIRendererContainer<IResizingRenderer> rendererContainer = new UIDefaultRendererContainer<>(name, this, CastUtilities.castUnchecked(UIComponentUserResizeableExtensionEmptyResizingRenderer.class));
		if (!getRendererContainerReference().compareAndSet(null, rendererContainer))
			throw new IllegalStateException();
		getBinderObserverSupplier().ifPresent(rendererContainer::initializeBindings);
	}

	protected Optional<? extends Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>>> getBinderObserverSupplier() { return Optional.ofNullable(binderObserverSupplier); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		IUIComponentUserResizableExtension.super.initializeBindings(binderObserverSupplier);
		setBinderObserverSupplier(binderObserverSupplier);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
				() -> ImmutableBinderAction.bind(
						getActivationMouseButtons(),
						getResizeBorders(), getResizeBorderDefaultThickness()
				));
		BindingUtilities.initializeBindings(
				Optional.ofNullable(getRendererContainerReference().get()).map(ImmutableList::of).orElseGet(ImmutableList::of),
				binderObserverSupplier
		);
	}

	protected AtomicReference<IUIRendererContainer<IResizingRenderer>> getRendererContainerReference() { return rendererContainerReference; }

	protected void setBinderObserverSupplier(@Nullable Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) { this.binderObserverSupplier = binderObserverSupplier; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		IUIComponentUserResizableExtension.super.cleanupBindings(binderObserverSupplier);
		setBinderObserverSupplier(null);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
				() -> ImmutableBinderAction.unbind(
						getActivationMouseButtons(),
						getResizeBorders(), getResizeBorderDefaultThickness()
				));
		BindingUtilities.cleanupBindings(
				Optional.ofNullable(getRendererContainerReference().get()).map(ImmutableList::of).orElseGet(ImmutableList::of),
				binderObserverSupplier
		);
	}

	@Override
	public IExtensionType<INamespacePrefixedString, ?, IUIComponent> getType() { return StaticHolder.getType().getValue(); }

	@Override
	@OverridingMethodsMustInvokeSuper
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	public void onExtensionAdded(IUIComponent container) {
		super.onExtensionAdded(container);
		getContainer().ifPresent(c -> c.addModifier(getModifier()));
	}

	protected Modifier getModifier() { return modifier; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionRemoved() {
		super.onExtensionRemoved();
		getContainer()
				.ifPresent(c -> c.removeModifier(getModifier()));
	}

	protected IBindingField<Map<EnumUISide, Double>> getResizeBorders() {
		return resizeBorders;
	}

	@Override
	public Optional<? extends IResizeData> getResizeData() { return Optional.ofNullable(resizeData); }

	protected void setResizeData(@Nullable IResizeData resizeData) { this.resizeData = resizeData; }

	public static class Modifier
			extends UIAbstractVirtualComponent
			implements IUIComponentCursorHandleProviderModifier, IUIComponentRendererInvokerModifier {
		private final OptionalWeakReference<UITeleportingComponentUserResizableExtension<?>> owner;
		private final Object lockObject = new Object();
		private boolean beingHovered = false;

		@Nullable
		private Integer activeMouseButton;

		@SuppressWarnings({"OverridableMethodCallDuringObjectConstruction", "rawtypes", "RedundantSuppression"})
		protected Modifier(UITeleportingComponentUserResizableExtension<?> owner) {
			super(IShapeDescriptor.StaticHolder.getShapeDescriptorPlaceholder());
			this.owner = new OptionalWeakReference<>(owner);

			addEventListener(EnumUIEventDOMType.MOUSE_ENTER.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(evt -> setBeingHovered(true)), false);
			addEventListener(EnumUIEventDOMType.MOUSE_LEAVE.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(evt -> setBeingHovered(false)), false);
			addEventListener(EnumUIEventDOMType.MOUSE_DOWN.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(evt -> {
				if (!getActiveMouseButton().isPresent()) {
					int button = evt.getData().getButton();
					getOwner()
							.filter(owner2 -> IField.getValueNonnull(owner2.getActivationMouseButtons()).contains(button))
							.filter(owner2 -> startResizeMaybe(evt.getViewContext(), evt.getData().getCursorPositionView()))
							.flatMap(owner2 -> owner2.getContainer()) // TODO Java 9 - IllegalAccessError now, make method ref
							.ifPresent(c -> {
								c.getParent().ifPresent(p ->
										p.moveChildToTop(c));
								setActiveMouseButton(button);
								evt.stopPropagation();
							});
				}
			}), false);
			addEventListener(EnumUIEventDOMType.MOUSE_UP.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(evt -> {
				if (getActiveMouseButton().filter(Predicate.isEqual(evt.getData().getButton())).isPresent()
						&& finishResizeMaybe(evt.getViewContext(), evt.getData().getCursorPositionView())) {
					evt.stopPropagation();
					setActiveMouseButton(null);
				}
			}), false);
		}

		protected Optional<Integer> getActiveMouseButton() {
			return Optional.ofNullable(activeMouseButton);
		}

		protected void setActiveMouseButton(@Nullable Integer activeMouseButton) {
			this.activeMouseButton = activeMouseButton;
		}

		@SuppressWarnings({"rawtypes", "RedundantSuppression"})
		protected boolean startResizeMaybe(@SuppressWarnings("unused") IUIViewContext viewContext, Point2D point) {
			return getOwner().flatMap(owner ->
					owner.getContainer().flatMap(container -> container.getManager()
							.flatMap(IUIComponentManager::getView)
							.flatMap(view -> IUIViewComponent.createComponentContextWithManager(view)
									.map(context -> {
										try (IUIComponentContext ctx = context) {
											view.getPathResolver().resolvePath(ctx, (Point2D) point.clone());

											Rectangle2D contextualShape = IUIComponent.getContextualShape(ctx, container).getBounds2D();
											Set<EnumUISide> sides = EnumUISide.getSidesMouseOver(contextualShape, point);

											@Nullable Point2D base = null;
											if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.LEFT))
												base = new Point2D.Double(contextualShape.getMaxX(), contextualShape.getMaxY());
											else if (sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.RIGHT))
												base = new Point2D.Double(contextualShape.getX(), contextualShape.getY());
											else if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.RIGHT))
												base = new Point2D.Double(contextualShape.getX(), contextualShape.getMaxY());
											else if (sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.LEFT))
												base = new Point2D.Double(contextualShape.getMaxX(), contextualShape.getY());

											IResizeData d = new ImmutableResizeData(point, sides, base, getCursor(sides).orElseThrow(InternalError::new).getHandle());
											synchronized (getLockObject()) {
												if (owner.getResizeData().isPresent())
													return false;
												owner.setResizeData(d);
											}
											return true;
										}
									})
							)
					)
			)
					.orElse(false);
		}

		protected Optional<? extends UITeleportingComponentUserResizableExtension<?>> getOwner() { return owner.getOptional(); }

		protected boolean finishResizeMaybe(@SuppressWarnings("unused") IUIViewContext viewContext, Point2D point) {
			return getOwner().flatMap(owner -> owner.getContainer().flatMap(container -> owner.getResizeData().map(data -> {
				Rectangle2D relativeShape = container.getShapeDescriptor().getShapeOutput().getBounds2D();
				data.handle((Point2D) point.clone(), relativeShape, relativeShape);
				boolean ret;
				synchronized (getLockObject()) {
					if (!owner.getResizeData().isPresent())
						return false;
					ret = container.reshape(s -> s.adapt(relativeShape));
					owner.setResizeData(null);
				}
				return ret;
			}))).orElse(false);
		}

		protected Object getLockObject() { return lockObject; }

		@Override
		public boolean containsPoint(IUIComponentContext context, Point2D point) {
			return getOwner()
					.flatMap(owner -> owner.isResizing() ?
							Optional.of(true) :
							owner.getResizeShape()
									.map(shape -> IUIComponentContext.createContextualShape(context, shape))
									.map(shape -> shape.contains(point)))
					.orElse(false);
		}

		@Override
		public Optional<Long> getCursorHandle(IUIComponentContext context) {
			if (getModifyStage().isPre())
				return Optional2.of(
						() -> getOwner().orElse(null),
						() -> context.getViewContext().getInputDevices().getPointerDevice().orElse(null))
						.flatMap(values -> {
							UITeleportingComponentUserResizableExtension<?> owner = values.getValue1Nonnull();
							IInputPointerDevice pointerDevice = values.getValue2Nonnull();
							Point2D cursorPosition = pointerDevice.getPositionView();
							@SuppressWarnings("Convert2MethodRef") @Nullable
							Optional<? extends Long> ret = owner.getResizeData()
									.map(d -> d.getBaseView()
											.map(b -> {
												Set<EnumUISide> sides = EnumUISide.getSidesMouseOver(
														new Rectangle2D.Double(b.getX(), b.getY(), 0, 0),
														cursorPosition);
												if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.LEFT)
														|| sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.RIGHT))
													return EnumGLFWCursor.EXTENSION_RESIZE_NW_SE_CURSOR.getHandle();
												else if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.RIGHT)
														|| sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.LEFT))
													return EnumGLFWCursor.EXTENSION_RESIZE_NE_SW_CURSOR.getHandle();
												return null;
											})
											.orElseGet(() -> d.getInitialCursorHandle())
									); // COMMENT compiler bug, long does not get boxed to Long with a method reference

							if (!ret.isPresent())
								ret = owner.getContainer()
										.filter(c -> isBeingHovered())
										.flatMap(c ->
												getCursor(
														EnumUISide.getSidesMouseOver(
																IUIComponent.getContextualShape(context, c).getBounds2D(),
																cursorPosition)
												)
														.map(ICursor::getHandle));

							return ret;
						});
			return Optional.empty();
		}

		protected boolean isBeingHovered() { return beingHovered; }

		protected void setBeingHovered(boolean beingHovered) { this.beingHovered = beingHovered; }

		@Override
		public void invokeRenderer(IUIComponentContext context) {
			if (getModifyStage().isPost()) {
				getOwner().ifPresent(owner ->
						Optional2.of(
								() -> owner.getRendererContainer().getRenderer().orElse(null),
								() -> owner.getResizeData().orElse(null))
								.ifPresent(values -> {
									IResizingRenderer renderer = values.getValue1Nonnull();
									IResizeData data = values.getValue2Nonnull();
									renderer.render(context, data);
								}));
			}
		}
	}
}
