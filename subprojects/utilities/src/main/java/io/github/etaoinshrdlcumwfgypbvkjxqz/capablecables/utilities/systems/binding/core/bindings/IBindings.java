package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.bindings;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinding;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.NoSuchBindingTransformerException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;

import java.util.Iterator;

public interface IBindings<B extends IBinding<?>>
		extends IHasBindingKey {
	boolean add(Iterator<? extends B> bindings)
			throws NoSuchBindingTransformerException;

	boolean remove(Iterator<? extends B> bindings);

	boolean removeAll();

	boolean isEmpty();
}
