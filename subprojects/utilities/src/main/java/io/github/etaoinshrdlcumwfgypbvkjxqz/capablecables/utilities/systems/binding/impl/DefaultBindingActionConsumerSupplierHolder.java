package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.DefaultValueHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBindingActionConsumerSupplierHolder;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DefaultBindingActionConsumerSupplierHolder
		extends DefaultValueHolder<Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>>>
		implements IBindingActionConsumerSupplierHolder {
	public DefaultBindingActionConsumerSupplierHolder() {
		super(null);
	}
}
