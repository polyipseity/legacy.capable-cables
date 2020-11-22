package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderObserverSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.DefaultBinderObserverSupplierHolder;
import io.reactivex.rxjava3.observers.DisposableObserver;
import org.jetbrains.annotations.NonNls;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Optional;
import java.util.function.Supplier;

public class UIDefaultRendererContainerContainer<R extends IUIRenderer<?>>
		implements IUIRendererContainerContainer<R> {
	private final IUIRendererContainer<R> rendererContainer;
	private final IBinderObserverSupplierHolder binderObserverSupplierHolder = new DefaultBinderObserverSupplierHolder();

	public UIDefaultRendererContainerContainer(IUIRendererContainer<R> rendererContainer) {
		// COMMENT this class is pretty much useless right now except for allowing easy change of implementation in the future
		this.rendererContainer = rendererContainer;
	}

	public static <R extends IUIRenderer<?>> UIDefaultRendererContainerContainer<R> ofDefault(@Nullable @NonNls CharSequence name,
	                                                                                          IUIRendererContainerContainer<?> container,
	                                                                                          Class<? extends R> defaultRendererClass) {
		return new UIDefaultRendererContainerContainer<>(new UIDefaultRendererContainer<>(name, container, defaultRendererClass));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		IUIRendererContainerContainer.super.initializeBindings(binderObserverSupplier);
		getBinderObserverSupplierHolder().setValue(binderObserverSupplier);
		BindingUtilities.initializeBindings(binderObserverSupplier,
				ImmutableList.of(getRendererContainer()));
	}

	protected IBinderObserverSupplierHolder getBinderObserverSupplierHolder() {
		return binderObserverSupplierHolder;
	}

	@Override
	public IUIRendererContainer<? extends R> getRendererContainer() {
		return rendererContainer;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings() {
		getBinderObserverSupplierHolder().getValue().ifPresent(binderObserverSupplier ->
				BindingUtilities.cleanupBindings(
						ImmutableList.of(getRendererContainer())));
		getBinderObserverSupplierHolder().setValue(null);
		IUIRendererContainerContainer.super.cleanupBindings();
	}
}
