package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BinderObserverSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.reactivex.rxjava3.observers.DisposableObserver;
import org.jetbrains.annotations.NonNls;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class UIDefaultRendererContainerContainer<R extends IUIRenderer<?>, C extends IUIRendererContainerContainer<?>>
		implements IUIRendererContainerContainer<R> {
	private final OptionalWeakReference<C> container;
	private final BiFunction<@Nonnull ? super CharSequence, @Nonnull ? super C, @Nonnull ? extends IUIRendererContainer<R>> rendererContainerInitializer;
	private final BinderObserverSupplierHolder binderObserverSupplierHolder = new BinderObserverSupplierHolder();
	private final AtomicReference<IUIRendererContainer<R>> rendererContainerReference = new AtomicReference<>();

	public UIDefaultRendererContainerContainer(C container,
	                                           BiFunction<@Nonnull ? super CharSequence, @Nonnull ? super C, @Nonnull ? extends IUIRendererContainer<R>> rendererContainerInitializer) {
		this.container = new OptionalWeakReference<>(container);
		this.rendererContainerInitializer = rendererContainerInitializer;
	}

	public static <C extends IUIRendererContainerContainer<?>, R extends IUIRenderer<?>> BiFunction<@Nonnull CharSequence, @Nonnull C, @Nonnull IUIRendererContainer<R>>
	createRendererContainerInitializer(Class<? extends R> defaultRendererClass) {
		return (name, container) ->
				new UIDefaultRendererContainer<>(name, container, defaultRendererClass);
	}

	@Override
	public IUIRendererContainer<? extends R> getRendererContainer() throws IllegalStateException {
		return Optional.ofNullable(getRendererContainerReference().get()).orElseThrow(IllegalStateException::new);
	}

	@Override
	public void initializeRendererContainer(@NonNls CharSequence name) throws IllegalStateException {
		IUIRendererContainer<R> rendererContainer =
				getRendererContainerInitializer().apply(name, getContainer().orElseThrow(IllegalStateException::new));
		if (!getRendererContainerReference().compareAndSet(null, rendererContainer))
			throw new IllegalStateException();
		getBinderObserverSupplierHolder().getBinderObserverSupplier().ifPresent(rendererContainer::initializeBindings);
	}

	protected BiFunction<@Nonnull ? super CharSequence, @Nonnull ? super C, @Nonnull ? extends IUIRendererContainer<R>> getRendererContainerInitializer() {
		return rendererContainerInitializer;
	}

	protected Optional<? extends C> getContainer() {
		return container.getOptional();
	}

	protected BinderObserverSupplierHolder getBinderObserverSupplierHolder() {
		return binderObserverSupplierHolder;
	}

	protected AtomicReference<IUIRendererContainer<R>> getRendererContainerReference() {
		return rendererContainerReference;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		IUIRendererContainerContainer.super.initializeBindings(binderObserverSupplier);
		getBinderObserverSupplierHolder().setBinderObserverSupplier(binderObserverSupplier);
		BindingUtilities.initializeBindings(
				Optional.ofNullable(getRendererContainerReference().get()).map(ImmutableList::of).orElseGet(ImmutableList::of),
				binderObserverSupplier
		);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		IUIRendererContainerContainer.super.cleanupBindings(binderObserverSupplier);
		getBinderObserverSupplierHolder().setBinderObserverSupplier(null);
		BindingUtilities.cleanupBindings(
				Optional.ofNullable(getRendererContainerReference().get()).map(ImmutableList::of).orElseGet(ImmutableList::of),
				binderObserverSupplier
		);
	}
}
