package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.extensions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUISubInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIExtensionConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.extensions.IUIExtensionBackgroundRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.extensions.IUIExtensionScreenProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.events.bus.UIViewMinecraftBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.utilities.minecraft.UIBackgrounds;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.AbstractContainerAwareExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.LoggingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.geom.Point2D;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

@OnlyIn(Dist.CLIENT)
public class UIExtensionBackgroundMinecraft<E extends IUIComponentManager<?>>
		extends AbstractContainerAwareExtension<INamespacePrefixedString, IUIComponentManager<?>, E>
		implements IUIExtensionBackgroundRenderer {
	@UIExtensionConstructor(type = UIExtensionConstructor.EnumConstructorType.CONTAINER_CLASS)
	public UIExtensionBackgroundMinecraft(Class<E> containerClass) {
		super(CastUtilities.castUnchecked(IUIComponentManager.class), // COMMENT the generics should not matter here
				containerClass);
	}

	protected void renderBackground(Screen screen, Point2D mouse, double partialTicks) { UIBackgrounds.notifyBackgroundDrawn(screen); }

	protected final AtomicReference<RenderObserver> observerRender = new AtomicReference<>();

	@Override
	public IExtensionType<INamespacePrefixedString, ?, IUIComponentManager<?>> getType() { return IUIExtensionBackgroundRenderer.TYPE.getValue(); }

	@OnlyIn(Dist.CLIENT)
	public static class Default<E extends IUIComponentManager<?>>
			extends UIExtensionBackgroundMinecraft<E> {
		@UIExtensionConstructor(type = UIExtensionConstructor.EnumConstructorType.CONTAINER_CLASS)
		public Default(Class<E> containerClass) { super(containerClass); }

		@Override
		protected void renderBackground(Screen screen, Point2D mouse, double partialTicks) { UIBackgrounds.renderBackgroundAndNotify(screen.getMinecraft(), screen.width, screen.height, 0); }
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionAdded(IUIComponentManager<?> container) {
		super.onExtensionAdded(container);
		UIEventBusEntryPoint.<UIViewMinecraftBusEvent.Render>getEventBus()
				.subscribe(getObserverRender().accumulateAndGet(new RenderObserver(), (p, n) -> {
					if (p != null)
						p.dispose();
					return n;
				}));
	}

	@OnlyIn(Dist.CLIENT)
	public static class Dirt<E extends IUIComponentManager<?>>
			extends UIExtensionBackgroundMinecraft<E> {
		@UIExtensionConstructor(type = UIExtensionConstructor.EnumConstructorType.CONTAINER_CLASS)
		public Dirt(Class<E> containerClass) { super(containerClass); }

		@Override
		protected void renderBackground(Screen screen, Point2D mouse, double partialTicks) { UIBackgrounds.renderDirtBackgroundAndNotify(screen.getMinecraft(), screen.width, screen.height, 0); }
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionRemoved() {
		super.onExtensionRemoved();
		Optional.ofNullable(getObserverRender().getAndSet(null)).ifPresent(DisposableObserver::dispose);
	}

	protected AtomicReference<RenderObserver> getObserverRender() { return observerRender; }

	@OnlyIn(Dist.CLIENT)
	public class RenderObserver
			extends LoggingDisposableObserver<UIViewMinecraftBusEvent.Render> {
		public RenderObserver() { super(UIConfiguration.getInstance().getLogger()); }

		@Override
		@SubscribeEvent
		public void onNext(@Nonnull UIViewMinecraftBusEvent.Render event) {
			super.onNext(event);
			if (event.getStage().isPre())
				CastUtilities.castChecked(IUIViewComponent.class, event.getView())
						.filter(evc -> getContainer().filter(Predicate.isEqual(evc.getManager())).isPresent())
						.flatMap(IUISubInfrastructure::getInfrastructure)
						.flatMap(IUIExtensionScreenProvider.TYPE.getValue()::find)
						.ifPresent(extScr -> renderBackground(extScr.getScreen(), event.getCursorPositionView(), event.getPartialTicks()));
		}
	}
}
