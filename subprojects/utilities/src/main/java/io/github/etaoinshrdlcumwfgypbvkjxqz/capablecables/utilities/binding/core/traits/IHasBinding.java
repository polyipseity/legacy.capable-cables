package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.traits;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.methods.IBindingMethod;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import io.reactivex.rxjava3.core.ObservableSource;

public interface IHasBinding {
	default Iterable<? extends IBindingField<?>> getBindingFields() { return BindingUtilities.getBindingFields(this, BindingUtilities.EnumScopeOptions.getAll()); }

	default Iterable<? extends IBindingMethod<?>> getBindingMethods() { return BindingUtilities.getBindingMethods(this, BindingUtilities.EnumScopeOptions.getAll()); }

	@SuppressWarnings("UnstableApiUsage")
	default Iterable<? extends ObservableSource<IBinderAction>> getBinderNotifiers() {
		return Streams.stream(BindingUtilities.getHasBindingsVariables(this)).unordered()
				.flatMap(v -> Streams.stream(v.getBinderNotifiers()))
				.collect(ImmutableSet.toImmutableSet());
	}
}
