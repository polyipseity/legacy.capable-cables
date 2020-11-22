package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBinding;
import io.reactivex.rxjava3.observers.DisposableObserver;

import java.util.Optional;
import java.util.function.Supplier;

public enum BindingUtilities {
	;

	public static void actOnBinderObserverSupplier(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier,
	                                               Supplier<@Nonnull ? extends IBinderAction> action) {
		binderObserverSupplier.get()
				.ifPresent(binderObserver -> binderObserver.onNext(action.get()));
	}

	@SuppressWarnings("UnstableApiUsage")
	public static void findAndInitializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier,
	                                             Iterable<?> objects) {
		initializeBindings(
				binderObserverSupplier, Streams.stream(objects).unordered()
						.filter(IHasBinding.class::isInstance)
						.map(IHasBinding.class::cast)
						.collect(ImmutableSet.toImmutableSet())
		);
	}

	@SuppressWarnings("UnstableApiUsage")
	public static void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier,
	                                      Iterable<? extends IHasBinding> bindings) {
		Streams.stream(bindings).unordered()
				.forEach(binding -> binding.initializeBindings(binderObserverSupplier));
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
