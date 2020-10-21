package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.resizable;

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
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.AbstractContainerAwareExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl.UIObjectUtilities;
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
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class UITeleportingComponentUserResizableExtension<E extends IUIComponent & IUIReshapeExplicitly<? extends IShapeDescriptor<? extends RectangularShape>>>
		extends AbstractContainerAwareExtension<INamespacePrefixedString, IUIComponent, E>
		implements IUIComponentUserResizableExtension<E> {
	public static final int RESIZE_BORDER_THICKNESS_DEFAULT = 10;
	private final int resizeBorderThickness = getResizeBorderThicknessDefault(); // TODO make this a property and strategy or something like that
	@SuppressWarnings("ThisEscapedInObjectConstruction")
	private final Modifier modifier = new Modifier(this);
	private final AtomicReference<IUIRendererContainer<IResizingRenderer>> rendererContainerReference = new AtomicReference<>();
	@Nullable
	private IResizeData resizeData;
	@Nullable
	private Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier;

	@SuppressWarnings("unchecked")
	@UIExtensionConstructor
	public UITeleportingComponentUserResizableExtension(UIExtensionConstructor.IArguments arguments) {
		super(IUIComponent.class, (Class<E>) arguments.getContainerClass());
	}

	public static int getResizeBorderThicknessDefault() {
		return RESIZE_BORDER_THICKNESS_DEFAULT;
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

	protected void setBinderObserverSupplier(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) { this.binderObserverSupplier = binderObserverSupplier; }

	protected AtomicReference<IUIRendererContainer<IResizingRenderer>> getRendererContainerReference() { return rendererContainerReference; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		setBinderObserverSupplier(binderObserverSupplier);
		Optional.ofNullable(getRendererContainerReference().get())
				.ifPresent(rendererContainer -> rendererContainer.initializeBindings(binderObserverSupplier));
	}

	@Override
	public IExtensionType<INamespacePrefixedString, ?, IUIComponent> getType() { return StaticHolder.getType().getValue(); }

	@Override
	@OverridingMethodsMustInvokeSuper
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	public void onExtensionAdded(IUIComponent container) {
		super.onExtensionAdded(container);
		getContainer()
				.ifPresent(c -> c.addModifier(getModifier()));
	}

	protected Modifier getModifier() { return modifier; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionRemoved() {
		super.onExtensionRemoved();
		getContainer()
				.ifPresent(c -> c.removeModifier(getModifier()));
	}

	@Override
	public Optional<? extends Shape> getResizeShape() {
		return getContainer().map(c -> {
			Rectangle2D spb = c.getShapeDescriptor().getShapeOutput().getBounds2D();
			Area ret = new Area(UIObjectUtilities.applyRectangularShape(spb, (x, y, w, h) -> {
				assert x != null;
				assert y != null;
				assert w != null;
				assert h != null;
				return new Rectangle2D.Double(x - getResizeBorderThickness(), y - getResizeBorderThickness(),
						w + (getResizeBorderThickness() << 1), h + (getResizeBorderThickness() << 1));
			}));
			ret.subtract(new Area(spb));
			return ret;
		});
	}

	@Override
	public Optional<? extends IResizeData> getResizeData() { return Optional.ofNullable(resizeData); }

	protected void setResizeData(@Nullable IResizeData resizeData) { this.resizeData = resizeData; }

	public int getResizeBorderThickness() { return resizeBorderThickness; }

	public static class Modifier
			extends UIAbstractVirtualComponent
			implements IUIComponentCursorHandleProviderModifier, IUIComponentRendererInvokerModifier {
		private final OptionalWeakReference<UITeleportingComponentUserResizableExtension<?>> owner;
		private final Object lockObject = new Object();
		private boolean beingHovered = false;

		@SuppressWarnings({"OverridableMethodCallDuringObjectConstruction", "rawtypes", "RedundantSuppression"})
		protected Modifier(UITeleportingComponentUserResizableExtension<?> owner) {
			super(IShapeDescriptor.StaticHolder.getShapeDescriptorPlaceholder());
			this.owner = new OptionalWeakReference<>(owner);

			addEventListener(EnumUIEventDOMType.MOUSE_ENTER.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(evt -> setBeingHovered(true)), false);
			addEventListener(EnumUIEventDOMType.MOUSE_LEAVE.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(evt -> setBeingHovered(false)), false);
			addEventListener(EnumUIEventDOMType.MOUSE_DOWN.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(evt -> {
				if (evt.getData().getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT && startResizeMaybe(evt.getViewContext(), evt.getData().getCursorPositionView())) { // todo custom
					getOwner()
							.flatMap(owner2 -> owner2.getContainer()) // TODO Java 9 - IllegalAccessError now, make method ref
							.ifPresent(c -> {
								c.getParent()
										.ifPresent(p -> p.moveChildToTop(c));
								c.moveModifierToTop(this);
							});
					evt.stopPropagation();
				}
			}), false);
			addEventListener(EnumUIEventDOMType.MOUSE_UP.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(evt -> {
				if (evt.getData().getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT && finishResizeMaybe(evt.getViewContext(), evt.getData().getCursorPositionView()))
					evt.stopPropagation();
			}), false);
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