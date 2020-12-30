package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IValueHolder;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface IBindingActionConsumerSupplierHolder
		extends IValueHolder<Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>>> {}
