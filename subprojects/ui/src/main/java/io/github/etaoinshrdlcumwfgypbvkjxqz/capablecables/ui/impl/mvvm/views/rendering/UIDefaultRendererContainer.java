package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.naming.AbstractNamed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BinderObserverSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.reactivex.rxjava3.observers.DisposableObserver;
import org.jetbrains.annotations.NonNls;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Optional;
import java.util.function.Supplier;

public class UIDefaultRendererContainer<R extends IUIRenderer<?>>
		extends AbstractNamed
		implements IUIRendererContainer<R> {
	private final OptionalWeakReference<IUIRendererContainerContainer<?>> container;
	private final BinderObserverSupplierHolder binderObserverSupplierHolder = new BinderObserverSupplierHolder();
	private final Class<? extends R> defaultRendererClass;
	@Nullable
	private R renderer;

	public UIDefaultRendererContainer(@NonNls CharSequence name, IUIRendererContainerContainer<?> container, Class<? extends R> defaultRendererClass) {
		super(name);
		this.container = new OptionalWeakReference<>(container);
		this.defaultRendererClass = defaultRendererClass;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		IUIRendererContainer.super.initializeBindings(binderObserverSupplier);
		getBinderObserverSupplierHolder().setBinderObserverSupplier(binderObserverSupplier);
		BindingUtilities.initializeBindings(
				getRenderer().map(ImmutableList::of).orElseGet(ImmutableList::of),
				binderObserverSupplier
		);
	}

	protected BinderObserverSupplierHolder getBinderObserverSupplierHolder() {
		return binderObserverSupplierHolder;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		IUIRendererContainer.super.cleanupBindings(binderObserverSupplier);
		getBinderObserverSupplierHolder().setBinderObserverSupplier(null);
		BindingUtilities.cleanupBindings(
				getRenderer().map(ImmutableList::of).orElseGet(ImmutableList::of),
				binderObserverSupplier
		);
	}

	@Override
	public Optional<? extends R> getRenderer() { return Optional.ofNullable(renderer); }

	@Override
	public Class<? extends R> getDefaultRendererClass() { return defaultRendererClass; }

	@Override
	public Optional<? extends IUIRendererContainerContainer<?>> getContainer() { return container.getOptional(); }

	@Override
	@Deprecated
	public void setRenderer(@Nullable R renderer) {
		IUIRendererContainer.setRendererImpl(getContainer().orElseThrow(IllegalStateException::new),
				renderer,
				nextRenderer -> {
					Optional<? extends Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>>> binderObserverSupplier =
							getBinderObserverSupplierHolder().getBinderObserverSupplier();
					@Nullable R previousRenderer = this.renderer;
					this.renderer = nextRenderer;
					if (previousRenderer != null)
						binderObserverSupplier.ifPresent(previousRenderer::cleanupBindings);
					if (nextRenderer != null)
						binderObserverSupplier.ifPresent(nextRenderer::initializeBindings);
				},
				this.renderer);
	}
}
