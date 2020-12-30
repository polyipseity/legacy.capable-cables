package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBindingAction;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIStructureLifecycleContext {
	Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>> getBindingActionConsumerSupplier();
}
