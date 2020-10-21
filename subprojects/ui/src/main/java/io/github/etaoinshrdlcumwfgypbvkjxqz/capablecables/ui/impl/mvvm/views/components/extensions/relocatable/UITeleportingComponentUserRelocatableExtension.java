package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.relocatable;

import com.google.common.collect.ImmutableSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserRelocatableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentCursorHandleProviderModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventMouse;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRendererInvokerModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIExtensionConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIFunctionalEventListener;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.modifiers.UIAbstractVirtualComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.AbstractContainerAwareExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl.UIObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl.Optional2;
import io.reactivex.rxjava3.observers.DisposableObserver;
import org.jetbrains.annotations.NonNls;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class UITeleportingComponentUserRelocatableExtension<E extends IUIComponent & IUIReshapeExplicitly<? extends IShapeDescriptor<? extends RectangularShape>>>
		extends AbstractContainerAwareExtension<INamespacePrefixedString, IUIComponent, E>
		implements IUIComponentUserRelocatableExtension<E> {
	@NonNls
	public static final String PROPERTY_ACTIVATION_MOUSE_BUTTONS = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "component.extension.user_relocatable.activation.mouse";
	private static final INamespacePrefixedString PROPERTY_ACTIVATION_MOUSE_BUTTONS_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyActivationMouseButtons());
	@UIProperty(PROPERTY_ACTIVATION_MOUSE_BUTTONS)
	private final IBindingField<Set<Integer>> activationMouseButtons;

	@SuppressWarnings("unchecked")
	@UIExtensionConstructor
	public UITeleportingComponentUserRelocatableExtension(UIExtensionConstructor.IArguments arguments) {
		super(IUIComponent.class, (Class<E>) arguments.getContainerClass());

		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.activationMouseButtons = IUIPropertyMappingValue.createBindingField(CastUtilities.castUnchecked(Set.class), false,
				ImmutableSet.of(GLFW.GLFW_MOUSE_BUTTON_LEFT), mappings.get(getPropertyActivationMouseButtonsLocation()));
	}

	public static INamespacePrefixedString getPropertyActivationMouseButtonsLocation() {
		return PROPERTY_ACTIVATION_MOUSE_BUTTONS_LOCATION;
	}

	public static final int RELOCATE_BORDER_THICKNESS_DEFAULT = 10;
	private final int relocateBorderThickness = getRelocateBorderThicknessDefault(); // TODO make this a property and strategy or something like that
	@SuppressWarnings("ThisEscapedInObjectConstruction")
	private final Modifier modifier = new Modifier(this);
	private final AtomicReference<IUIRendererContainer<IRelocatingRenderer>> rendererContainerReference = new AtomicReference<>();
	@Nullable
	private IRelocateData relocateData;
	@Nullable
	private Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier;

	public static String getPropertyActivationMouseButtons() {
		return PROPERTY_ACTIVATION_MOUSE_BUTTONS;
	}

	protected IBindingField<Set<Integer>> getActivationMouseButtons() {
		return activationMouseButtons;
	}

	public static int getRelocateBorderThicknessDefault() {
		return RELOCATE_BORDER_THICKNESS_DEFAULT;
	}

	@Override
	public IUIRendererContainer<IRelocatingRenderer> getRendererContainer()
			throws IllegalStateException { return Optional.ofNullable(getRendererContainerReference().get()).orElseThrow(IllegalStateException::new); }

	@Override
	public void initializeRendererContainer(@NonNls CharSequence name)
			throws IllegalStateException {
		IUIRendererContainer<IRelocatingRenderer> rendererContainer = new UIDefaultRendererContainer<>(name, this, CastUtilities.castUnchecked(UIComponentUserRelocatableExtensionEmptyRelocatingRenderer.class));
		if (!getRendererContainerReference().compareAndSet(null, rendererContainer))
			throw new IllegalStateException();
		getBinderObserverSupplier().ifPresent(rendererContainer::initializeBindings);
	}

	protected Optional<? extends Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>>> getBinderObserverSupplier() { return Optional.ofNullable(binderObserverSupplier); }

	protected void setBinderObserverSupplier(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) { this.binderObserverSupplier = binderObserverSupplier; }

	protected AtomicReference<IUIRendererContainer<IRelocatingRenderer>> getRendererContainerReference() { return rendererContainerReference; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		setBinderObserverSupplier(binderObserverSupplier);
		Optional.ofNullable(getRendererContainerReference().get())
				.ifPresent(rendererContainer -> rendererContainer.initializeBindings(binderObserverSupplier));
	}

	@Override
	public IExtensionType<INamespacePrefixedString, ?, IUIComponent> getType() { return StaticHolder.getTYPE().getValue(); }

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
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	public void onExtensionRemoved() {
		super.onExtensionRemoved();
		getContainer().ifPresent(c ->
				c.removeModifier(getModifier()));
	}

	@Override
	public Optional<? extends Shape> getRelocateShape() {
		return getContainer().map(c ->
				UIObjectUtilities.applyRectangularShape(c.getShapeDescriptor().getShapeOutput().getBounds2D(),
						(x, y, w, h) -> {
							assert x != null;
							assert y != null;
							assert w != null;
							return new Rectangle2D.Double(x, y, w, getRelocateBorderThickness());
						})); // TODO custom
	}

	@Override
	public boolean isRelocating() { return getRelocateData().isPresent(); }

	@Override
	public Optional<? extends IRelocateData> getRelocateData() { return Optional.ofNullable(relocateData); }

	protected void setRelocateData(@Nullable IRelocateData relocateData) { this.relocateData = relocateData; }

	public int getRelocateBorderThickness() { return relocateBorderThickness; }

	public static class Modifier
			extends UIAbstractVirtualComponent
			implements IUIComponentCursorHandleProviderModifier, IUIComponentRendererInvokerModifier {
		private final OptionalWeakReference<UITeleportingComponentUserRelocatableExtension<?>> owner;
		private final Object lockObject = new Object();

		@Nullable
		private Integer activeMouseButton;

		@SuppressWarnings({"OverridableMethodCallDuringObjectConstruction", "rawtypes", "RedundantSuppression"})
		protected Modifier(UITeleportingComponentUserRelocatableExtension<?> owner) {
			super(IShapeDescriptor.StaticHolder.getShapeDescriptorPlaceholder());
			this.owner = new OptionalWeakReference<>(owner);

			addEventListener(EnumUIEventDOMType.MOUSE_DOWN.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(evt -> {
				if (!getActiveMouseButton().isPresent()) {
					int button = evt.getData().getButton();
					getOwner()
							.filter(owner2 -> IField.getValueNonnull(owner2.getActivationMouseButtons()).contains(button))
							.filter(owner2 -> startRelocateMaybe(evt.getViewContext(), evt.getData().getCursorPositionView()))
							.flatMap(owner2 -> owner2.getContainer()) // TODO Java 9 - IllegalAccessError now, make method ref
							.ifPresent(c -> {
								c.getParent().ifPresent(p ->
										p.moveChildToTop(c));
								c.moveModifierToTop(this);
								setActiveMouseButton(button);
								evt.stopPropagation();
							});
				}
			}), false);
			addEventListener(EnumUIEventDOMType.MOUSE_UP.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(evt -> {
				if (getActiveMouseButton().filter(Predicate.isEqual(evt.getData().getButton())).isPresent()
						&& finishRelocateMaybe(evt.getViewContext(), evt.getData().getCursorPositionView())) {
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

		protected boolean startRelocateMaybe(@SuppressWarnings("unused") IUIViewContext viewContext, Point2D point) {
			return getOwner().map(owner -> {
				IRelocateData d = new ImmutableRelocateData(point);
				synchronized (getLockObject()) {
					if (owner.getRelocateData().isPresent())
						return false;
					owner.setRelocateData(d);
				}
				return true;
			}).orElse(false);
		}

		public Optional<? extends UITeleportingComponentUserRelocatableExtension<?>> getOwner() { return owner.getOptional(); }

		protected boolean finishRelocateMaybe(@SuppressWarnings("unused") IUIViewContext viewContext, Point2D point) {
			return getOwner()
					.flatMap(owner -> owner.getContainer().flatMap(container -> owner.getRelocateData().map(data -> {
						Rectangle2D resultRectangle = container.getShapeDescriptor().getShapeOutput().getBounds2D();
						data.handle((Point2D) point.clone(), resultRectangle, resultRectangle);
						boolean ret;
						synchronized (getLockObject()) {
							if (!owner.getRelocateData().isPresent())
								return false;
							ret = container.reshape(s -> s.adapt(resultRectangle));
							owner.setRelocateData(null);
						}
						return ret;
					})))
					.orElse(false);
		}

		protected Object getLockObject() { return lockObject; }

		@Override
		@SuppressWarnings({"rawtypes", "RedundantSuppression"})
		public Optional<Long> getCursorHandle(IUIComponentContext context) {
			return getModifyStage().isPre()
					&& getOwner()
					.filter(IUIComponentUserRelocatableExtension::isRelocating)
					.isPresent()
					? Optional.of(MemoryUtil.NULL)
					: Optional.empty();
		}

		@Override
		public boolean containsPoint(IUIComponentContext context, Point2D point) {
			return getOwner()
					.flatMap(owner -> owner.isRelocating() ?
							Optional.of(true) :
							owner.getRelocateShape()
									.map(shape -> IUIComponentContext.createContextualShape(context, shape))
									.map(shape -> shape.contains(point)))
					.orElse(false);
		}

		@Override
		public void invokeRenderer(IUIComponentContext context) {
			if (getModifyStage().isPost()) {
				getOwner().ifPresent(owner ->
						Optional2.of(
								() -> owner.getRendererContainer().getRenderer().orElse(null),
								() -> owner.getRelocateData().orElse(null))
								.ifPresent(values -> {
									IRelocatingRenderer renderer = values.getValue1Nonnull();
									IRelocateData data = values.getValue2Nonnull();
									renderer.render(context, data);
								}));
			}
		}
	}
}
