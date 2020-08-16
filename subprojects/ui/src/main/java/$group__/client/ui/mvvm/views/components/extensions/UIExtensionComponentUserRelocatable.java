package $group__.client.ui.mvvm.views.components.extensions;

import $group__.client.ui.events.bus.EventBusEntryPoint;
import $group__.client.ui.events.ui.UIEventListener;
import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.structures.IShapeDescriptor;
import $group__.client.ui.mvvm.core.views.IUIReshapeExplicitly;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.components.extensions.IUIExtensionComponentUserRelocatable;
import $group__.client.ui.mvvm.core.views.components.extensions.cursors.IUIComponentCursorHandleProvider;
import $group__.client.ui.mvvm.core.views.events.IUIEventMouse;
import $group__.client.ui.mvvm.structures.ShapeDescriptor;
import $group__.client.ui.mvvm.views.components.UIComponentVirtual;
import $group__.client.ui.mvvm.views.events.ui.UIEventMouse;
import $group__.client.ui.utilities.UIObjectUtilities;
import $group__.client.ui.utilities.minecraft.DrawingUtilities;
import $group__.utilities.extensions.ExtensionContainerAware;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.Optional;
import java.util.function.Function;

public class UIExtensionComponentUserRelocatable<E extends IUIComponent & IUIReshapeExplicitly<? extends IShapeDescriptor<? extends RectangularShape>>>
		extends ExtensionContainerAware<ResourceLocation, IUIComponent, E>
		implements IUIExtensionComponentUserRelocatable<E> {
	public static final int RELOCATE_BORDER_THICKNESS_DEFAULT = 10;
	protected final int relocateBorderThickness = RELOCATE_BORDER_THICKNESS_DEFAULT; // TODO make this a property and strategy or something like that
	protected final Object lockObject = new Object();
	protected final VirtualComponent virtualComponent = new VirtualComponent();
	@Nullable
	protected IRelocateData relocateData;

	public UIExtensionComponentUserRelocatable(Class<E> extendedClass) {
		super(IUIComponent.class, extendedClass);
	}

	@Override
	public IType<? extends ResourceLocation, ?, ? extends IUIComponent> getType() { return TYPE.getValue(); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionAdded(IUIComponent container) {
		super.onExtensionAdded(container);
		getContainer().ifPresent(c -> {
			getVirtualComponent().setRelatedComponent(c);
			c.getManager().ifPresent(m ->
					m.getPathResolver().addVirtualElement(c, getVirtualComponent()));
		});
		EventBusEntryPoint.INSTANCE.register(this);
	}

	protected VirtualComponent getVirtualComponent() { return virtualComponent; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionRemoved() {
		super.onExtensionRemoved();
		getContainer().ifPresent(c -> {
			getVirtualComponent().setRelatedComponent(null);
			c.getManager().ifPresent(m ->
					m.getPathResolver().removeVirtualElement(c, getVirtualComponent()));
		});
		EventBusEntryPoint.INSTANCE.unregister(this);
	}

	@SubscribeEvent(priority = EventPriority.LOW, receiveCanceled = true)
	protected void onDrawScreenPost(GuiScreenEvent.DrawScreenEvent.Post event) {
		getRelocateData().ifPresent(d -> getContainer().ifPresent(c -> c.getManager().ifPresent(m -> {
			Point2D cp = new Point2D.Double(event.getMouseX(), event.getMouseY());
			Rectangle2D r = c.getShapeDescriptor().getShapeOutput().getBounds2D();
			d.handle(r, cp);
			DrawingUtilities.drawRectangle(m.getPathResolver().resolvePath(cp, true).getTransformCurrentView(),
					r, Color.DARK_GRAY.getRGB(), 0); // TODO customize
		})));
	}

	@Override
	public Optional<? extends Shape> getRelocateShape() {
		return getContainer().map(c ->
				UIObjectUtilities.applyRectangular(c.getShapeDescriptor().getShapeOutput().getBounds2D(),
						(x, y, w, h) -> new Rectangle2D.Double(x, y, w, getRelocateBorderThickness())));
	}

	@Override
	public Optional<IRelocateData> getRelocateData() { return Optional.ofNullable(relocateData); }

	protected void setRelocateData(@Nullable IRelocateData relocateData) { this.relocateData = relocateData; }

	public int getRelocateBorderThickness() { return relocateBorderThickness; }

	protected Object getLockObject() { return lockObject; }

	public static class RelocateData implements IRelocateData {
		protected final Point2D cursorPosition;

		public RelocateData(Point2D cursorPosition) {
			this.cursorPosition = (Point2D) cursorPosition.clone();
		}

		@Override
		public Point2D getCursorPositionView() { return (Point2D) getCursorPosition().clone(); }

		protected Point2D getCursorPosition() { return cursorPosition; }

		@Override
		public void handle(RectangularShape rectangular, Point2D cursorPosition) {
			Point2D o = getCursorPosition();
			rectangular.setFrame(rectangular.getX() + (cursorPosition.getX() - o.getX()), rectangular.getY() + (cursorPosition.getY() - o.getY()),
					rectangular.getWidth(), rectangular.getHeight());
		}
	}

	protected class VirtualComponent
			extends UIComponentVirtual
			implements IUIComponentCursorHandleProvider {
		@SuppressWarnings("OverridableMethodCallDuringObjectConstruction")
		protected VirtualComponent() {
			addEventListener(UIEventMouse.TYPE_MOUSE_DOWN, new UIEventListener.Functional<IUIEventMouse>(evt -> {
				if (evt.getData().getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT && startRelocateMaybe(evt.getData().getCursorPositionView())) // todo custom
					evt.stopPropagation();
			}), false);
			addEventListener(UIEventMouse.TYPE_MOUSE_UP, new UIEventListener.Functional<IUIEventMouse>(evt -> {
				if (evt.getData().getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT && finishRelocateMaybe(evt.getData().getCursorPositionView()))
					evt.stopPropagation();
			}), false);
		}

		protected boolean startRelocateMaybe(Point2D cursorPosition) {
			return getContainer().map(c -> {
				IRelocateData d = new RelocateData(cursorPosition);
				synchronized (getLockObject()) {
					if (getRelocateData().isPresent())
						return false;
					setRelocateData(d);
					return true;
				}
			}).orElse(false);
		}

		protected boolean finishRelocateMaybe(Point2D cursorPosition) {
			return getContainer().flatMap(c -> getRelocateData().filter(d -> {
				Rectangle2D r = c.getShapeDescriptor().getShapeOutput().getBounds2D();
				d.handle(r, cursorPosition);
				synchronized (getLockObject()) {
					if (!getRelocateData().isPresent())
						return false;
					c.reshape(s -> s.bound(r));
					setRelocateData(null);
					return true;
				}
			})).isPresent();
		}

		@Override
		public Optional<Long> getCursorHandle(IAffineTransformStack stack, Point2D cursorPosition) {
			if (isRelocating())
				return Optional.of(MemoryUtil.NULL);
			else
				return Optional.empty();
		}

		@Override
		public IShapeDescriptor<?> getShapeDescriptor() {
			if (isRelocating())
				return getManager()
						.map(m ->
								new ShapeDescriptor.Generic(m.getShapeDescriptor().getShapeOutput()))
						.orElseGet(() -> new ShapeDescriptor.Generic(new Rectangle2D.Double()));
			else
				return new ShapeDescriptor.Generic(getRelocateShape()
						.<Shape>map(Function.identity())
						.orElseGet(Rectangle2D.Double::new));
		}
	}
}
