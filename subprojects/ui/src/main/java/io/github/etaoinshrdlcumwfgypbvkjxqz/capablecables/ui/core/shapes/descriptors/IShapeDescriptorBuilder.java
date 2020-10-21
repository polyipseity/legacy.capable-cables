package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors;

import com.google.common.collect.Iterables;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeConstraint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IHasGenericClass;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.AffineTransform;

@SuppressWarnings("UnusedReturnValue")
public interface IShapeDescriptorBuilder<S extends Shape>
		extends IHasGenericClass<S> {
	static void addUIObjects(IShapeDescriptor<?> shapeDescriptor, Iterable<? extends IShapeConstraint> constraints) {
		shapeDescriptor.modify(() -> {
			Iterables.addAll(shapeDescriptor.getConstraintsRef(), constraints);
			return false;
		});
	}

	IShapeDescriptorBuilder<S> setProperty(@NonNls CharSequence key, @Nullable Object value)
			throws IllegalArgumentException;

	IShapeDescriptorBuilder<S> transformConcatenate(AffineTransform transform);

	IShapeDescriptorBuilder<S> transformPreConcatenate(AffineTransform transform);

	IShapeDescriptorBuilder<S> setWidth(double width);

	IShapeDescriptorBuilder<S> setHeight(double height);

	IShapeDescriptorBuilder<S> setX(double x);

	IShapeDescriptorBuilder<S> setY(double y);

	IShapeDescriptorBuilder<S> constrain(Iterable<? extends IShapeConstraint> constraints);

	IShapeDescriptor<S> build();
}