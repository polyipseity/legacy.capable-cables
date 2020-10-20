package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;

import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.function.BooleanSupplier;

public interface IShapeDescriptorProvider {
	IShapeDescriptor<?> getShapeDescriptor();

	boolean modifyShape(BooleanSupplier action)
			throws ConcurrentModificationException;

	boolean isModifyingShape();

	Shape getAbsoluteShape()
			throws IllegalStateException;
}
