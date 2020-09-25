package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIExtensionComponentUserResizable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.cursors.IUIComponentCursorHandleProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventMouse;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIExtensionConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.cursors.EnumGLFWCursor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.cursors.ICursor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.ui.UIEventListener;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.events.bus.UIViewMinecraftBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.UIComponentVirtual;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.EnumUIAxis;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.shapes.descriptors.GenericShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.utilities.minecraft.MinecraftDrawingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AutoCloseableRotator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.AbstractContainerAwareExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.LoggingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ui.UIObjectUtilities;
import io.reactivex.rxjava3.disposables.Disposable;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class UIExtensionComponentUserResizable<E extends IUIComponent & IUIReshapeExplicitly<? extends IShapeDescriptor<? extends RectangularShape>>>
		extends AbstractContainerAwareExtension<INamespacePrefixedString, IUIComponent, E>
		implements IUIExtensionComponentUserResizable<E> {
	public static final int RESIZE_BORDER_THICKNESS_DEFAULT = 10;
	private final int resizeBorderThickness = RESIZE_BORDER_THICKNESS_DEFAULT; // TODO make this a property and strategy or something like that
	private final Object lockObject = new Object();
	private final VirtualComponent virtualComponent = new VirtualComponent();
	@SuppressWarnings("ThisEscapedInObjectConstruction")
	private final AutoCloseableRotator<RenderObserver, RuntimeException> renderObserverRotator =
			new AutoCloseableRotator<>(() -> new RenderObserver(this, UIConfiguration.getInstance().getLogger()), Disposable::dispose);
	@Nullable
	private IResizeData resizeData;

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

	@UIExtensionConstructor(type = UIExtensionConstructor.EnumConstructorType.CONTAINER_CLASS)
	public UIExtensionComponentUserResizable(Class<E> containerClass) {
		super(IUIComponent.class, containerClass);
	}

	@Override
	public IExtensionType<INamespacePrefixedString, ?, IUIComponent> getType() { return TYPE.getValue(); }

	@Override
	@OverridingMethodsMustInvokeSuper
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	public void onExtensionAdded(IUIComponent container) {
		super.onExtensionAdded(container);
		getContainer()
				.ifPresent(c -> c.getManager()
						.flatMap(IUIComponentManager::getView)
						.ifPresent(v -> {
							getVirtualComponent().setRelatedComponent(c);
							v.getPathResolver().addVirtualElement(c, getVirtualComponent());
						}));
		UIEventBusEntryPoint.<UIViewMinecraftBusEvent.Render>getEventBus()
				.subscribe(getRenderObserverRotator().get());
	}

	protected AutoCloseableRotator<RenderObserver, RuntimeException> getRenderObserverRotator() { return renderObserverRotator; }

	@SuppressWarnings("ReturnOfInnerClass")
	protected VirtualComponent getVirtualComponent() { return virtualComponent; }

	@Override
	public Optional<? extends Shape> getResizeShape() {
		return getContainer().map(c -> {
			Rectangle2D spb = c.getShapeDescriptor().getShapeOutput().getBounds2D();
			Area ret = new Area(UIObjectUtilities.applyRectangularShape(spb, (x, y, w, h) ->
					new Rectangle2D.Double(x - getResizeBorderThickness(), y - getResizeBorderThickness(),
							w + (getResizeBorderThickness() << 1), h + (getResizeBorderThickness() << 1))));
			ret.subtract(new Area(spb));
			return ret;
		});
	}

	@Override
	public Optional<? extends IResizeData> getResizeData() { return Optional.ofNullable(resizeData); }

	protected void setResizeData(@Nullable IResizeData resizeData) { this.resizeData = resizeData; }

	public int getResizeBorderThickness() { return resizeBorderThickness; }

	protected Object getLockObject() { return lockObject; }

	public static class ResizeData implements IResizeData {
		protected final Point2D cursorPosition;
		protected final Set<EnumUISide> sides;
		@Nullable
		protected final Point2D base;
		protected final long initialCursorHandle;

		public ResizeData(Point2D cursorPosition, Set<EnumUISide> sides, @Nullable Point2D base, long initialCursorHandle) {
			this.cursorPosition = (Point2D) cursorPosition.clone();
			this.sides = EnumSet.copyOf(sides);
			this.base = Optional.ofNullable(base).map(Point2D::clone).map(CastUtilities::<Point2D>castUnchecked).orElse(null);
			this.initialCursorHandle = initialCursorHandle;
		}

		@Override
		public Point2D getCursorPositionView() { return getCursorPosition(); }

		@Override
		public Set<? extends EnumUISide> getSidesView() { return EnumSet.copyOf(getSides()); }

		@Override
		public Optional<? extends Point2D> getBaseView() { return getBase().map(Point2D::clone).map(CastUtilities::castUnchecked); }

		@Override
		public long getInitialCursorHandle() { return initialCursorHandle; }

		@Override
		public <R extends RectangularShape> R handle(Point2D cursorPosition, RectangularShape source, R destination) {
			Point2D previousCursorPosition = getCursorPosition();
			for (EnumUISide side : getSides()) {
				EnumUIAxis axis = side.getAxis();
				side.getSetter().accept(destination, side.getGetter().apply(source) + (axis.getCoordinate(cursorPosition) - axis.getCoordinate(previousCursorPosition)));
			}
			return destination;
		}

		protected Optional<Point2D> getBase() { return Optional.ofNullable(base); }

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected Set<EnumUISide> getSides() { return sides; }

		protected Point2D getCursorPosition() { return cursorPosition; }
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionRemoved() {
		super.onExtensionRemoved();
		getRenderObserverRotator().close();
	}

	protected static class RenderObserver
			extends LoggingDisposableObserver<UIViewMinecraftBusEvent.Render> {
		private final OptionalWeakReference<UIExtensionComponentUserResizable<?>> owner;

		public RenderObserver(UIExtensionComponentUserResizable<?> owner, Logger logger) {
			super(logger);
			this.owner = new OptionalWeakReference<>(owner);
		}

		@Override
		@SubscribeEvent(priority = EventPriority.LOW, receiveCanceled = true)
		@SuppressWarnings({"rawtypes", "RedundantSuppression"})
		public void onNext(UIViewMinecraftBusEvent.Render event) {
			super.onNext(event);
			if (event.getStage().isPost())
				getOwner()
						.ifPresent(owner ->
								owner.getResizeData()
										.ifPresent(d ->
												owner.getContainer()
														.ifPresent(c -> c.getManager()
																.flatMap(IUIComponentManager::getView)
																.ifPresent(view -> IUIViewComponent.StaticHolder.createComponentContextWithManager(view)
																		.ifPresent(context -> {
																			Point2D previousCursorPosition = event.getCursorPositionView();
																			Rectangle2D resultRectangle = c.getShapeDescriptor().getShapeOutput().getBounds2D();
																			try (IUIComponentContext ctx = context) {
																				view.getPathResolver().resolvePath(ctx, (Point2D) previousCursorPosition.clone(), true);
																				UIObjectUtilities.acceptRectangularShape(
																						d.handle((Point2D) previousCursorPosition.clone(), resultRectangle, resultRectangle),
																						(x, y, w, h) ->
																								resultRectangle.setFrame(x, y, w - 1, h - 1));
																				MinecraftDrawingUtilities.drawRectangle(ctx.getTransformStack().element(),
																						resultRectangle,
																						Color.DARK_GRAY.getRGB(),
																						0); // TODO customize
																			}
																		})))));
		}

		protected Optional<? extends UIExtensionComponentUserResizable<?>> getOwner() { return owner.getOptional(); }
	}

	public class VirtualComponent
			extends UIComponentVirtual
			implements IUIComponentCursorHandleProvider {
		protected boolean beingHovered = false;

		@SuppressWarnings({"OverridableMethodCallDuringObjectConstruction", "rawtypes", "RedundantSuppression"})
		protected VirtualComponent() {
			super(IShapeDescriptor.StaticHolder.getShapeDescriptorPlaceholder());

			addEventListener(EnumUIEventDOMType.MOUSE_ENTER.getEventType(), new UIEventListener.Functional<IUIEventMouse>(evt -> setBeingHovered(true)), false);
			addEventListener(EnumUIEventDOMType.MOUSE_LEAVE.getEventType(), new UIEventListener.Functional<IUIEventMouse>(evt -> setBeingHovered(false)), false);
			addEventListener(EnumUIEventDOMType.MOUSE_DOWN.getEventType(), new UIEventListener.Functional<IUIEventMouse>(evt -> {
				if (evt.getData().getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT && startResizeMaybe(evt.getData().getCursorPositionView())) { // todo custom
					getContainer().ifPresent(c -> {
						c.getParent()
								.ifPresent(p -> p.moveChildToTop(c));
						c.getManager()
								.flatMap(IUIComponentManager::getView)
								.ifPresent(v -> v.getPathResolver().moveVirtualElementToTop(c, this));
					});
					evt.stopPropagation();
				}
			}), false);
			addEventListener(EnumUIEventDOMType.MOUSE_UP.getEventType(), new UIEventListener.Functional<IUIEventMouse>(evt -> {
				if (evt.getData().getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT && finishResizeMaybe(evt.getData().getCursorPositionView()))
					evt.stopPropagation();
			}), false);
		}

		@SuppressWarnings({"rawtypes", "RedundantSuppression"})
		protected boolean startResizeMaybe(Point2D cursorPosition) {
			return getContainer()
					.flatMap(c -> c.getManager()
							.flatMap(IUIComponentManager::getView)
							.flatMap(view -> IUIViewComponent.StaticHolder.createComponentContextWithManager(view)
									.map(context -> {
										Rectangle2D spb;
										try (IUIComponentContext ctx = context) {
											view.getPathResolver().resolvePath(ctx, (Point2D) cursorPosition.clone(), true);
											spb = ctx.getTransformStack().element().createTransformedShape(c.getShapeDescriptor().getShapeOutput()).getBounds2D();
										}
										Set<EnumUISide> sides = EnumUISide.getSidesMouseOver(spb, cursorPosition);

										@Nullable Point2D base = null;
										if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.LEFT))
											base = new Point2D.Double(spb.getMaxX(), spb.getMaxY());
										else if (sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.RIGHT))
											base = new Point2D.Double(spb.getX(), spb.getY());
										else if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.RIGHT))
											base = new Point2D.Double(spb.getX(), spb.getMaxY());
										else if (sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.LEFT))
											base = new Point2D.Double(spb.getMaxX(), spb.getY());

										IResizeData d = new ResizeData(cursorPosition, sides, base, getCursor(sides).orElseThrow(InternalError::new).getHandle());
										synchronized (getLockObject()) {
											if (getResizeData().isPresent())
												return false;
											setResizeData(d);
											return true;
										}
									})))
					.orElse(false);
		}

		protected boolean finishResizeMaybe(Point2D cursorPosition) {
			return getContainer().flatMap(c -> getResizeData().filter(d -> {
				Rectangle2D resultRectangle = c.getShapeDescriptor().getShapeOutput().getBounds2D();
				d.handle((Point2D) cursorPosition.clone(), resultRectangle, resultRectangle);
				synchronized (getLockObject()) {
					if (!getResizeData().isPresent())
						return false;
					c.reshape(s -> s.bound(resultRectangle));
					setResizeData(null);
					return true;
				}
			})).isPresent();
		}

		@Override
		public IShapeDescriptor<?> getShapeDescriptor() {
			return isResizing()
					? getManager()
					.map(m ->
							new GenericShapeDescriptor(m.getShapeDescriptor().getShapeOutput()))
					.orElseGet(() -> new GenericShapeDescriptor(new Rectangle2D.Double()))
					: new GenericShapeDescriptor(getResizeShape()
					.<Shape>map(Function.identity())
					.orElseGet(Rectangle2D.Double::new));
		}

		@Override
		public Optional<? extends Long> getCursorHandle(IUIComponentContext context) {
			Point2D cursorPosition = context.getCursorPositionView();
			@SuppressWarnings("Convert2MethodRef") @Nullable Optional<? extends Long> ret = getResizeData()
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
							.orElseGet(() -> d.getInitialCursorHandle())); // COMMENT compiler bug, long does not get boxed to Long with a method reference

			if (!ret.isPresent())
				ret = getContainer()
						.filter(c -> isBeingHovered())
						.flatMap(c ->
								getCursor(
										EnumUISide.getSidesMouseOver(
												context.getTransformStack().element()
														.createTransformedShape(c.getShapeDescriptor().getShapeOutput()).getBounds2D(), cursorPosition))
										.map(ICursor::getHandle));

			return ret;
		}

		protected boolean isBeingHovered() { return beingHovered; }

		protected void setBeingHovered(boolean beingHovered) { this.beingHovered = beingHovered; }
	}
}
