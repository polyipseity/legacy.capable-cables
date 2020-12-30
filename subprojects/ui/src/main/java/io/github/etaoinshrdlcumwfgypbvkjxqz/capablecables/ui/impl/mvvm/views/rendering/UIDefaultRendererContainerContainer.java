package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering;

import com.google.common.collect.Iterators;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBindingActionConsumerSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.DefaultBindingActionConsumerSupplierHolder;
import org.jetbrains.annotations.NonNls;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UIDefaultRendererContainerContainer<R extends IUIRenderer<?>>
		implements IUIRendererContainerContainer<R> {
	private final IUIRendererContainer<R> rendererContainer;
	private final IBindingActionConsumerSupplierHolder bindingActionConsumerSupplierHolder = new DefaultBindingActionConsumerSupplierHolder();

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
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>> bindingActionConsumerSupplier) {
		IUIRendererContainerContainer.super.initializeBindings(bindingActionConsumerSupplier);
		getBindingActionConsumerSupplierHolder().setValue(bindingActionConsumerSupplier);
		BindingUtilities.initializeBindings(bindingActionConsumerSupplier,
				Iterators.singletonIterator(getRendererContainer()));
	}

	protected IBindingActionConsumerSupplierHolder getBindingActionConsumerSupplierHolder() {
		return bindingActionConsumerSupplierHolder;
	}

	@Override
	public IUIRendererContainer<? extends R> getRendererContainer() {
		return rendererContainer;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings() {
		BindingUtilities.cleanupBindings(Iterators.singletonIterator(getRendererContainer()));
		getBindingActionConsumerSupplierHolder().setValue(null);
		IUIRendererContainerContainer.super.cleanupBindings();
	}
}
