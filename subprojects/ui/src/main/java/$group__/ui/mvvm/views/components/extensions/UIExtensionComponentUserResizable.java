package $group__.ui.mvvm.views.components.extensions;

import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.ui.core.mvvm.views.IUIReshapeExplicitly;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.mvvm.views.components.extensions.IUIExtensionComponentUserResizable;
import $group__.ui.core.mvvm.views.components.extensions.cursors.IUIComponentCursorHandleProvider;
import $group__.ui.core.mvvm.views.events.IUIEventMouse;
import $group__.ui.core.parsers.components.UIExtensionConstructor;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.events.bus.UIEventBusEntryPoint;
import $group__.ui.events.ui.UIEventListener;
import $group__.ui.mvvm.views.components.UIComponentVirtual;
import $group__.ui.mvvm.views.events.ui.UIEventMouse;
import $group__.ui.structures.EnumCursor;
import $group__.ui.structures.EnumUIAxis;
import $group__.ui.structures.EnumUISide;
import $group__.ui.structures.Point2DImmutable;
import $group__.ui.structures.shapes.descriptors.GenericShapeDescriptor;
import $group__.ui.utilities.UIObjectUtilities;
import $group__.ui.utilities.minecraft.DrawingUtilities;
import $group__.utilities.extensions.ExtensionContainerAware;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.reactive.DisposableObserverAuto;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

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
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class UIExtensionComponentUserResizable<E extends IUIComponent & IUIReshapeExplicitly<? extends IShapeDescriptor<? extends RectangularShape>>>
		extends ExtensionContainerAware<INamespacePrefixedString, IUIComponent, E>
		implements IUIExtensionComponentUserResizable<E> {
	public static final int RESIZE_BORDER_THICKNESS_DEFAULT = 10;
	protected final int resizeBorderThickness = RESIZE_BORDER_THICKNESS_DEFAULT; // TODO make this a property and strategy or something like that
	protected final Object lockObject = new Object();
	protected final VirtualComponent virtualComponent = new VirtualComponent();
	@Nullable
	protected IResizeData resizeData;

	@UIExtensionConstructor(type = UIExtensionConstructor.EnumConstructorType.CONTAINER_CLASS)
	public UIExtensionComponentUserResizable(Class<E> containerClass) {
		super(IUIComponent.class, containerClass);
	}

	protected static Optional<EnumCursor> getCursor(Set<? extends EnumUISide> sides) {
		@Nullable EnumCursor cursor = null;
		if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.LEFT)
				|| sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.RIGHT))
			cursor = EnumCursor.EXTENSION_RESIZE_NW_SE_CURSOR;
		else if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.RIGHT)
				|| sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.LEFT))
			cursor = EnumCursor.EXTENSION_RESIZE_NE_SW_CURSOR;
		else if (sides.contains(EnumUISide.LEFT) || sides.contains(EnumUISide.RIGHT))
			cursor = EnumCursor.STANDARD_RESIZE_HORIZONTAL_CURSOR;
		else if (sides.contains(EnumUISide.UP) || sides.contains(EnumUISide.DOWN))
			cursor = EnumCursor.STANDARD_RESIZE_VERTICAL_CURSOR;
		return Optional.ofNullable(cursor);
	}

	@Override
	public IType<? extends INamespacePrefixedString, ?, ? extends IUIComponent> getType() { return TYPE.getValue(); }

	protected final AtomicReference<ObserverDrawScreenEventPost> observerDrawScreenEventPost = new AtomicReference<>();

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionAdded(IUIComponent container) {
		super.onExtensionAdded(container);
		getContainer()
				.ifPresent(c -> c.getManager()
						.flatMap(IUIComponentManager::getView)
						.ifPresent(v -> {
							getVirtualComponent().setRelatedComponent(c);
							v.getPathResolver().addVirtualElement(c, getVirtualComponent());
						}));
		UIEventBusEntryPoint.<GuiScreenEvent.DrawScreenEvent.Post>getEventBus()
				.subscribe(getObserverDrawScreenEventPost().accumulateAndGet(new ObserverDrawScreenEventPost(), (p, n) -> {
					if (p != null)
						p.dispose();
					return n;
				}));
	}

	@SuppressWarnings("ReturnOfInnerClass")
	protected VirtualComponent getVirtualComponent() { return virtualComponent; }

	@Override
	public Optional<? extends Shape> getResizeShape() {
		return getContainer().map(c -> {
			Rectangle2D spb = c.getShapeDescriptor().getShapeOutput().getBounds2D();
			Area ret = new Area(UIObjectUtilities.applyRectangular(spb, (x, y, w, h) ->
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
			this.base = (Point2D) Optional.ofNullable(base).map(Point2D::clone).orElse(null);
			this.initialCursorHandle = initialCursorHandle;
		}

		@Override
		public Point2D getCursorPositionView() { return (Point2D) getCursorPosition().clone(); }

		@Override
		public Set<? extends EnumUISide> getSidesView() { return EnumSet.copyOf(getSides()); }

		@Override
		public Optional<? extends Point2D> getBaseView() { return getBase().map(p -> (Point2D) p.clone()); }

		@Override
		public long getInitialCursorHandle() { return initialCursorHandle; }

		@Override
		public void handle(RectangularShape rectangular, Point2D cursorPosition) {
			Point2D o = getCursorPosition();
			for (EnumUISide side : getSides()) {
				EnumUIAxis axis = side.getAxis();
				side.getSetter().accept(rectangular, side.getGetter().apply(rectangular) + (axis.getCoordinate(cursorPosition) - axis.getCoordinate(o)));
			}
		}

		protected Optional<Point2D> getBase() { return Optional.ofNullable(base); }

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected Set<EnumUISide> getSides() { return sides; }

		protected Point2D getCursorPosition() { return cursorPosition; }
	}

	public class VirtualComponent
			extends UIComponentVirtual
			implements IUIComponentCursorHandleProvider {
		protected boolean beingHovered = false;

		@SuppressWarnings("OverridableMethodCallDuringObjectConstruction")
		protected VirtualComponent() {
			super(IShapeDescriptor.getShapeDescriptorPlaceholderCopy());

			addEventListener(UIEventMouse.TYPE_MOUSE_ENTER, new UIEventListener.Functional<IUIEventMouse>(evt -> setBeingHovered(true)), false);
			addEventListener(UIEventMouse.TYPE_MOUSE_LEAVE, new UIEventListener.Functional<IUIEventMouse>(evt -> setBeingHovered(false)), false);
			addEventListener(UIEventMouse.TYPE_MOUSE_DOWN, new UIEventListener.Functional<IUIEventMouse>(evt -> {
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
			addEventListener(UIEventMouse.TYPE_MOUSE_UP, new UIEventListener.Functional<IUIEventMouse>(evt -> {
				if (evt.getData().getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT && finishResizeMaybe(evt.getData().getCursorPositionView()))
					evt.stopPropagation();
			}), false);
		}

		protected boolean startResizeMaybe(Point2D cursorPosition) {
			return getContainer()
					.flatMap(c -> c.getManager()
							.flatMap(IUIComponentManager::getView)
							.filter(v -> {
								Rectangle2D spb = v.getPathResolver().resolvePath(cursorPosition, true).getTransformCurrentView().createTransformedShape(c.getShapeDescriptor().getShapeOutput()).getBounds2D();
								Set<EnumUISide> sides = EnumUISide.getSidesMouseOver(spb, cursorPosition);

								@Nullable Point2D base = null;
								if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.LEFT))
									base = new Point2DImmutable(spb.getMaxX(), spb.getMaxY());
								else if (sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.RIGHT))
									base = new Point2DImmutable(spb.getX(), spb.getY());
								else if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.RIGHT))
									base = new Point2DImmutable(spb.getX(), spb.getMaxY());
								else if (sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.LEFT))
									base = new Point2DImmutable(spb.getMaxX(), spb.getY());

								IResizeData d = new ResizeData(cursorPosition, sides, base, getCursor(sides).orElseThrow(InternalError::new).getHandle());
								synchronized (getLockObject()) {
									if (getResizeData().isPresent())
										return false;
									setResizeData(d);
									return true;
								}
							})).isPresent();
		}

		protected boolean finishResizeMaybe(Point2D cursorPosition) {
			return getContainer().flatMap(c -> getResizeData().filter(d -> {
				Rectangle2D r = c.getShapeDescriptor().getShapeOutput().getBounds2D();
				d.handle(r, cursorPosition);
				synchronized (getLockObject()) {
					if (!getResizeData().isPresent())
						return false;
					c.reshape(s -> s.bound(r));
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
		public Optional<? extends Long> getCursorHandle(IAffineTransformStack stack, Point2D cursorPosition) {
			@SuppressWarnings("Convert2MethodRef") @Nullable Long ret = getResizeData()
					.map(d -> d.getBaseView()
							.map(b -> {
								Set<EnumUISide> sides = EnumUISide.getSidesMouseOver(
										new Rectangle2D.Double(b.getX(), b.getY(), 0, 0),
										cursorPosition);
								if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.LEFT)
										|| sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.RIGHT))
									return EnumCursor.EXTENSION_RESIZE_NW_SE_CURSOR.getHandle();
								else if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.RIGHT)
										|| sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.LEFT))
									return EnumCursor.EXTENSION_RESIZE_NE_SW_CURSOR.getHandle();
								return null;
							})
							.orElseGet(() -> d.getInitialCursorHandle())) // COMMENT compiler bug, long does not get boxed to Long with a method reference
					.orElse(null);

			if (ret == null)
				ret = getContainer()
						.filter(c -> isBeingHovered())
						.flatMap(c ->
								getCursor(EnumUISide.getSidesMouseOver(stack.element().createTransformedShape(c.getShapeDescriptor().getShapeOutput()).getBounds2D(), cursorPosition))
										.map(EnumCursor::getHandle))
						.orElse(null);

			return Optional.ofNullable(ret);
		}

		protected boolean isBeingHovered() { return beingHovered; }

		protected void setBeingHovered(boolean beingHovered) { this.beingHovered = beingHovered; }
	}

	protected AtomicReference<ObserverDrawScreenEventPost> getObserverDrawScreenEventPost() { return observerDrawScreenEventPost; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionRemoved() {
		super.onExtensionRemoved();
		Optional.ofNullable(getObserverDrawScreenEventPost().getAndSet(null)).ifPresent(DisposableObserver::dispose);
	}

	public class ObserverDrawScreenEventPost
			extends DisposableObserverAuto<GuiScreenEvent.DrawScreenEvent.Post> {
		@Override
		@SubscribeEvent(priority = EventPriority.LOW, receiveCanceled = true)
		public void onNext(GuiScreenEvent.DrawScreenEvent.Post event) {
			getResizeData()
					.ifPresent(d ->
							getContainer()
									.ifPresent(c -> {
										Point2D cp = new Point2DImmutable(event.getMouseX(), event.getMouseY());
										c.getManager()
												.flatMap(IUIComponentManager::getView)
												.ifPresent(m -> {
													Rectangle2D r = c.getShapeDescriptor().getShapeOutput().getBounds2D();
													d.handle(r, cp);
													UIObjectUtilities.acceptRectangular(r, (x, y, w, h) ->
															r.setFrame(x, y, w - 1, h - 1));
													DrawingUtilities.drawRectangle(m.getPathResolver().resolvePath(cp, true).getTransformCurrentView(),
															r, Color.DARK_GRAY.getRGB(), 0); // TODO customize
												});
									}));
		}
	}
}
