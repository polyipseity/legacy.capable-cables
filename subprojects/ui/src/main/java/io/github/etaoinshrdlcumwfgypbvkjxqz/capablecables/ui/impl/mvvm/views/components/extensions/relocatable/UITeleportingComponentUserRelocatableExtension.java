package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.relocatable;

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
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserRelocatableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.EnumModifyStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentCursorHandleProviderModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventMouse;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRendererInvokerModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIFunctionalEventListener;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.modifiers.UIAbstractVirtualComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.OneUseRunnable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.IIntersection;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ConstantValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderObserverSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.DefaultBinderObserverSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.AbstractContainerAwareExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IMouseButtonClickData;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl.Optional2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl.OptionalUtilities;
import io.reactivex.rxjava3.observers.DisposableObserver;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMaps;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import org.jetbrains.annotations.NonNls;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.function.Supplier;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressBoxing;
import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;

public class UITeleportingComponentUserRelocatableExtension<C extends IUIComponent>
		extends AbstractContainerAwareExtension<IIdentifier, IUIComponent, C>
		implements IUIComponentUserRelocatableExtension<C> {
	public static final @NonNls String PROPERTY_TARGET_COMPONENT = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.teleporting.target";
	public static final @NonNls String PROPERTY_ACTIVATION_MOUSE_BUTTONS = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.teleporting.activation.mouse";
	public static final @NonNls String PROPERTY_RELOCATE_BORDERS = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.teleporting.relocate_borders";
	private static final IIdentifier PROPERTY_TARGET_COMPONENT_IDENTIFIER = ImmutableIdentifier.of(getPropertyTargetComponent());
	private static final IIdentifier PROPERTY_ACTIVATION_MOUSE_BUTTONS_IDENTIFIER = ImmutableIdentifier.of(getPropertyActivationMouseButtons());
	private static final IIdentifier PROPERTY_RELOCATE_BORDERS_IDENTIFIER = ImmutableIdentifier.of(getPropertyRelocateBorders());

	@UIProperty(PROPERTY_TARGET_COMPONENT)
	private final IBindingField<String> targetComponent;
	@UIProperty(PROPERTY_ACTIVATION_MOUSE_BUTTONS) // COMMENT accepted type: Set<? extends Integer>
	private final IBindingField<IntSet> activationMouseButtons;
	@UIProperty(PROPERTY_ACTIVATION_MOUSE_BUTTONS) // COMMENT accepted type: Map<? extends EnumUISide, ? extends Double>
	private final IBindingField<Object2DoubleMap<EnumUISide>> relocateBorders;

	private final Modifier modifier = new Modifier(suppressThisEscapedWarning(() -> this));
	private final IUIRendererContainerContainer<IRelocatingRenderer> rendererContainerContainer;
	private final IBinderObserverSupplierHolder binderObserverSupplierHolder = new DefaultBinderObserverSupplierHolder();
	@Nullable
	private IRelocateData relocateData;

	@SuppressWarnings("unchecked")
	@UIExtensionConstructor
	public UITeleportingComponentUserRelocatableExtension(IUIExtensionArguments arguments) {
		super((Class<C>) arguments.getContainerClass());

		this.rendererContainerContainer =
				UIDefaultRendererContainerContainer.ofDefault(arguments.getRendererName().orElse(null), suppressThisEscapedWarning(() -> this), UIComponentUserRelocatableExtensionEmptyRelocatingRenderer.class);

		Map<IIdentifier, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.targetComponent = IUIPropertyMappingValue.createBindingField(String.class, ConstantValue.of(""), mappings.get(getPropertyTargetComponentIdentifier()));
		this.activationMouseButtons = IUIPropertyMappingValue.createBindingField(IntSet.class,
				() -> IntSets.singleton(GLFW.GLFW_MOUSE_BUTTON_LEFT),
				mappings.get(getPropertyActivationMouseButtonsIdentifier()),
				CastUtilities.<Class<Set<? extends Integer>>>castUnchecked(Set.class),
				mappingValue -> IntSets.unmodifiable(new IntOpenHashSet(mappingValue)));
		this.relocateBorders = IUIPropertyMappingValue.createBindingField(CastUtilities.castUnchecked(Object2DoubleMap.class),
				() -> {
					Object2DoubleMap<EnumUISide> defaultValue = new Object2DoubleOpenHashMap<>();
					defaultValue.put(EnumUISide.UP, 10D);
					return Object2DoubleMaps.unmodifiable(defaultValue);
				},
				mappings.get(getPropertyRelocateBordersIdentifier()),
				CastUtilities.<Class<Map<? extends EnumUISide, ? extends Double>>>castUnchecked(Map.class),
				mappingValue -> Object2DoubleMaps.unmodifiable(new Object2DoubleOpenHashMap<>(mappingValue)));
	}

	public static IIdentifier getPropertyTargetComponentIdentifier() {
		return PROPERTY_TARGET_COMPONENT_IDENTIFIER;
	}

	public static IIdentifier getPropertyActivationMouseButtonsIdentifier() {
		return PROPERTY_ACTIVATION_MOUSE_BUTTONS_IDENTIFIER;
	}

	public static IIdentifier getPropertyRelocateBordersIdentifier() {
		return PROPERTY_RELOCATE_BORDERS_IDENTIFIER;
	}

	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	protected static Optional<IUIComponent> getTargetComponent(UITeleportingComponentUserRelocatableExtension<?> instance) {
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

	public static String getPropertyRelocateBorders() {
		return PROPERTY_RELOCATE_BORDERS;
	}

	public static String getPropertyActivationMouseButtons() {
		return PROPERTY_ACTIVATION_MOUSE_BUTTONS;
	}

	public static String getPropertyTargetComponent() {
		return PROPERTY_TARGET_COMPONENT;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		IUIComponentUserRelocatableExtension.super.initializeBindings(binderObserverSupplier);
		getBinderObserverSupplierHolder().setValue(binderObserverSupplier);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
				() -> ImmutableBinderAction.bind(
						getActivationMouseButtons(),
						getRelocateBorders()
				));
		BindingUtilities.initializeBindings(
				binderObserverSupplier, ImmutableList.of(getRendererContainerContainer())
		);
	}

	protected IBinderObserverSupplierHolder getBinderObserverSupplierHolder() {
		return binderObserverSupplierHolder;
	}

	protected IBindingField<IntSet> getActivationMouseButtons() {
		return activationMouseButtons;
	}

	@Override
	public Optional<? extends Shape> getRelocateShape() {
		return getContainer()
				.map(container -> {
					// COMMENT step 1: calculate based on our own data only
					Area result = getRelocateBorders().getValue().object2DoubleEntrySet().stream().unordered()
							.map(entry -> {
								EnumUISide side = AssertionUtilities.assertNonnull(entry.getKey());
								double thickness = entry.getDoubleValue();

								EnumUISide oppositeSide = side.getOpposite().orElseThrow(IllegalStateException::new);
								Rectangle2D border = IUIComponent.getShape(container).getBounds2D();

								// COMMENT fwe drag the opposite side to a given offset from our side, ignoring other sides
								oppositeSide.setValue(border,
										side.getValue(border) + side.inwardsBy(thickness).orElseThrow(IllegalStateException::new));

								return border;
							})
							.map(Area::new)
							.collect(Area::new, Area::add, Area::add);
					// COMMENT step 2: subtract children - we should only affect the component we are acting on
					AffineTransform childrenTransform = new AffineTransform();
					container.transformChildren(childrenTransform);
					result.subtract(
							container.getChildrenView().stream().unordered()
									.map(IUIComponent::getShape)
									.map(childrenTransform::createTransformedShape)
									.map(Area::new)
									.collect(Area::new, Area::add, Area::add)
					);
					return result;
				});
	}

	protected IUIRendererContainerContainer<IRelocatingRenderer> getRendererContainerContainer() {
		return rendererContainerContainer;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings() {
		getBinderObserverSupplierHolder().getValue().ifPresent(binderObserverSupplier -> {
			BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
					() -> ImmutableBinderAction.unbind(
							getActivationMouseButtons(),
							getRelocateBorders()
					));
			BindingUtilities.cleanupBindings(
					ImmutableList.of(getRendererContainerContainer())
			);
		});
		getBinderObserverSupplierHolder().setValue(null);
		IUIComponentUserRelocatableExtension.super.cleanupBindings();
	}

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
	public IUIRendererContainer<? extends IRelocatingRenderer> getRendererContainer() {
		return getRendererContainerContainer().getRendererContainer();
	}

	@Override
	public IExtensionType<IIdentifier, ?, IUIComponent> getType() { return StaticHolder.getTYPE().getValue(); }

	protected IBindingField<Object2DoubleMap<EnumUISide>> getRelocateBorders() {
		return relocateBorders;
	}

	@Override
	public boolean isRelocating() { return getRelocateData().isPresent(); }

	@Override
	public Optional<? extends IRelocateData> getRelocateData() { return Optional.ofNullable(relocateData); }

	protected void setRelocateData(@Nullable IRelocateData relocateData) { this.relocateData = relocateData; }

	public static class Modifier
			extends UIAbstractVirtualComponent
			implements IUIComponentCursorHandleProviderModifier, IUIComponentRendererInvokerModifier {
		private final OptionalWeakReference<UITeleportingComponentUserRelocatableExtension<?>> owner;
		private final Object lockObject = new Object();
		private final Runnable eventTargetListenersInitializer;
		@Nullable
		private Integer activeMouseButton;

		@SuppressWarnings({"rawtypes", "RedundantSuppression"})
		protected Modifier(UITeleportingComponentUserRelocatableExtension<?> owner) {
			super(IShapeDescriptor.StaticHolder.getShapeDescriptorPlaceholder());
			this.owner = OptionalWeakReference.of(owner);

			this.eventTargetListenersInitializer = new OneUseRunnable(() -> {
				addEventListener(EnumUIEventDOMType.MOUSE_DOWN.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(evt -> {
					if (!getActiveMouseButton().isPresent()) {
						int button = evt.getData().getButton();
						getOwner()
								.filter(owner2 -> owner2.getActivationMouseButtons().getValue().contains(button))
								.filter(owner2 -> startRelocateMaybe(evt.getViewContext(), evt.getData().getCursorPositionView()))
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
							&& finishRelocateMaybe(evt.getViewContext(), evt.getData().getCursorPositionView())) {
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

		public Optional<? extends UITeleportingComponentUserRelocatableExtension<?>> getOwner() { return owner.getOptional(); }

		protected boolean startRelocateMaybe(@SuppressWarnings("unused") IUIViewContext viewContext, Point2D point) {
			return getOwner().filter(owner -> UITeleportingComponentUserRelocatableExtension.getTargetComponent(owner).filter(targetComponent -> {
				if (!(targetComponent instanceof IUIReshapeExplicitly))
					return false;
				IRelocateData data = UIImmutableRelocateData.of((IUIComponent & IUIReshapeExplicitly<?>) targetComponent,
						point);
				synchronized (getLockObject()) {
					if (owner.getRelocateData().isPresent())
						return false;
					owner.setRelocateData(data);
				}
				return true;
			}).isPresent()).isPresent();
		}

		protected boolean finishRelocateMaybe(@SuppressWarnings("unused") IUIViewContext viewContext, Point2D point) {
			return getOwner()
					.filter(owner -> owner.getRelocateData().filter(data ->
							Optional2.of(
									() -> data.getTargetComponent().orElse(null),
									() -> data.handle((Point2D) point.clone()).orElse(null))
									.filter(dataValues -> {
										IIntersection<? extends IUIComponent, ? extends IUIReshapeExplicitly<?>> targetComponent = dataValues.getValue1Nonnull();
										Rectangle2D relativeShapeBounds = dataValues.getValue2Nonnull().getBounds2D();
										synchronized (getLockObject()) {
											if (!owner.getRelocateData().isPresent())
												return false;
											owner.setRelocateData(null);
											return targetComponent.getRight().reshape(s -> s.adapt(relativeShapeBounds));
										}
									}).isPresent()
					).isPresent()).isPresent();
		}

		protected Object getLockObject() { return lockObject; }

		@Override
		protected SetMultimap<IIdentifier, UIEventListenerWithParameters> getEventTargetListeners() {
			eventTargetListenersInitializer.run();
			return super.getEventTargetListeners();
		}

		@Override
		@SuppressWarnings({"rawtypes", "RedundantSuppression"})
		public OptionalLong getCursorHandle(IUIComponentContext context) {
			return getModifyStage() == EnumModifyStage.PRE
					&& getOwner()
					.filter(IUIComponentUserRelocatableExtension::isRelocating)
					.isPresent()
					? OptionalLong.of(MemoryUtil.NULL)
					: OptionalLong.empty();
		}

		@Override
		public boolean containsPoint(IUIComponentContext context, Point2D point) {
			return getOwner()
					.filter(owner -> owner.isRelocating()
							|| owner.getRelocateShape()
							.map(shape -> IUIComponentContext.createContextualShape(context, shape))
							.filter(shape -> shape.contains(point))
							.isPresent())
					.isPresent();
		}

		@Override
		public void invokeRenderer(IUIComponentContext context) {
			if (getModifyStage() == EnumModifyStage.POST) {
				getOwner().ifPresent(owner ->
						Optional2.of(
								() -> owner.getRendererContainer().getRenderer().orElse(null),
								() -> owner.getRelocateData().orElse(null))
								.ifPresent(values -> {
									IRelocatingRenderer renderer = values.getValue1Nonnull();
									IRelocateData data = values.getValue2Nonnull();
									data.getTargetComponent().ifPresent(targetComponent -> {
										if (targetComponent.getLeft().equals(IUIComponentContext.getCurrentComponent(context)
												.orElseThrow(AssertionError::new))) {
											renderer.render(context, data);
										} else {
											try (IUIComponentContext context1 = IUIComponent.createContextTo(targetComponent.getLeft())) {
												renderer.render(context1, data);
											}
										}
									});
								}));
			}
		}
	}
}
