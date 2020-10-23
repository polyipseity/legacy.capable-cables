package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.extensions.background;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUISubInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIExtensionConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.events.bus.IUIMinecraftRenderEventExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.extensions.IUIMinecraftBackgroundExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.extensions.IUIMinecraftScreenProviderExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.IUIMinecraftViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIAbstractViewBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AutoCloseableRotator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.LoggingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.AbstractContainerAwareExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IInputDevices;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IInputPointerDevice;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl.Optional3;
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
public class UIDefaultMinecraftBackgroundExtension
		extends AbstractContainerAwareExtension<INamespacePrefixedString, IUIMinecraftViewComponent<?, ?>, IUIMinecraftViewComponent<?, ?>>
		implements IUIMinecraftBackgroundExtension {
	private static final UIExtensionConstructor.IArguments DEFAULT_ARGUMENTS =
			new UIExtensionConstructor.ImmutableArguments(ImmutableMap.of(), IUIMinecraftViewComponent.class);
	@SuppressWarnings("ThisEscapedInObjectConstruction")
	private final AutoCloseableRotator<RenderObserver, RuntimeException> renderObserverRotator =
			new AutoCloseableRotator<>(() -> new RenderObserver(this, UIConfiguration.getInstance().getLogger()), Disposable::dispose);
	private final AtomicReference<IUIRendererContainer<IBackgroundRenderer>> rendererContainerReference = new AtomicReference<>();

	public UIDefaultMinecraftBackgroundExtension() { this(getDefaultArguments()); }

	@UIExtensionConstructor
	public UIDefaultMinecraftBackgroundExtension(@SuppressWarnings("unused") UIExtensionConstructor.IArguments arguments) {
		super(CastUtilities.castUnchecked(IUIMinecraftViewComponent.class),
				CastUtilities.castUnchecked(IUIMinecraftViewComponent.class));
	}

	protected static UIExtensionConstructor.IArguments getDefaultArguments() { return DEFAULT_ARGUMENTS; }

	@Override
	public IUIRendererContainer<? extends IBackgroundRenderer> getRendererContainer()
			throws IllegalStateException { return Optional.ofNullable(getRendererContainerReference().get()).orElseThrow(IllegalStateException::new); }

	@Override
	public void initializeRendererContainer(@NonNls CharSequence name)
			throws IllegalStateException {
		if (!getRendererContainerReference().compareAndSet(null,
				new UIDefaultRendererContainer<>(name, this, UIMinecraftBackgroundExtensionEmptyBackgroundRenderer.class)))
			throw new IllegalStateException();
	}

	protected AtomicReference<IUIRendererContainer<IBackgroundRenderer>> getRendererContainerReference() { return rendererContainerReference; }

	@Override
	public IExtensionType<INamespacePrefixedString, ?, IUIMinecraftViewComponent<?, ?>> getType() { return StaticHolder.getType().getValue(); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionAdded(IUIMinecraftViewComponent<?, ?> container) {
		super.onExtensionAdded(container);
		UIEventBusEntryPoint.<UIAbstractViewBusEvent.Render>getEventBus().subscribe(getRenderObserverRotator().get());
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

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {}

	@OnlyIn(Dist.CLIENT)
	public static class RenderObserver
			extends LoggingDisposableObserver<UIAbstractViewBusEvent.Render> {
		private final OptionalWeakReference<UIDefaultMinecraftBackgroundExtension> owner;

		public RenderObserver(UIDefaultMinecraftBackgroundExtension owner, Logger logger) {
			super(logger);
			this.owner = new OptionalWeakReference<>(owner);
		}

		@Override
		@SubscribeEvent
		public void onNext(@Nonnull UIAbstractViewBusEvent.Render event) {
			super.onNext(event);
			if (event.getStage().isPre())
				Optional3.of(
						() -> getOwner().orElse(null),
						() -> IUIMinecraftRenderEventExtension.StaticHolder.getType().find(event).orElse(null),
						() -> event.getView().getContext()
								.map(IUIViewContext::getInputDevices)
								.flatMap(IInputDevices::getPointerDevice)
								.orElse(null))
						.ifPresent(values -> {
							UIDefaultMinecraftBackgroundExtension owner = values.getValue1Nonnull();
							IUIMinecraftRenderEventExtension renderExtension = values.getValue2Nonnull();
							IInputPointerDevice pointerDevice = values.getValue3Nonnull();
							owner.getRendererContainer().getRenderer().ifPresent(renderer ->
									CastUtilities.castChecked(CastUtilities.<Class<IUIMinecraftViewComponent<?, ?>>>castUnchecked(IUIViewComponent.class), event.getView())
											.filter(evc -> owner.getContainer().filter(Predicate.isEqual(evc)).isPresent())
											.flatMap(IUISubInfrastructure::getInfrastructure)
											.flatMap(IUIMinecraftScreenProviderExtension.StaticHolder.getType().getValue()::find)
											.flatMap(IUIMinecraftScreenProviderExtension::getScreen)
											.ifPresent(screen -> renderer.render(screen, pointerDevice.getPositionView(), renderExtension.getPartialTicks()))
							);
						});
		}

		protected Optional<? extends UIDefaultMinecraftBackgroundExtension> getOwner() { return owner.getOptional(); }
	}
}
