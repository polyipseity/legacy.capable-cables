package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.bindings;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBinding;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.NoSuchBindingTransformerException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.traits.IHasBindingKey;

import java.util.Iterator;

public interface IBindings<B extends IBinding<?>>
		extends IHasBindingKey {
	boolean add(Iterator<? extends B> bindings)
			throws NoSuchBindingTransformerException;

	boolean remove(Iterator<? extends B> bindings);

	boolean removeAll();

	boolean isEmpty();
}
