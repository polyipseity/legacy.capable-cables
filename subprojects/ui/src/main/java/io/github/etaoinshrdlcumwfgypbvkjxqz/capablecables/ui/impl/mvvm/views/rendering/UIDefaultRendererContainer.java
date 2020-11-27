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
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderObserverSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.DefaultBinderObserverSupplierHolder;
import io.reactivex.rxjava3.observers.DisposableObserver;
import org.jetbrains.annotations.NonNls;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Optional;
import java.util.function.Supplier;

public class UIDefaultRendererContainer<R extends IUIRenderer<?>>
		extends AbstractNamed
		implements IUIRendererContainer<R> {
	private final OptionalWeakReference<IUIRendererContainerContainer<?>> container;
	private final IBinderObserverSupplierHolder binderObserverSupplierHolder = new DefaultBinderObserverSupplierHolder();
	private final Class<? extends R> defaultRendererClass;
	@Nullable
	private R renderer;

	public UIDefaultRendererContainer(@Nullable @NonNls CharSequence name, IUIRendererContainerContainer<?> container, Class<? extends R> defaultRendererClass) {
		super(name);
		this.container = new OptionalWeakReference<>(container);
		this.defaultRendererClass = defaultRendererClass;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		IUIRendererContainer.super.initializeBindings(binderObserverSupplier);
		getBinderObserverSupplierHolder().setValue(binderObserverSupplier);
		BindingUtilities.initializeBindings(
				binderObserverSupplier, getRenderer().map(ImmutableList::of).orElseGet(ImmutableList::of)
		);
	}

	protected IBinderObserverSupplierHolder getBinderObserverSupplierHolder() {
		return binderObserverSupplierHolder;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings() {
		getBinderObserverSupplierHolder().getValue().ifPresent(binderObserverSupplier ->
				BindingUtilities.cleanupBindings(
						getRenderer().map(ImmutableList::of).orElseGet(ImmutableList::of)));
		getBinderObserverSupplierHolder().setValue(null);
		IUIRendererContainer.super.cleanupBindings();
	}

	@Override
	public Class<? extends R> getDefaultRendererClass() { return defaultRendererClass; }

	@Override
	public Optional<? extends IUIRendererContainerContainer<?>> getContainer() { return container.getOptional(); }

	@Override
	public Optional<? extends R> getRenderer() { return Optional.ofNullable(renderer); }

	@Override
	@Deprecated
	public void setRenderer(@Nullable R renderer) {
		IUIRendererContainer.setRendererImpl(getContainer().orElseThrow(IllegalStateException::new),
				renderer,
				nextRenderer -> {
					Optional<? extends Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>>> binderObserverSupplier =
							getBinderObserverSupplierHolder().getValue();
					@Nullable R previousRenderer = this.renderer;
					this.renderer = nextRenderer;
					if (previousRenderer != null)
						binderObserverSupplier.ifPresent(binderObserverSupplier1 -> previousRenderer.cleanupBindings());
					if (nextRenderer != null)
						binderObserverSupplier.ifPresent(nextRenderer::initializeBindings);
				},
				this.renderer);
	}
}
