package $group__.ui.core.mvvm.views;

import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.utilities.functions.FunctionUtilities;

import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.function.Predicate;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIReshapeExplicitly<S extends Shape> {
	static boolean refresh(IUIReshapeExplicitly<?> trait) { return trait.reshape(FunctionUtilities.alwaysTruePredicate()); }

	boolean reshape(Predicate<? super IShapeDescriptor<? super S>> action)
			throws ConcurrentModificationException;
}
