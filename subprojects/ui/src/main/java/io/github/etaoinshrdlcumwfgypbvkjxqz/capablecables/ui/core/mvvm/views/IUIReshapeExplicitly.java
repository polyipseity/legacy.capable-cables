package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.FunctionUtilities;

import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.function.Predicate;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIReshapeExplicitly<S extends Shape> {
	@SuppressWarnings("UnusedReturnValue")
	static boolean refresh(IUIReshapeExplicitly<?> trait) { return trait.reshape(FunctionUtilities.getAlwaysTruePredicate()); }

	boolean reshape(Predicate<? super IShapeDescriptor<? super S>> action)
			throws ConcurrentModificationException;
}
