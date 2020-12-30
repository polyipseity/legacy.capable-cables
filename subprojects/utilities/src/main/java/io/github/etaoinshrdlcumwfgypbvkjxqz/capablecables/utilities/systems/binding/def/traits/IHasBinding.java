package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.traits;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBindingAction;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface IHasBinding {
	@OverridingMethodsMustInvokeSuper
	default void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>> bindingActionConsumerSupplier) {
		// COMMENT for 'OverridingMethodsMustInvokeSuper'
	}

	@OverridingMethodsMustInvokeSuper
	default void cleanupBindings() {
		// COMMENT for 'OverridingMethodsMustInvokeSuper'
	}
}
