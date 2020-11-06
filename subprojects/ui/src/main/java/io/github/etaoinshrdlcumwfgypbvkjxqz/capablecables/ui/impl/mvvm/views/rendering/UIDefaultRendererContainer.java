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
	private final Class<? extends R> defaultRendererClass;
	@Nullable
	private R renderer;
	@Nullable
	private Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier;

	public UIDefaultRendererContainer(@NonNls CharSequence name, IUIRendererContainerContainer<?> container, Class<? extends R> defaultRendererClass) {
		super(name);
		this.container = new OptionalWeakReference<>(container);
		this.defaultRendererClass = defaultRendererClass;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		IUIRendererContainer.super.initializeBindings(binderObserverSupplier);
		setBinderObserverSupplier(binderObserverSupplier);
		BindingUtilities.initializeBindings(
				getRenderer().map(ImmutableList::of).orElseGet(ImmutableList::of),
				binderObserverSupplier
		);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		IUIRendererContainer.super.cleanupBindings(binderObserverSupplier);
		setBinderObserverSupplier(null);
		BindingUtilities.cleanupBindings(
				getRenderer().map(ImmutableList::of).orElseGet(ImmutableList::of),
				binderObserverSupplier
		);
	}

	@Override
	public Optional<? extends R> getRenderer() { return Optional.ofNullable(renderer); }

	protected Optional<? extends Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>>> getBinderObserverSupplier() {
		return Optional.ofNullable(binderObserverSupplier);
	}

	@Override
	public Class<? extends R> getDefaultRendererClass() { return defaultRendererClass; }

	@Override
	public Optional<? extends IUIRendererContainerContainer<?>> getContainer() { return container.getOptional(); }

	protected void setBinderObserverSupplier(@Nullable Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		this.binderObserverSupplier = binderObserverSupplier;
	}

	@Override
	@Deprecated
	public void setRenderer(@Nullable R renderer) {
		IUIRendererContainer.setRendererImpl(getContainer().orElseThrow(IllegalStateException::new),
				renderer,
				nextRenderer -> {
					@Nullable R previousRenderer = this.renderer;
					this.renderer = nextRenderer;
					if (previousRenderer != null)
						getBinderObserverSupplier().ifPresent(previousRenderer::cleanupBindings);
					if (nextRenderer != null)
						getBinderObserverSupplier().ifPresent(nextRenderer::initializeBindings);
				},
				this.renderer);
	}
}
