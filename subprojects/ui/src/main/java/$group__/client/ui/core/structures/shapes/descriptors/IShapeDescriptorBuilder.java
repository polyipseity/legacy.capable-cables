package $group__.client.ui.core.structures.shapes.descriptors;

import $group__.client.ui.core.structures.shapes.interactions.IShapeConstraint;
import $group__.utilities.interfaces.IHasGenericClass;
import com.google.common.collect.Iterables;

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

	IShapeDescriptorBuilder<S> setProperty(String property, Object value)
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
