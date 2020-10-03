package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions.relocatable;

import com.google.common.collect.ImmutableMap;
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
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIExtensionConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.ui.FunctionalUIEventListener;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.modifiers.AbstractUIVirtualComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.rendering.DefaultUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.AbstractContainerAwareExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.optionals.Optional2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ui.UIObjectUtilities;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.Optional;

public class UITeleportingComponentUserRelocatableExtension<E extends IUIComponent & IUIReshapeExplicitly<? extends IShapeDescriptor<? extends RectangularShape>>>
		extends AbstractContainerAwareExtension<INamespacePrefixedString, IUIComponent, E>
		implements IUIComponentUserRelocatableExtension<E> {
	public static final int RELOCATE_BORDER_THICKNESS_DEFAULT = 10;
	private final int relocateBorderThickness = RELOCATE_BORDER_THICKNESS_DEFAULT; // TODO make this a property and strategy or something like that
	@SuppressWarnings("ThisEscapedInObjectConstruction")
	private final Modifier modifier = new Modifier(this);
	private final IUIRendererContainer<IRelocatingRenderer> rendererContainer =
			new DefaultUIRendererContainer<>(new UIComponentUserRelocatableExtensionNullRelocatingRenderer(ImmutableMap.of()));
	@Nullable
	protected IRelocateData relocateData;

	@UIExtensionConstructor(type = UIExtensionConstructor.EnumConstructorType.CONTAINER_CLASS)
	public UITeleportingComponentUserRelocatableExtension(Class<E> containerClass) {
		super(IUIComponent.class, containerClass);
	}

	@Override
	public Optional<? extends IRelocatingRenderer> getRenderer() {
		return getRendererContainer().getRenderer();
	}

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

	@Override
	public IExtensionType<INamespacePrefixedString, ?, IUIComponent> getType() { return TYPE.getValue(); }

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
			extends AbstractUIVirtualComponent
			implements IUIComponentCursorHandleProviderModifier, IUIComponentRendererInvokerModifier {
		private final OptionalWeakReference<UITeleportingComponentUserRelocatableExtension<?>> owner;
		private final Object lockObject = new Object();

		@SuppressWarnings({"OverridableMethodCallDuringObjectConstruction", "rawtypes", "RedundantSuppression"})
		protected Modifier(UITeleportingComponentUserRelocatableExtension<?> owner) {
			super(IShapeDescriptor.StaticHolder.getShapeDescriptorPlaceholder());
			this.owner = new OptionalWeakReference<>(owner);

			addEventListener(EnumUIEventDOMType.MOUSE_DOWN.getEventType(), new FunctionalUIEventListener<IUIEventMouse>(evt -> {
				if (evt.getData().getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT && startRelocateMaybe(evt.getViewContextView(), evt.getData().getCursorPositionView())) { // todo custom
					getOwner()
							.flatMap(owner2 -> owner2.getContainer()) // TODO Java 9 - IllegalAccessError now, make method ref
							.ifPresent(c -> {
								c.getParent().ifPresent(p ->
										p.moveChildToTop(c));
								c.moveModifierToTop(this);
							});
				}
			}), false);
			addEventListener(EnumUIEventDOMType.MOUSE_UP.getEventType(), new FunctionalUIEventListener<IUIEventMouse>(evt -> {
				if (evt.getData().getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT && finishRelocateMaybe(evt.getViewContextView(), evt.getData().getCursorPositionView()))
					evt.stopPropagation();
			}), false);
		}

		protected boolean startRelocateMaybe(@SuppressWarnings("unused") IUIViewContext viewContext, Point2D point) {
			return getOwner().map(owner -> {
				IRelocateData d = new DefaultRelocateData(point);
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
							ret = container.reshape(s -> s.bound(resultRectangle));
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
									.map(shape -> IUIComponentContext.StaticHolder.createContextualShape(context, shape))
									.map(shape -> shape.contains(point)))
					.orElse(false);
		}

		@Override
		public void invokeRenderer(IUIComponentContext context) {
			if (getModifyStage().isPost()) {
				getOwner().ifPresent(owner ->
						Optional2.of(owner.getRenderer().orElse(null), owner.getRelocateData().orElse(null))
								.ifPresent(values -> {
									IRelocatingRenderer renderer = values.getValue1Nonnull();
									IRelocateData data = values.getValue2Nonnull();
									renderer.render(context, data);
								}));
			}
		}
	}
}
