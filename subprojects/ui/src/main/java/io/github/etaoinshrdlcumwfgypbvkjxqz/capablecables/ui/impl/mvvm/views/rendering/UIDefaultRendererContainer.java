package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.naming.AbstractNamed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.reactivex.rxjava3.observers.DisposableObserver;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nullable;
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
	private Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier;

	public UIDefaultRendererContainer(@NonNls CharSequence name, IUIRendererContainerContainer<?> container, Class<? extends R> defaultRendererClass) {
		super(name);
		this.container = new OptionalWeakReference<>(container);
		this.defaultRendererClass = defaultRendererClass;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		setBinderObserverSupplier(binderObserverSupplier);
		getRenderer().ifPresent(renderer -> renderer.initializeBindings(binderObserverSupplier));
	}

	@Override
	public Optional<? extends R> getRenderer() { return Optional.ofNullable(renderer); }

	@Override
	@Deprecated
	public void setRenderer(@Nullable R renderer) {
		IUIRendererContainer.setRendererImpl(getContainer().orElseThrow(IllegalStateException::new),
				renderer,
				renderer2 -> {
					this.renderer = renderer2;
					if (renderer2 != null)
						getBinderObserverSupplier().ifPresent(renderer2::initializeBindings);
				},
				this.renderer);
	}

	@Override
	public Class<? extends R> getDefaultRendererClass() { return defaultRendererClass; }

	@Override
	public Optional<? extends IUIRendererContainerContainer<?>> getContainer() { return container.getOptional(); }

	protected Optional<? extends Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>>> getBinderObserverSupplier() {
		return Optional.ofNullable(binderObserverSupplier);
	}

	protected void setBinderObserverSupplier(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		this.binderObserverSupplier = binderObserverSupplier;
	}
}
