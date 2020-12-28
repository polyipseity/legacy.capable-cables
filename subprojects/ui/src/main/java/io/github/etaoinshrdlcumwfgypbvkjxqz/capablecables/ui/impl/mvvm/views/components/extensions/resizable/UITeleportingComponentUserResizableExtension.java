package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.resizable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIExtensionArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIExtensionConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserResizableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.EnumModifyStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentCursorHandleProviderModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventMouse;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRendererInvokerModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.cursors.EnumGLFWCursor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIFunctionalEventListener;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.modifiers.UIAbstractVirtualComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.OneUseRunnable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.IIntersection;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ConstantValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBindingActionConsumerSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.DefaultBindingActionConsumerSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.AbstractContainerAwareExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.ICursor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IMouseButtonClickData;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IPointerDevice;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl.Optional2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl.OptionalUtilities;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMaps;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import org.jetbrains.annotations.NonNls;
import org.lwjgl.glfw.GLFW;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressBoxing;
import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;

public class UITeleportingComponentUserResizableExtension<C extends IUIComponent>
		extends AbstractContainerAwareExtension<IIdentifier, IUIComponent, C>
		implements IUIComponentUserResizableExtension<C> {
	public static final @NonNls String PROPERTY_TARGET_COMPONENT = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.teleporting.target";
	public static final @NonNls String PROPERTY_ACTIVATION_MOUSE_BUTTONS = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.teleporting.activation.mouse";
	public static final @NonNls String PROPERTY_RESIZE_BORDERS_DEFAULT_THICKNESS = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.teleporting.resize_borders.default_thickness";
	@NonNls
	public static final String PROPERTY_RESIZE_BORDERS = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.teleporting.resize_borders";
	private static final IIdentifier PROPERTY_TARGET_COMPONENT_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyTargetComponent());
	private static final IIdentifier PROPERTY_ACTIVATION_MOUSE_BUTTONS_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyActivationMouseButtons());
	private static final IIdentifier PROPERTY_RESIZE_BORDERS_DEFAULT_THICKNESS_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyResizeBordersDefaultThickness());
	private static final IIdentifier PROPERTY_RESIZE_BORDERS_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyResizeBorders());

	@UIProperty(PROPERTY_TARGET_COMPONENT)
	private final IBindingField<String> targetComponent;
	@UIProperty(PROPERTY_ACTIVATION_MOUSE_BUTTONS) // COMMENT accepted type: Set<? extends Integer>
	private final IBindingField<IntSet> activationMouseButtons;
	@UIProperty(PROPERTY_RESIZE_BORDERS)
	private final IBindingField<Double> resizeBorderDefaultThickness;
	@UIProperty(PROPERTY_RESIZE_BORDERS_DEFAULT_THICKNESS)
	// COMMENT accepted type: Map<? extends EnumUISide, ? extends Double>
	private final IBindingField<Object2DoubleMap<EnumUISide>> resizeBorders;

	private final Modifier modifier = new Modifier(suppressThisEscapedWarning(() -> this));
	private final IUIRendererContainerContainer<IResizingRenderer> rendererContainerContainer;
	private final IBindingActionConsumerSupplierHolder bindingActionConsumerSupplierHolder = new DefaultBindingActionConsumerSupplierHolder();
	@Nullable
	private IResizeData resizeData;

	@SuppressWarnings("unchecked")
	@UIExtensionConstructor
	public UITeleportingComponentUserResizableExtension(IUIExtensionArguments arguments) {
		super((Class<C>) arguments.getContainerClass());

		this.rendererContainerContainer =
				UIDefaultRendererContainerContainer.ofDefault(arguments.getRendererName().orElse(null), suppressThisEscapedWarning(() -> this), UIComponentUserResizeableExtensionEmptyResizingRenderer.class);

		Map<IIdentifier, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.targetComponent = IUIPropertyMappingValue.createBindingField(String.class, ConstantValue.of(""), mappings.get(getPropertyTargetComponentIdentifier()));
		this.activationMouseButtons = IUIPropertyMappingValue.createBindingField(IntSet.class,
				() -> IntSets.singleton(GLFW.GLFW_MOUSE_BUTTON_LEFT),
				mappings.get(getPropertyActivationMouseButtonsIdentifier()),
				CastUtilities.<Class<Set<? extends Integer>>>castUnchecked(Set.class),
				mappingValue -> IntSets.unmodifiable(new IntOpenHashSet(mappingValue)));
		this.resizeBorderDefaultThickness = IUIPropertyMappingValue.createBindingField(Double.class, ConstantValue.of(suppressBoxing(10D)), mappings.get(getPropertyResizeBordersDefaultThicknessIdentifier()));
		this.resizeBorders = IUIPropertyMappingValue.createBindingField(CastUtilities.castUnchecked(Object2DoubleMap.class),
				Object2DoubleMaps::emptyMap,
				mappings.get(getPropertyResizeBordersIdentifier()),
				CastUtilities.<Class<Map<EnumUISide, Double>>>castUnchecked(Map.class),
				mappingValue -> Object2DoubleMaps.unmodifiable(new Object2DoubleOpenHashMap<>(mappingValue)));
	}

	public static IIdentifier getPropertyTargetComponentIdentifier() {
		return PROPERTY_TARGET_COMPONENT_IDENTIFIER;
	}

	public static IIdentifier getPropertyActivationMouseButtonsIdentifier() {
		return PROPERTY_ACTIVATION_MOUSE_BUTTONS_IDENTIFIER;
	}

	public static IIdentifier getPropertyResizeBordersDefaultThicknessIdentifier() {
		return PROPERTY_RESIZE_BORDERS_DEFAULT_THICKNESS_IDENTIFIER;
	}

	public static IIdentifier getPropertyResizeBordersIdentifier() {
		return PROPERTY_RESIZE_BORDERS_IDENTIFIER;
	}

	public static String getPropertyActivationMouseButtons() {
		return PROPERTY_ACTIVATION_MOUSE_BUTTONS;
	}

	public static String getPropertyResizeBordersDefaultThickness() {
		return PROPERTY_RESIZE_BORDERS_DEFAULT_THICKNESS;
	}

	public static String getPropertyResizeBorders() {
		return PROPERTY_RESIZE_BORDERS;
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

	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	protected static Optional<IUIComponent> getTargetComponent(UITeleportingComponentUserResizableExtension<?> instance) {
		String targetComponent = instance.getTargetComponent().getValue();
		return targetComponent.isEmpty() // COMMENT empty component names are disallowed, so this indicates using the container
				? OptionalUtilities.upcast(instance.getContainer())
				: instance.getContainer()
				.flatMap(IUIComponent::getManager)
				.flatMap(IUIComponentManager::getView)
				.map(IUIView::getNamedTrackers)
				.flatMap(namedTrackers -> OptionalUtilities.upcast(namedTrackers.get(IUIComponent.class, targetComponent)));
	}

	protected IBindingField<String> getTargetComponent() {
		return targetComponent;
	}

	public static String getPropertyTargetComponent() {
		return PROPERTY_TARGET_COMPONENT;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>> bindingActionConsumerSupplier) {
		IUIComponentUserResizableExtension.super.initializeBindings(bindingActionConsumerSupplier);
		getBindingActionConsumerSupplierHolder().setValue(bindingActionConsumerSupplier);
		BindingUtilities.supplyBindingAction(bindingActionConsumerSupplier,
				() -> ImmutableBindingAction.bind(
						getActivationMouseButtons(),
						getResizeBorders(), getResizeBorderDefaultThickness()
				));
		BindingUtilities.initializeBindings(
				bindingActionConsumerSupplier, ImmutableList.of(getRendererContainerContainer())
		);
	}

	protected IBindingActionConsumerSupplierHolder getBindingActionConsumerSupplierHolder() {
		return bindingActionConsumerSupplierHolder;
	}

	@Override
	public Optional<? extends Shape> getResizeShape() {
		return getContainer()
				.map(IUIComponent::getShape)
				.map(Shape::getBounds2D)
				.map(containerShapeBounds -> {
					@SuppressWarnings("AutoUnboxing") double defaultThickness = getResizeBorderDefaultThickness().getValue();
					Object2DoubleMap<EnumUISide> borders = getResizeBorders().getValue();
					Area resizeShape = new Area(
							EnumUISide.getEdges().stream().unordered()
									.map(side -> {
										EnumUISide oppositeSide = side.getOpposite().orElseThrow(AssertionError::new);
										Rectangle2D border = (Rectangle2D) containerShapeBounds.clone();

										// COMMENT we drag the opposite side to a given offset from our side, ignoring other sides
										oppositeSide.setValue(border,
												side.getValue(border)
														+ side.outwardsBy(borders.getOrDefault(side, defaultThickness)).orElseThrow(AssertionError::new));

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

	protected IBindingField<Object2DoubleMap<EnumUISide>> getResizeBorders() {
		return resizeBorders;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings() {
		getBindingActionConsumerSupplierHolder().getValue().ifPresent(bindingActionConsumer -> BindingUtilities.supplyBindingAction(bindingActionConsumer,
				() -> ImmutableBindingAction.unbind(
						getActivationMouseButtons(),
						getResizeBorders(), getResizeBorderDefaultThickness()
				)));
		BindingUtilities.cleanupBindings(ImmutableList.of(getRendererContainerContainer()));
		getBindingActionConsumerSupplierHolder().setValue(null);
		IUIComponentUserResizableExtension.super.cleanupBindings();
	}

	protected IBindingField<IntSet> getActivationMouseButtons() {
		return activationMouseButtons;
	}

	protected IUIRendererContainerContainer<IResizingRenderer> getRendererContainerContainer() {
		return rendererContainerContainer;
	}

	@Override
	public Optional<? extends IResizeData> getResizeData() { return Optional.ofNullable(resizeData); }

	protected void setResizeData(@Nullable IResizeData resizeData) { this.resizeData = resizeData; }

	@Override
	@OverridingMethodsMustInvokeSuper
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	public void onExtensionAdded(IUIComponent container) {
		super.onExtensionAdded(container);
		container.addModifier(getModifier());
		container.getManager().flatMap(IUIComponentManager::getView)
				.ifPresent(view -> IUIView.registerRendererContainers(view, ImmutableSet.of(getRendererContainer())));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	public void onExtensionRemoved() {
		getContainer().ifPresent(container -> {
			container.removeModifier(getModifier());
			container.getManager().flatMap(IUIComponentManager::getView)
					.ifPresent(view -> IUIView.unregisterRendererContainers(view, ImmutableSet.of(getRendererContainer())));
		});
		super.onExtensionRemoved();
	}

	protected Modifier getModifier() { return modifier; }

	@Override
	public IUIRendererContainer<? extends IResizingRenderer> getRendererContainer() {
		return getRendererContainerContainer().getRendererContainer();
	}

	@Override
	public IExtensionType<IIdentifier, ?, IUIComponent> getType() { return StaticHolder.getType().getValue(); }

	public static class Modifier
			extends UIAbstractVirtualComponent
			implements IUIComponentCursorHandleProviderModifier, IUIComponentRendererInvokerModifier {
		private final OptionalWeakReference<UITeleportingComponentUserResizableExtension<?>> owner;
		private final Object lockObject = new Object();
		private final Runnable eventTargetListenersInitializer;
		private boolean beingHovered = false;
		@Nullable
		private Integer activeMouseButton;

		@SuppressWarnings({"rawtypes", "RedundantSuppression"})
		protected Modifier(UITeleportingComponentUserResizableExtension<?> owner) {
			super(IShapeDescriptor.StaticHolder.getShapeDescriptorPlaceholder());
			this.owner = OptionalWeakReference.of(owner);

			this.eventTargetListenersInitializer = new OneUseRunnable(() -> {
				addEventListener(EnumUIEventDOMType.MOUSE_ENTER.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(evt -> setBeingHovered(true)), false);
				addEventListener(EnumUIEventDOMType.MOUSE_LEAVE.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(evt -> setBeingHovered(false)), false);
				addEventListener(EnumUIEventDOMType.MOUSE_DOWN.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(evt -> {
					if (!getActiveMouseButton().isPresent()) {
						int button = evt.getData().getButton();
						getOwner()
								.filter(owner2 -> owner2.getActivationMouseButtons().getValue().contains(button))
								.filter(owner2 -> startResizeMaybe(evt.getViewContext(), evt.getData().getCursorPositionView()))
								.flatMap(owner2 -> owner2.getContainer()) // TODO Java 9 - IllegalAccessError now, make method ref
								.ifPresent(c -> {
									c.getParent().ifPresent(p ->
											p.moveChildToTop(c));
									setActiveMouseButton(suppressBoxing(button));
									evt.stopPropagation();
								});
					}
				}), false);
				addEventListener(EnumUIEventDOMType.MOUSE_UP.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(evt -> {
					if (getActiveMouseButton().orElseGet(IMouseButtonClickData.StaticHolder::getMouseButtonNull) == evt.getData().getButton()
							&& finishResizeMaybe(evt.getViewContext(), evt.getData().getCursorPositionView())) {
						evt.stopPropagation();
						setActiveMouseButton(null);
					}
				}), false);
			});
		}

		protected OptionalInt getActiveMouseButton() {
			return OptionalUtilities.ofInt(activeMouseButton);
		}

		protected void setActiveMouseButton(@Nullable Integer activeMouseButton) {
			this.activeMouseButton = activeMouseButton;
		}

		protected Optional<? extends UITeleportingComponentUserResizableExtension<?>> getOwner() { return owner.getOptional(); }

		@SuppressWarnings({"rawtypes", "RedundantSuppression"})
		protected boolean startResizeMaybe(@SuppressWarnings("unused") IUIViewContext viewContext, Point2D point) {
			return getOwner().filter(owner ->
					Optional2.of(
							() -> owner.getContainer().orElse(null),
							() -> UITeleportingComponentUserResizableExtension.getTargetComponent(owner)
									.filter(IUIReshapeExplicitly.class::isInstance)
									.orElse(null))
							.filter(ownerValues -> {
								IUIComponent container = ownerValues.getValue1Nonnull();
								IUIComponent targetComponent = ownerValues.getValue2Nonnull();
								return targetComponent.getManager()
										.flatMap(IUIComponentManager::getView)
										.filter(view -> IUIViewComponent.createComponentContextWithManager(view)
												.filter(context -> {
													try (IUIComponentContext context1 = context) {
														IUIViewComponent.getPathResolver(view).resolvePath(context1, (Point2D) point.clone());

														Rectangle2D contextualShape = IUIComponent.getContextualShape(context1, container).getBounds2D();
														Set<EnumUISide> sides = EnumUISide.getSidesPointOver(contextualShape, point);

														Point2D[] base = {null}; // TODO ASM - replace it with non-array variable, ASM hates annotations here
														if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.LEFT))
															base[0] = new Point2D.Double(contextualShape.getMaxX(), contextualShape.getMaxY());
														else if (sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.RIGHT))
															base[0] = new Point2D.Double(contextualShape.getX(), contextualShape.getY());
														else if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.RIGHT))
															base[0] = new Point2D.Double(contextualShape.getX(), contextualShape.getMaxY());
														else if (sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.LEFT))
															base[0] = new Point2D.Double(contextualShape.getMaxX(), contextualShape.getY());

														IResizeData data = UIImmutableResizeData.of((IUIComponent & IUIReshapeExplicitly<?>) targetComponent,
																point,
																sides,
																base[0],
																getCursor(sides).orElseThrow(AssertionError::new));
														synchronized (getLockObject()) {
															if (owner.getResizeData().isPresent())
																return false;
															owner.setResizeData(data);
														}
														return true;
													}
												}).isPresent()
										).isPresent();
							}).isPresent()
			).isPresent();
		}

		protected boolean finishResizeMaybe(@SuppressWarnings("unused") IUIViewContext viewContext, Point2D point) {
			return getOwner().filter(owner -> owner.getResizeData().filter(data ->
					Optional2.of(
							() -> data.getTargetComponent().orElse(null),
							() -> data.handle((Point2D) point.clone()).orElse(null))
							.filter(dataValues -> {
								IIntersection<? extends IUIComponent, ? extends IUIReshapeExplicitly<?>> targetComponent = dataValues.getValue1Nonnull();
								Rectangle2D relativeShapeBounds = dataValues.getValue2Nonnull().getBounds2D();
								synchronized (getLockObject()) {
									if (!owner.getResizeData().isPresent())
										return false;
									owner.setResizeData(null);
									return targetComponent.getRight().reshape(s -> s.adapt(relativeShapeBounds));
								}
							})
							.isPresent()
			).isPresent()).isPresent();
		}

		protected Object getLockObject() { return lockObject; }

		@Override
		protected SetMultimap<IIdentifier, UIEventListenerWithParameters> getEventTargetListeners() {
			eventTargetListenersInitializer.run();
			return super.getEventTargetListeners();
		}

		@Override
		public boolean containsPoint(IUIComponentContext context, Point2D point) {
			return getOwner()
					.filter(owner -> owner.isResizing()
							|| owner.getResizeShape()
							.map(shape -> IUIComponentContext.createContextualShape(context, shape))
							.filter(shape -> shape.contains(point))
							.isPresent())
					.isPresent();
		}

		@Override
		public Optional<? extends ICursor> getCursorHandle(IUIComponentContext context) {
			if (getModifyStage() == EnumModifyStage.PRE)
				return Optional2.of(
						() -> getOwner().orElse(null),
						() -> context.getViewContext().getInputDevices().getPointerDevice().orElse(null))
						.flatMap(values -> {
							UITeleportingComponentUserResizableExtension<?> owner = values.getValue1Nonnull();
							IPointerDevice pointerDevice = values.getValue2Nonnull();
							Point2D cursorPosition = pointerDevice.getPositionView();
							@SuppressWarnings("Convert2MethodRef") Optional<? extends ICursor> ret = owner.getResizeData()
									.map(d -> d.getBaseView()
											.<ICursor>flatMap(b -> {
												Set<EnumUISide> sides = EnumUISide.getSidesPointOver(
														new Rectangle2D.Double(b.getX(), b.getY(), 0, 0),
														cursorPosition);
												if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.LEFT)
														|| sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.RIGHT))
													return Optional.of(EnumGLFWCursor.EXTENSION_RESIZE_NW_SE_CURSOR);
												else if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.RIGHT)
														|| sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.LEFT))
													return Optional.of(EnumGLFWCursor.EXTENSION_RESIZE_NE_SW_CURSOR);
												return Optional.empty();
											})
											.orElseGet(() -> d.getInitialCursorHandle()) // TODO javac bug
									);

							if (!ret.isPresent())
								ret = isBeingHovered()
										? owner.getContainer()
										.flatMap(container ->
												getCursor(
														EnumUISide.getSidesPointOver(
																IUIComponent.getContextualShape(context, container).getBounds2D(),
																cursorPosition)))
										: Optional.empty();

							return ret;
						});
			return Optional.empty();
		}

		protected boolean isBeingHovered() { return beingHovered; }

		protected void setBeingHovered(boolean beingHovered) { this.beingHovered = beingHovered; }

		@Override
		public void invokeRenderer(IUIComponentContext context) {
			if (getModifyStage() == EnumModifyStage.POST) {
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
