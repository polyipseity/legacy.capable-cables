package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.naming.AbstractNamed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CollectionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBindingActionConsumerSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.DefaultBindingActionConsumerSupplierHolder;
import org.jetbrains.annotations.NonNls;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UIDefaultRendererContainer<R extends IUIRenderer<?>>
		extends AbstractNamed
		implements IUIRendererContainer<R> {
	private final OptionalWeakReference<IUIRendererContainerContainer<?>> container;
	private final IBindingActionConsumerSupplierHolder bindingActionConsumerSupplierHolder = new DefaultBindingActionConsumerSupplierHolder();
	private final Class<? extends R> defaultRendererClass;
	@Nullable
	private R renderer;

	public UIDefaultRendererContainer(@Nullable @NonNls CharSequence name, IUIRendererContainerContainer<?> container, Class<? extends R> defaultRendererClass) {
		super(name);
		this.container = OptionalWeakReference.of(container);
		this.defaultRendererClass = defaultRendererClass;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>> bindingActionConsumerSupplier) {
		IUIRendererContainer.super.initializeBindings(bindingActionConsumerSupplier);
		getBindingActionConsumerSupplierHolder().setValue(bindingActionConsumerSupplier);
		BindingUtilities.initializeBindings(
				bindingActionConsumerSupplier, CollectionUtilities.iterate(getRenderer())
		);
	}

	protected IBindingActionConsumerSupplierHolder getBindingActionConsumerSupplierHolder() {
		return bindingActionConsumerSupplierHolder;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings() {
		BindingUtilities.cleanupBindings(CollectionUtilities.iterate(getRenderer()));
		getBindingActionConsumerSupplierHolder().setValue(null);
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
					Optional<? extends Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>>> bindingActionConsumer =
							getBindingActionConsumerSupplierHolder().getValue();
					@Nullable R previousRenderer = this.renderer;
					this.renderer = nextRenderer;
					if (previousRenderer != null)
						previousRenderer.cleanupBindings();
					if (nextRenderer != null)
						bindingActionConsumer.ifPresent(nextRenderer::initializeBindings);
				},
				this.renderer);
	}
}
