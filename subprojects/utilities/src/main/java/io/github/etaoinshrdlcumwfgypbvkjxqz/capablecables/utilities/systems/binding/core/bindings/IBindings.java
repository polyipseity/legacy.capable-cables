package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.bindings;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinding;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.NoSuchBindingTransformerException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;

public interface IBindings<B extends IBinding<?>>
		extends IHasBindingKey {
	boolean add(Iterable<? extends B> bindings)
			throws NoSuchBindingTransformerException;

	boolean remove(Iterable<? extends B> bindings);

	boolean removeAll();

	boolean isEmpty();
}
