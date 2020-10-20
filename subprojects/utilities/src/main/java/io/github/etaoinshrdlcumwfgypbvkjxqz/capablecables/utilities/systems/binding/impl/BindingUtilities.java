package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBinding;
import io.reactivex.rxjava3.observers.DisposableObserver;

import java.util.Optional;
import java.util.function.Supplier;

public enum BindingUtilities {
	;

	public static void actOnBinderObserverSupplier(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier,
	                                               Supplier<? extends IBinderAction> action) {
		AssertionUtilities.assertNonnull(binderObserverSupplier.get())
				.ifPresent(binderObserver -> binderObserver.onNext(AssertionUtilities.assertNonnull(action.get())));
	}

	@SuppressWarnings("UnstableApiUsage")
	public static void findAndInitializeBindings(Iterable<?> objects, Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		initializeBindings(
				Streams.stream(objects).unordered()
						.filter(IHasBinding.class::isInstance)
						.map(IHasBinding.class::cast)
						.collect(ImmutableSet.toImmutableSet()),
				binderObserverSupplier);
	}

	@SuppressWarnings("UnstableApiUsage")
	public static void initializeBindings(Iterable<? extends IHasBinding> bindings, Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		Streams.stream(bindings).unordered()
				.forEach(binding -> binding.initializeBindings(binderObserverSupplier));
	}
}
