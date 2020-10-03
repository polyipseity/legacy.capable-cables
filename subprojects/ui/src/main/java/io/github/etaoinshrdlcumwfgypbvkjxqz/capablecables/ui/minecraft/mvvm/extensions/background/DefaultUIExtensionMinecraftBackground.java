package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.extensions.background;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUISubInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIExtensionConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.extensions.IUIExtensionMinecraftBackground;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.extensions.IUIExtensionMinecraftScreenProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.IUIViewComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.events.bus.UIViewMinecraftBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.rendering.DefaultUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AutoCloseableRotator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.AbstractContainerAwareExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.inputs.IInputPointerDevice;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.LoggingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.optionals.Optional2;
import io.reactivex.rxjava3.disposables.Disposable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Optional;
import java.util.function.Predicate;

@OnlyIn(Dist.CLIENT)
public class DefaultUIExtensionMinecraftBackground<E extends IUIViewComponentMinecraft<?, ?>>
		extends AbstractContainerAwareExtension<INamespacePrefixedString, IUIViewComponentMinecraft<?, ?>, E>
		implements IUIExtensionMinecraftBackground {
	@SuppressWarnings("ThisEscapedInObjectConstruction")
	private final AutoCloseableRotator<RenderObserver, RuntimeException> renderObserverRotator =
			new AutoCloseableRotator<>(() -> new RenderObserver(this, UIConfiguration.getInstance().getLogger()), Disposable::dispose);
	private final IUIRendererContainer<IBackgroundRenderer> rendererContainer = new DefaultUIRendererContainer<>(new UIExtensionMinecraftBackgroundNullBackgroundRenderer(ImmutableMap.of()));

	@UIExtensionConstructor(type = UIExtensionConstructor.EnumConstructorType.CONTAINER_CLASS)
	public DefaultUIExtensionMinecraftBackground(Class<E> containerClass) {
		super(CastUtilities.castUnchecked(IUIViewComponentMinecraft.class), // COMMENT the generics should not matter here
				containerClass);
	}

	@Override
	public IExtensionType<INamespacePrefixedString, ?, IUIViewComponentMinecraft<?, ?>> getType() { return IUIExtensionMinecraftBackground.TYPE.getValue(); }

	@Override
	public Optional<? extends IBackgroundRenderer> getRenderer() {
		return getRendererContainer().getRenderer();
	}

	protected IUIRendererContainer<IBackgroundRenderer> getRendererContainer() { return rendererContainer; }

	@Override
	@Deprecated
	public void setRenderer(@Nullable IBackgroundRenderer renderer) {
		IUIRendererContainer.StaticHolder.setRendererImpl(this, renderer, getRendererContainer()::setRenderer);
	}

	@Override
	public Class<? extends IBackgroundRenderer> getDefaultRendererClass() { return getRendererContainer().getDefaultRendererClass(); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionAdded(IUIViewComponentMinecraft<?, ?> container) {
		super.onExtensionAdded(container);
		UIEventBusEntryPoint.<UIViewMinecraftBusEvent.Render>getEventBus().subscribe(getRenderObserverRotator().get());
	}

	protected AutoCloseableRotator<RenderObserver, RuntimeException> getRenderObserverRotator() { return renderObserverRotator; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionRemoved() {
		super.onExtensionRemoved();
		getRenderObserverRotator().close();
	}

	@OnlyIn(Dist.CLIENT)
	public static class RenderObserver
			extends LoggingDisposableObserver<UIViewMinecraftBusEvent.Render> {
		private final OptionalWeakReference<DefaultUIExtensionMinecraftBackground<?>> owner;

		public RenderObserver(DefaultUIExtensionMinecraftBackground<?> owner, Logger logger) {
			super(logger);
			this.owner = new OptionalWeakReference<>(owner);
		}

		@Override
		@SubscribeEvent
		public void onNext(@Nonnull UIViewMinecraftBusEvent.Render event) {
			super.onNext(event);
			if (event.getStage().isPre())
				Optional2.of(getOwner().orElse(null), event.getContextView().getInputDevices().getPointerDevice().orElse(null))
						.ifPresent(values -> {
							DefaultUIExtensionMinecraftBackground<?> owner = values.getValue1Nonnull();
							IInputPointerDevice pointerDevice = values.getValue2Nonnull();
							owner.getRenderer().ifPresent(renderer ->
									CastUtilities.castChecked(CastUtilities.<Class<IUIViewComponentMinecraft<?, ?>>>castUnchecked(IUIViewComponent.class), event.getView())
											.filter(evc -> owner.getContainer().filter(Predicate.isEqual(evc.getManager())).isPresent())
											.flatMap(IUISubInfrastructure::getInfrastructure)
											.flatMap(IUIExtensionMinecraftScreenProvider.TYPE.getValue()::find)
											.flatMap(IUIExtensionMinecraftScreenProvider::getScreen)
											.ifPresent(screen -> renderer.render(screen, pointerDevice.getPositionView(), event.getPartialTicks()))
							);
						});
		}

		protected Optional<? extends DefaultUIExtensionMinecraftBackground<?>> getOwner() { return owner.getOptional(); }
	}
}
