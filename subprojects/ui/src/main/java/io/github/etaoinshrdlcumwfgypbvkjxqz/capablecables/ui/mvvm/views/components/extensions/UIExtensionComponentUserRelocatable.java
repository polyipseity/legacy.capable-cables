package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIExtensionComponentUserRelocatable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.cursors.IUIComponentCursorHandleProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventMouse;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIExtensionConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.ui.UIEventListener;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.events.bus.UIViewMinecraftBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.UINullVirtualComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.rendering.DefaultUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.rendering.NullUIRenderer;
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
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class UIExtensionComponentUserRelocatable<E extends IUIComponent & IUIReshapeExplicitly<? extends IShapeDescriptor<? extends RectangularShape>>>
		extends AbstractContainerAwareExtension<INamespacePrefixedString, IUIComponent, E>
		implements IUIExtensionComponentUserRelocatable<E> {
	public static final int RELOCATE_BORDER_THICKNESS_DEFAULT = 10;
	private final int relocateBorderThickness = RELOCATE_BORDER_THICKNESS_DEFAULT; // TODO make this a property and strategy or something like that
	private final VirtualComponent virtualComponent = new VirtualComponent();
	@Nullable
	protected IRelocateData relocateData;
	@SuppressWarnings("ThisEscapedInObjectConstruction")
	private final AutoCloseableRotator<RenderObserver, RuntimeException> renderObserverRotator =
			new AutoCloseableRotator<>(() -> new RenderObserver(this, UIConfiguration.getInstance().getLogger()), Disposable::dispose);
	private final IUIRendererContainer<IRelocatingRenderer> rendererContainer =
			new DefaultUIRendererContainer<>(new DefaultRelocatingRenderer(ImmutableMap.of()));

	@UIExtensionConstructor(type = UIExtensionConstructor.EnumConstructorType.CONTAINER_CLASS)
	public UIExtensionComponentUserRelocatable(Class<E> containerClass) {
		super(IUIComponent.class, containerClass);
	}

	@Override
	public Optional<? extends IRelocatingRenderer> getRenderer() {
		return getRendererContainer().getRenderer();
	}

	@Override
	public IExtensionType<INamespacePrefixedString, ?, IUIComponent> getType() { return TYPE.getValue(); }

	@Override
	@OverridingMethodsMustInvokeSuper
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	public void onExtensionAdded(IUIComponent container) {
		super.onExtensionAdded(container);
		getContainer().ifPresent(c -> {
			getVirtualComponent().setRelatedComponent(c);
			c.getManager()
					.flatMap(IUIComponentManager::getView)
					.ifPresent(v -> v.getPathResolver().addVirtualElement(c, getVirtualComponent()));
		});
		UIEventBusEntryPoint.<UIViewMinecraftBusEvent.Render>getEventBus().subscribe(getRenderObserverRotator().get());
	}

	protected AutoCloseableRotator<RenderObserver, RuntimeException> getRenderObserverRotator() { return renderObserverRotator; }

	@SuppressWarnings("ReturnOfInnerClass")
	protected VirtualComponent getVirtualComponent() { return virtualComponent; }

	@Override
	public Optional<? extends Shape> getRelocateShape() {
		return getContainer().map(c ->
				UIObjectUtilities.applyRectangularShape(c.getShapeDescriptor().getShapeOutput().getBounds2D(),
						(x, y, w, h) -> new Rectangle2D.Double(x, y, w, getRelocateBorderThickness())));
	}

	@Override
	public Optional<? extends IRelocateData> getRelocateData() { return Optional.ofNullable(relocateData); }

	protected void setRelocateData(@Nullable IRelocateData relocateData) { this.relocateData = relocateData; }

	public int getRelocateBorderThickness() { return relocateBorderThickness; }

	protected IUIRendererContainer<IRelocatingRenderer> getRendererContainer() { return rendererContainer; }

	@Override
	@Deprecated
	public void setRenderer(@Nullable IRelocatingRenderer renderer) {
		StaticHolder.setRendererImpl(this, renderer, getRendererContainer()::setRenderer);
	}

	@Override
	public Class<? extends IRelocatingRenderer> getDefaultRendererClass() {
		return getRendererContainer().getDefaultRendererClass();
	}

	public static class RelocateData implements IRelocateData {
		protected final Point2D cursorPosition;

		public RelocateData(Point2D cursorPosition) {
			this.cursorPosition = (Point2D) cursorPosition.clone();
		}

		@Override
		public Point2D getCursorPositionView() { return (Point2D) getCursorPosition().clone(); }

		protected Point2D getCursorPosition() { return cursorPosition; }

		@Override
		public <R extends RectangularShape> R handle(Point2D cursorPosition, RectangularShape source, R destination) {
			Point2D previousCursorPosition = getCursorPosition();
			destination.setFrame(
					source.getX() + (cursorPosition.getX() - previousCursorPosition.getX()),
					source.getY() + (cursorPosition.getY() - previousCursorPosition.getY()),
					source.getWidth(),
					source.getHeight());
			return destination;
		}
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
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
		@SuppressWarnings({"rawtypes", "RedundantSuppression"})
		public void onNext(UIViewMinecraftBusEvent.Render event) {
			// TODO render locally
			super.onNext(event);
			if (event.getStage().isPost())
				getOwner().ifPresent(owner ->
						owner.getRelocateData().ifPresent(data ->
								owner.getRenderer().ifPresent(renderer ->
										owner.getContainer().ifPresent(container -> container.getManager()
												.flatMap(IUIComponentManager::getView)
												.ifPresent(view -> IUIViewComponent.StaticHolder.createComponentContextWithManager(view)
														.ifPresent(context -> {
															try (IUIComponentContext ctx = context) {
																Point2D previousCursorPosition = data.getCursorPositionView();
																view.getPathResolver().resolvePath(ctx, (Point2D) previousCursorPosition.clone(), false);
																ctx.getPath().getPathEnd()
																		.filter(Predicate.isEqual(container))
																		.ifPresent(pathEnd -> renderer.render(ctx, data));
															}
														})
												)
										)
								)
						)
				);
		}

		protected Optional<? extends UIExtensionComponentUserRelocatable<?>> getOwner() { return owner.getOptional(); }
	}

	protected static class DefaultRelocatingRenderer
			extends NullUIRenderer<IUIExtensionComponentUserRelocatable<?>>
			implements IRelocatingRenderer {
		@UIRendererConstructor(type = UIRendererConstructor.EnumConstructorType.MAPPINGS)
		protected DefaultRelocatingRenderer(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings) {
			super(mappings, CastUtilities.castUnchecked(IUIExtensionComponentUserRelocatable.class));
		}

		@Override
		public void render(IUIComponentContext context, IRelocateData data) {
			IUIComponent container = context.getPath().getPathEnd().orElseThrow(AssertionError::new);
			Point2D currentCursorPosition = context.getCursorPositionView();
			Rectangle2D resultRectangle = container.getShapeDescriptor().getShapeOutput().getBounds2D();
			MinecraftDrawingUtilities.drawRectangle(context.getTransformStack().element(),
					data.handle((Point2D) currentCursorPosition.clone(), resultRectangle, resultRectangle),
					Color.DARK_GRAY.getRGB(),
					0); // TODO customize
		}
	}

	protected class VirtualComponent
			extends UINullVirtualComponent
			implements IUIComponentCursorHandleProvider {
		// TODO make static
		private final Object lockObject = new Object();

		protected boolean startRelocateMaybe(Point2D cursorPosition) {
			return getContainer().map(c -> {
				IRelocateData d = new RelocateData(cursorPosition);
				synchronized (getLockObject()) {
					if (getRelocateData().isPresent())
						return false;
					setRelocateData(d);
				}
				return true;
			}).orElse(false);
		}

		@SuppressWarnings({"OverridableMethodCallDuringObjectConstruction", "rawtypes", "RedundantSuppression"})
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

		protected Object getLockObject() { return lockObject; }

		protected boolean finishRelocateMaybe(Point2D cursorPosition) {
			return getContainer().flatMap(c -> getRelocateData().filter(d -> {
				Rectangle2D resultRectangle = c.getShapeDescriptor().getShapeOutput().getBounds2D();
				d.handle((Point2D) cursorPosition.clone(), resultRectangle, resultRectangle);
				boolean ret;
				synchronized (getLockObject()) {
					if (!getRelocateData().isPresent())
						return false;
					ret = c.reshape(s -> s.bound(resultRectangle));
					setRelocateData(null);
				}
				return ret;
			})).isPresent();
		}

		@Override
		public Optional<? extends Long> getCursorHandle(IUIComponentContext context) {
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
