package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIStructureLifecycleContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBindingAction;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UIImmutableStructureLifecycleContext
		implements IUIStructureLifecycleContext {
	private final Supplier<? extends Optional<? extends Consumer<? super IBindingAction>>> bindingActionConsumerSupplier;

	private UIImmutableStructureLifecycleContext(Supplier<? extends Optional<? extends Consumer<? super IBindingAction>>> bindingActionConsumerSupplier) {
		this.bindingActionConsumerSupplier = bindingActionConsumerSupplier;
	}

	public static UIImmutableStructureLifecycleContext of(Supplier<? extends Optional<? extends Consumer<? super IBindingAction>>> bindingActionConsumerSupplier) {
		return new UIImmutableStructureLifecycleContext(bindingActionConsumerSupplier);
	}

	@Override
	public Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>> getBindingActionConsumerSupplier() {
		return bindingActionConsumerSupplier;
	}
}
