package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.mvvm.extensions.background;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIExtensionArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.UIExtensionConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.IUISubInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.construction.UIImmutableExtensionArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIAbstractViewBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.def.mvvm.events.bus.IUIMinecraftRenderEventExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.def.mvvm.extensions.IUIMinecraftBackgroundExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.def.mvvm.extensions.IUIMinecraftScreenProviderExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.def.mvvm.views.IUIMinecraftViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AutoCloseableRotator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.DelegatingSubscriber;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.ReactiveUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBindingActionConsumerSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.DefaultBindingActionConsumerSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EnumHookStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EventBusSubscriber;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.ImmutableSubscribeEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.AbstractContainerAwareExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.def.IInputDevices;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.def.IPointerDevice;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl.Optional3;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;

@OnlyIn(Dist.CLIENT)
public class UIDefaultMinecraftBackgroundExtension
		extends AbstractContainerAwareExtension<IIdentifier, IUIMinecraftViewComponent<?, ?>, IUIMinecraftViewComponent<?, ?>>
		implements IUIMinecraftBackgroundExtension {
	private static final IUIExtensionArguments DEFAULT_ARGUMENTS =
			UIImmutableExtensionArguments.of(ImmutableMap.of(), IUIMinecraftViewComponent.class, null);
	private final AutoCloseableRotator<DisposableSubscriber<UIAbstractViewBusEvent.Render>, RuntimeException> renderSubscriberRotator =
			new AutoCloseableRotator<>(() -> RenderSubscriber.ofDecorated(suppressThisEscapedWarning(() -> this), UIConfiguration.getInstance().getLogger()), Disposable::dispose);
	private final IUIRendererContainerContainer<IBackgroundRenderer> rendererContainerContainer;

	private final IBindingActionConsumerSupplierHolder bindingActionConsumerSupplierHolder = new DefaultBindingActionConsumerSupplierHolder();

	public UIDefaultMinecraftBackgroundExtension() { this(getDefaultArguments()); }

	@UIExtensionConstructor
	public UIDefaultMinecraftBackgroundExtension(@SuppressWarnings("unused") IUIExtensionArguments arguments) {
		super(CastUtilities.castUnchecked(IUIMinecraftViewComponent.class));

		this.rendererContainerContainer =
				UIDefaultRendererContainerContainer.ofDefault(arguments.getRendererName().orElse(null), suppressThisEscapedWarning(() -> this), UIMinecraftBackgroundExtensionEmptyBackgroundRenderer.class);
	}

	protected static IUIExtensionArguments getDefaultArguments() { return DEFAULT_ARGUMENTS; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionAdded(IUIMinecraftViewComponent<?, ?> container) {
		super.onExtensionAdded(container);
		UIEventBusEntryPoint.<UIAbstractViewBusEvent.Render>getBusPublisher().subscribe(getRenderSubscriberRotator().get());
		IUIView.registerRendererContainers(container, Iterators.singletonIterator(getRendererContainer()));
	}

	protected AutoCloseableRotator<? extends DisposableSubscriber<? super UIAbstractViewBusEvent.Render>, RuntimeException> getRenderSubscriberRotator() { return renderSubscriberRotator; }

	@Override
	public IUIRendererContainer<? extends IBackgroundRenderer> getRendererContainer() {
		return getRendererContainerContainer().getRendererContainer();
	}

	protected IUIRendererContainerContainer<IBackgroundRenderer> getRendererContainerContainer() {
		return rendererContainerContainer;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionRemoved() {
		getContainer().ifPresent(container -> IUIView.unregisterRendererContainers(container, Iterators.singletonIterator(getRendererContainer())));
		getRenderSubscriberRotator().close();
		super.onExtensionRemoved();
	}

	@Override
	public IExtensionType<IIdentifier, ?, IUIMinecraftViewComponent<?, ?>> getType() { return StaticHolder.getType().getValue(); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>> bindingActionConsumerSupplier) {
		IUIMinecraftBackgroundExtension.super.initializeBindings(bindingActionConsumerSupplier);
		getBindingActionConsumerSupplierHolder().setValue(bindingActionConsumerSupplier);
		BindingUtilities.initializeBindings(
				bindingActionConsumerSupplier, Iterators.singletonIterator(getRendererContainerContainer())
		);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings() {
		BindingUtilities.cleanupBindings(Iterators.singletonIterator(getRendererContainerContainer()));
		getBindingActionConsumerSupplierHolder().setValue(null);
		IUIMinecraftBackgroundExtension.super.cleanupBindings();
	}

	protected IBindingActionConsumerSupplierHolder getBindingActionConsumerSupplierHolder() {
		return bindingActionConsumerSupplierHolder;
	}

	@OnlyIn(Dist.CLIENT)
	public static class RenderSubscriber
			extends DelegatingSubscriber<UIAbstractViewBusEvent.Render> {
		private final OptionalWeakReference<UIDefaultMinecraftBackgroundExtension> owner;

		protected RenderSubscriber(Subscriber<? super UIAbstractViewBusEvent.Render> delegate, UIDefaultMinecraftBackgroundExtension owner) {
			super(delegate);
			this.owner = OptionalWeakReference.of(owner);
		}

		@SuppressWarnings("AnonymousInnerClass")
		public static DisposableSubscriber<UIAbstractViewBusEvent.Render> ofDecorated(UIDefaultMinecraftBackgroundExtension owner, Logger logger) {
			return new EventBusSubscriber<UIAbstractViewBusEvent.Render>(
					ImmutableSubscribeEvent.of(),
					ReactiveUtilities.decorateAsListener(delegate -> new RenderSubscriber(delegate, owner), logger)
			) {
				@Override
				public void onNext(UIAbstractViewBusEvent.Render event) {
					onNextImpl(event);
				}
			};
		}

		@Override
		@SubscribeEvent
		public void onNext(@Nonnull UIAbstractViewBusEvent.Render event) {
			super.onNext(event);
			if (event.getStage() == EnumHookStage.PRE)
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
							IPointerDevice pointerDevice = values.getValue3Nonnull();
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
