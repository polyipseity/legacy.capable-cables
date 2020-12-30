package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl;

import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.traits.IHasBinding;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public enum BindingUtilities {
	;

	public static void supplyBindingAction(Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>> bindingActionConsumerSupplier,
	                                       Supplier<@Nonnull ? extends IBindingAction> action) {
		bindingActionConsumerSupplier.get()
				.ifPresent(binderActionConsumer -> binderActionConsumer.accept(action.get()));
	}

	@SuppressWarnings("UnstableApiUsage")
	public static void findAndInitializeBindings(Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>> bindingActionConsumerSupplier,
	                                             Iterator<?> objects) {
		initializeBindings(
				bindingActionConsumerSupplier,
				Streams.stream(objects).unordered()
						.filter(IHasBinding.class::isInstance)
						.map(IHasBinding.class::cast)
						.iterator()
		);
	}

	@SuppressWarnings("UnstableApiUsage")
	public static void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>> bindingActionConsumerSupplier,
	                                      Iterator<? extends IHasBinding> bindings) {
		Streams.stream(bindings).unordered()
				.forEach(binding -> binding.initializeBindings(bindingActionConsumerSupplier));
	}

	@SuppressWarnings("UnstableApiUsage")
	public static void findAndCleanupBindings(Iterator<?> objects) {
		cleanupBindings(
				Streams.stream(objects).unordered()
						.filter(IHasBinding.class::isInstance)
						.map(IHasBinding.class::cast)
						.iterator()
		);
	}

	@SuppressWarnings("UnstableApiUsage")
	public static void cleanupBindings(Iterator<? extends IHasBinding> bindings) {
		Streams.stream(bindings).unordered()
				.forEach(IHasBinding::cleanupBindings);
	}
}
