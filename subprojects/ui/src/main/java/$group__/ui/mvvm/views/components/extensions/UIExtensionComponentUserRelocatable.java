package $group__.ui.mvvm.views.components.extensions;

import $group__.ui.UIConfiguration;
import $group__.ui.core.mvvm.views.IUIReshapeExplicitly;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.mvvm.views.components.extensions.IUIExtensionComponentUserRelocatable;
import $group__.ui.core.mvvm.views.components.extensions.cursors.IUIComponentCursorHandleProvider;
import $group__.ui.core.mvvm.views.events.IUIEventMouse;
import $group__.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import $group__.ui.core.parsers.components.UIExtensionConstructor;
import $group__.ui.core.structures.IUIComponentContext;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.events.bus.UIEventBusEntryPoint;
import $group__.ui.events.ui.UIEventListener;
import $group__.ui.minecraft.mvvm.events.bus.UIViewMinecraftBusEvent;
import $group__.ui.mvvm.views.components.UIComponentVirtual;
import $group__.ui.structures.ImmutablePoint2D;
import $group__.ui.structures.shapes.descriptors.GenericShapeDescriptor;
import $group__.ui.utilities.UIObjectUtilities;
import $group__.ui.utilities.minecraft.DrawingUtilities;
import $group__.utilities.AutoCloseableRotator;
import $group__.utilities.extensions.AbstractContainerAwareExtension;
import $group__.utilities.extensions.core.IExtensionType;
import $group__.utilities.reactive.LoggingDisposableObserver;
import $group__.utilities.references.OptionalWeakReference;
import $group__.utilities.structures.INamespacePrefixedString;
import io.reactivex.rxjava3.disposables.Disposable;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.Optional;
import java.util.function.Function;

public class UIExtensionComponentUserRelocatable<E extends IUIComponent & IUIReshapeExplicitly<? extends IShapeDescriptor<? extends RectangularShape>>>
		extends AbstractContainerAwareExtension<INamespacePrefixedString, IUIComponent, E>
		implements IUIExtensionComponentUserRelocatable<E> {
	public static final int RELOCATE_BORDER_THICKNESS_DEFAULT = 10;
	private final int relocateBorderThickness = RELOCATE_BORDER_THICKNESS_DEFAULT; // TODO make this a property and strategy or something like that
	private final Object lockObject = new Object();
	private final VirtualComponent virtualComponent = new VirtualComponent();
	@Nullable
	protected IRelocateData relocateData;
	@SuppressWarnings("ThisEscapedInObjectConstruction")
	private final AutoCloseableRotator<RenderObserver, RuntimeException> renderObserverRotator =
			new AutoCloseableRotator<>(() -> new RenderObserver(this, UIConfiguration.getInstance().getLogger()), Disposable::dispose);

	@UIExtensionConstructor(type = UIExtensionConstructor.EnumConstructorType.CONTAINER_CLASS)
	public UIExtensionComponentUserRelocatable(Class<E> containerClass) {
		super(IUIComponent.class, containerClass);
	}

	@Override
	public IExtensionType<INamespacePrefixedString, ?, IUIComponent> getType() { return TYPE.getValue(); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionAdded(IUIComponent container) {
		super.onExtensionAdded(container);
		getContainer().ifPresent(c -> {
			getVirtualComponent().setRelatedComponent(c);
			c.getManager()
					.flatMap(IUIComponentManager::getView)
					.ifPresent(v -> v.getPathResolver().addVirtualElement(c, getVirtualComponent()));
		});
		UIEventBusEntryPoint.<UIViewMinecraftBusEvent.Render>getEventBus().subscribe(getRenderObserverRotator().get().orElseThrow(AssertionError::new));
	}

	protected AutoCloseableRotator<RenderObserver, RuntimeException> getRenderObserverRotator() { return renderObserverRotator; }

	@SuppressWarnings("ReturnOfInnerClass")
	protected VirtualComponent getVirtualComponent() { return virtualComponent; }

	@Override
	public Optional<? extends Shape> getRelocateShape() {
		return getContainer().map(c ->
				UIObjectUtilities.applyRectangular(c.getShapeDescriptor().getShapeOutput().getBounds2D(),
						(x, y, w, h) -> new Rectangle2D.Double(x, y, w, getRelocateBorderThickness())));
	}

	@Override
	public Optional<? extends IRelocateData> getRelocateData() { return Optional.ofNullable(relocateData); }

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

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionRemoved() {
		super.onExtensionRemoved();
		getContainer().ifPresent(c -> {
			getVirtualComponent().setRelatedComponent(null);
			c.getManager()
					.flatMap(IUIComponentManager::getView)
					.ifPresent(v -> v.getPathResolver().removeVirtualElement(c, getVirtualComponent()));
		});
		getRenderObserverRotator().close();
	}

	protected static class RenderObserver
			extends LoggingDisposableObserver<UIViewMinecraftBusEvent.Render> {
		private final OptionalWeakReference<UIExtensionComponentUserRelocatable<?>> owner;

		public RenderObserver(UIExtensionComponentUserRelocatable<?> owner, Logger logger) {
			super(logger);
			this.owner = new OptionalWeakReference<>(owner);
		}

		@Override
		@SubscribeEvent(priority = EventPriority.LOW, receiveCanceled = true)
		public void onNext(UIViewMinecraftBusEvent.Render event) {
			super.onNext(event);
			if (event.getStage().isPost())
				getOwner().ifPresent(owner ->
						owner.getRelocateData()
								.ifPresent(d -> owner.getContainer()
										.ifPresent(c -> c.getManager()
												.flatMap(IUIComponentManager::getView)
												.ifPresent(v -> {
													ImmutablePoint2D cp = event.getCursorPositionView();
													Rectangle2D r = c.getShapeDescriptor().getShapeOutput().getBounds2D();
													d.handle(r, cp);
													try (IUIComponentContext context = v.createContext()) {
														v.getPathResolver().resolvePath(context, cp, true);
														DrawingUtilities.drawRectangle(context.getTransformStack().element(),
																r, Color.DARK_GRAY.getRGB(), 0); // TODO customize
													}
												}))));
		}

		protected Optional<? extends UIExtensionComponentUserRelocatable<?>> getOwner() { return owner.getOptional(); }
	}

	public class VirtualComponent
			extends UIComponentVirtual
			implements IUIComponentCursorHandleProvider {
		@SuppressWarnings("OverridableMethodCallDuringObjectConstruction")
		protected VirtualComponent() {
			super(IShapeDescriptor.StaticHolder.getShapeDescriptorPlaceholder());

			addEventListener(EnumUIEventDOMType.MOUSE_DOWN.getEventType(), new UIEventListener.Functional<IUIEventMouse>(evt -> {
				if (evt.getData().getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT && startRelocateMaybe(evt.getData().getCursorPositionView())) { // todo custom
					getContainer().ifPresent(c -> {
						c.getParent().ifPresent(p ->
								p.moveChildToTop(c));
						c.getManager()
								.flatMap(IUIComponentManager::getView)
								.ifPresent(v -> v.getPathResolver().moveVirtualElementToTop(c, this));
					});
					evt.stopPropagation();
				}
			}), false);
			addEventListener(EnumUIEventDOMType.MOUSE_UP.getEventType(), new UIEventListener.Functional<IUIEventMouse>(evt -> {
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
		public Optional<? extends Long> getCursorHandle(IUIComponentContext context, Point2D cursorPosition) {
			return isRelocating() ? Optional.of(MemoryUtil.NULL) : Optional.empty();
		}

		@Override
		public IShapeDescriptor<?> getShapeDescriptor() {
			return isRelocating()
					? getManager()
					.map(m -> new GenericShapeDescriptor(m.getShapeDescriptor().getShapeOutput()))
					.orElseGet(() -> new GenericShapeDescriptor(new Rectangle2D.Double()))
					: new GenericShapeDescriptor(getRelocateShape()
					.<Shape>map(Function.identity())
					.orElseGet(Rectangle2D.Double::new));
		}
	}
}
