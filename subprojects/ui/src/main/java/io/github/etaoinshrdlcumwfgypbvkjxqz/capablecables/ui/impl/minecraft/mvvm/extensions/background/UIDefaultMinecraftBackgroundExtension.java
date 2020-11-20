package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.extensions.background;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIExtensionArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIExtensionConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUISubInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.construction.UIImmutableExtensionArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.events.bus.IUIMinecraftRenderEventExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.extensions.IUIMinecraftBackgroundExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.extensions.IUIMinecraftScreenProviderExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.IUIMinecraftViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIAbstractViewBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AutoCloseableRotator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.LoggingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
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

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;

@OnlyIn(Dist.CLIENT)
public class UIDefaultMinecraftBackgroundExtension
		extends AbstractContainerAwareExtension<INamespacePrefixedString, IUIMinecraftViewComponent<?, ?>, IUIMinecraftViewComponent<?, ?>>
		implements IUIMinecraftBackgroundExtension {
	private static final IUIExtensionArguments DEFAULT_ARGUMENTS =
			new UIImmutableExtensionArguments(ImmutableMap.of(), IUIMinecraftViewComponent.class);
	private final AutoCloseableRotator<RenderObserver, RuntimeException> renderObserverRotator =
			new AutoCloseableRotator<>(() -> new RenderObserver(suppressThisEscapedWarning(() -> this), UIConfiguration.getInstance().getLogger()), Disposable::dispose);
	private final IUIRendererContainerContainer<IBackgroundRenderer> rendererContainerContainer =
			new UIDefaultRendererContainerContainer<>(suppressThisEscapedWarning(() -> this),
					UIDefaultRendererContainerContainer.createRendererContainerInitializer(UIMinecraftBackgroundExtensionEmptyBackgroundRenderer.class));

	public UIDefaultMinecraftBackgroundExtension() { this(getDefaultArguments()); }

	@UIExtensionConstructor
	public UIDefaultMinecraftBackgroundExtension(@SuppressWarnings("unused") IUIExtensionArguments arguments) {
		super(CastUtilities.castUnchecked(IUIMinecraftViewComponent.class));
	}

	protected static IUIExtensionArguments getDefaultArguments() { return DEFAULT_ARGUMENTS; }

	@Override
	public IUIRendererContainer<? extends IBackgroundRenderer> getRendererContainer()
			throws IllegalStateException {
		return getRendererContainerContainer().getRendererContainer();
	}

	protected IUIRendererContainerContainer<IBackgroundRenderer> getRendererContainerContainer() {
		return rendererContainerContainer;
	}

	@Override
	public void initializeRendererContainer(@NonNls CharSequence name)
			throws IllegalStateException {
		getRendererContainerContainer().initializeRendererContainer(name);
	}

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
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		IUIMinecraftBackgroundExtension.super.initializeBindings(binderObserverSupplier);
		BindingUtilities.initializeBindings(
				ImmutableList.of(getRendererContainerContainer()),
				binderObserverSupplier);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		IUIMinecraftBackgroundExtension.super.cleanupBindings(binderObserverSupplier);
		BindingUtilities.cleanupBindings(
				ImmutableList.of(getRendererContainerContainer()),
				binderObserverSupplier);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionRemoved() {
		super.onExtensionRemoved();
		getRenderObserverRotator().close();
	}

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
