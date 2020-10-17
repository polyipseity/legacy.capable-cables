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
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.rendering.UIDefaultRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AutoCloseableRotator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.AbstractContainerAwareExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.inputs.IInputPointerDevice;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.optionals.Optional2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.LoggingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class DefaultUIExtensionMinecraftBackground
		extends AbstractContainerAwareExtension<INamespacePrefixedString, IUIViewComponentMinecraft<?, ?>, IUIViewComponentMinecraft<?, ?>>
		implements IUIExtensionMinecraftBackground {
	@SuppressWarnings("ThisEscapedInObjectConstruction")
	private final AutoCloseableRotator<RenderObserver, RuntimeException> renderObserverRotator =
			new AutoCloseableRotator<>(() -> new RenderObserver(this, UIConfiguration.getInstance().getLogger()), Disposable::dispose);
	private static final UIExtensionConstructor.IArguments DEFAULT_ARGUMENTS =
			new UIExtensionConstructor.ImmutableArguments(ImmutableMap.of(), IUIViewComponentMinecraft.class);

	private final AtomicReference<IUIRendererContainer<IBackgroundRenderer>> rendererContainerReference = new AtomicReference<>();

	@Override
	public IUIRendererContainer<? extends IBackgroundRenderer> getRendererContainer()
			throws IllegalStateException { return Optional.ofNullable(getRendererContainerReference().get()).orElseThrow(IllegalStateException::new); }

	@Override
	public void initializeRendererContainer(@NonNls CharSequence name)
			throws IllegalStateException {
		if (!getRendererContainerReference().compareAndSet(null,
				new UIDefaultRendererContainer<>(name, this, UIExtensionMinecraftBackgroundNullBackgroundRenderer.class)))
			throw new IllegalStateException();
	}

	protected AtomicReference<IUIRendererContainer<IBackgroundRenderer>> getRendererContainerReference() { return rendererContainerReference; }

	public DefaultUIExtensionMinecraftBackground() { this(getDefaultArguments()); }

	@UIExtensionConstructor
	public DefaultUIExtensionMinecraftBackground(@SuppressWarnings("unused") UIExtensionConstructor.IArguments arguments) {
		super(CastUtilities.castUnchecked(IUIViewComponentMinecraft.class),
				CastUtilities.castUnchecked(IUIViewComponentMinecraft.class));
	}

	protected static UIExtensionConstructor.IArguments getDefaultArguments() { return DEFAULT_ARGUMENTS; }

	@Override
	public IExtensionType<INamespacePrefixedString, ?, IUIViewComponentMinecraft<?, ?>> getType() { return IUIExtensionMinecraftBackground.TYPE.getValue(); }

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

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {}

	@OnlyIn(Dist.CLIENT)
	public static class RenderObserver
			extends LoggingDisposableObserver<UIViewMinecraftBusEvent.Render> {
		private final OptionalWeakReference<DefaultUIExtensionMinecraftBackground> owner;

		public RenderObserver(DefaultUIExtensionMinecraftBackground owner, Logger logger) {
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
							DefaultUIExtensionMinecraftBackground owner = values.getValue1Nonnull();
							IInputPointerDevice pointerDevice = values.getValue2Nonnull();
							owner.getRendererContainer().getRenderer().ifPresent(renderer ->
									CastUtilities.castChecked(CastUtilities.<Class<IUIViewComponentMinecraft<?, ?>>>castUnchecked(IUIViewComponent.class), event.getView())
											.filter(evc -> owner.getContainer().filter(Predicate.isEqual(evc)).isPresent())
											.flatMap(IUISubInfrastructure::getInfrastructure)
											.flatMap(IUIExtensionMinecraftScreenProvider.TYPE.getValue()::find)
											.flatMap(IUIExtensionMinecraftScreenProvider::getScreen)
											.ifPresent(screen -> renderer.render(screen, pointerDevice.getPositionView(), event.getPartialTicks()))
							);
						});
		}

		protected Optional<? extends DefaultUIExtensionMinecraftBackground> getOwner() { return owner.getOptional(); }
	}
}
