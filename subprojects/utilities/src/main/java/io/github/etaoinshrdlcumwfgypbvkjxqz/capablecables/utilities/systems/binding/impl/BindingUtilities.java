package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBinding;

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
	                                             Iterable<?> objects) {
		initializeBindings(
				bindingActionConsumerSupplier, Streams.stream(objects).unordered()
						.filter(IHasBinding.class::isInstance)
						.map(IHasBinding.class::cast)
						.collect(ImmutableSet.toImmutableSet())
		);
	}

	@SuppressWarnings("UnstableApiUsage")
	public static void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>> bindingActionConsumerSupplier,
	                                      Iterable<? extends IHasBinding> bindings) {
		Streams.stream(bindings).unordered()
				.forEach(binding -> binding.initializeBindings(bindingActionConsumerSupplier));
	}

	@SuppressWarnings("UnstableApiUsage")
	public static void findAndCleanupBindings(Iterable<?> objects) {
		cleanupBindings(
				Streams.stream(objects).unordered()
						.filter(IHasBinding.class::isInstance)
						.map(IHasBinding.class::cast)
						.collect(ImmutableSet.toImmutableSet())
		);
	}

	@SuppressWarnings("UnstableApiUsage")
	public static void cleanupBindings(Iterable<? extends IHasBinding> bindings) {
		Streams.stream(bindings).unordered()
				.forEach(IHasBinding::cleanupBindings);
	}
}
