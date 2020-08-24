package $group__.client.ui.core.mvvm.views;

import $group__.client.ui.core.structures.shapes.descriptors.IShapeDescriptor;

import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.function.Function;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIReshapeExplicitly<S extends Shape> {
	static boolean refresh(IUIReshapeExplicitly<?> trait) { return trait.reshape(s -> true); }

	boolean reshape(Function<? super IShapeDescriptor<? super S>, ? extends Boolean> action)
			throws ConcurrentModificationException;
}
