package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.interactions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptor;

import java.util.ConcurrentModificationException;
import java.util.function.BooleanSupplier;

public interface IShapeDescriptorProvider {
	IShapeDescriptor<?> getShapeDescriptor();

	boolean modifyShape(BooleanSupplier action)
			throws ConcurrentModificationException;

	boolean isModifyingShape();
}
