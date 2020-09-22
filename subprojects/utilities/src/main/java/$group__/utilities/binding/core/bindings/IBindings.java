package $group__.utilities.binding.core.bindings;

import $group__.utilities.binding.core.IBinding;
import $group__.utilities.binding.core.NoSuchBindingTransformerException;
import $group__.utilities.binding.core.traits.IHasBindingKey;

public interface IBindings<B extends IBinding<?>>
		extends IHasBindingKey {
	boolean add(Iterable<? extends B> bindings)
			throws NoSuchBindingTransformerException;

	boolean remove(Iterable<? extends B> bindings);

	boolean removeAll();

	boolean isEmpty();
}
