package $group__.ui.minecraft.mvvm.extensions;

import $group__.ui.core.mvvm.IUICommon;
import $group__.ui.core.parsers.components.UIExtensionConstructor;
import $group__.ui.events.bus.UIEventBusEntryPoint;
import $group__.ui.minecraft.core.mvvm.extensions.IUIExtensionBackgroundRenderer;
import $group__.ui.minecraft.core.mvvm.extensions.IUIExtensionScreenProvider;
import $group__.ui.minecraft.core.mvvm.views.IUIViewComponentMinecraft;
import $group__.ui.minecraft.mvvm.events.bus.EventUIViewMinecraft;
import $group__.ui.utilities.minecraft.UIBackgrounds;
import $group__.utilities.CastUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.extensions.ExtensionContainerAware;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.reactive.DisposableObserverAuto;
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
public abstract class UIExtensionBackgroundMinecraft<E extends IUIViewComponentMinecraft<?, ?>>
		extends ExtensionContainerAware<INamespacePrefixedString, IUIViewComponentMinecraft<?, ?>, E>
		implements IUIExtensionBackgroundRenderer {
	@UIExtensionConstructor(type = UIExtensionConstructor.ConstructorType.EXTENDED_CLASS)
	public UIExtensionBackgroundMinecraft(Class<E> extendedClass) {
		super(CastUtilities.castUnchecked(IUIViewComponentMinecraft.class), // COMMENT the generics should not matter here
				extendedClass);
	}

	protected void renderBackground(Screen screen, Point2D mouse, double partialTicks) { UIBackgrounds.notifyBackgroundDrawn(screen); }

	@Override
	public IType<? extends INamespacePrefixedString, ?, ? extends IUIViewComponentMinecraft<?, ?>> getType() { return IUIExtensionBackgroundRenderer.TYPE.getValue(); }

	@OnlyIn(Dist.CLIENT)
	public static class Default<E extends IUIViewComponentMinecraft<?, ?>>
			extends UIExtensionBackgroundMinecraft<E> {
		@UIExtensionConstructor(type = UIExtensionConstructor.ConstructorType.EXTENDED_CLASS)
		public Default(Class<E> extendedClass) { super(extendedClass); }

		@Override
		protected void renderBackground(Screen screen, Point2D mouse, double partialTicks) { UIBackgrounds.renderBackgroundAndNotify(screen.getMinecraft(), screen.width, screen.height, 0); }
	}

	@OnlyIn(Dist.CLIENT)
	public static class Dirt<E extends IUIViewComponentMinecraft<?, ?>>
			extends UIExtensionBackgroundMinecraft<E> {
		@UIExtensionConstructor(type = UIExtensionConstructor.ConstructorType.EXTENDED_CLASS)
		public Dirt(Class<E> extendedClass) { super(extendedClass); }

		@Override
		protected void renderBackground(Screen screen, Point2D mouse, double partialTicks) { UIBackgrounds.renderDirtBackgroundAndNotify(screen.getMinecraft(), screen.width, screen.height, 0); }
	}

	protected final AtomicReference<ObserverRender> observerRender = new AtomicReference<>();

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionAdded(IUIViewComponentMinecraft<?, ?> container) {
		super.onExtensionAdded(container);
		UIEventBusEntryPoint.<EventUIViewMinecraft.Render>getEventBus()
				.subscribe(getObserverRender().accumulateAndGet(new ObserverRender(), (p, n) -> {
					Optional.ofNullable(p).ifPresent(DisposableObserver::dispose);
					return n;
				}));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionRemoved() {
		super.onExtensionRemoved();
		Optional.ofNullable(getObserverRender().getAndSet(null)).ifPresent(DisposableObserver::dispose);
	}

	protected AtomicReference<ObserverRender> getObserverRender() { return observerRender; }

	@OnlyIn(Dist.CLIENT)
	public class ObserverRender
			extends DisposableObserverAuto<EventUIViewMinecraft.Render> {
		@Override
		@SubscribeEvent
		public void onNext(@Nonnull EventUIViewMinecraft.Render event) {
			if (event.getStage() == EnumEventHookStage.PRE) {
				getContainer().filter(Predicate.isEqual(event.getView()))
						.flatMap(IUICommon::getInfrastructure)
						.flatMap(c -> IUIExtensionScreenProvider.TYPE.getValue().get(c))
						.ifPresent(extScr -> renderBackground(extScr.getScreen(), event.getCursorPositionView(), event.getPartialTicks()));
			}
		}
	}
}
