package $group__.client.ui.core.structures.shapes;

import $group__.utilities.interfaces.IHasGenericClass;
import com.google.common.collect.Iterables;

import java.awt.*;
import java.awt.geom.AffineTransform;

public interface IShapeDescriptorBuilder<S extends Shape>
		extends IHasGenericClass<S> {
	static void addUIObjects(IShapeDescriptor<?> shapeDescriptor, Iterable<? extends IUIConstraint> constraints, Iterable<? extends IUIAnchor> anchors) {
		shapeDescriptor.modify(() -> {
			Iterables.addAll(shapeDescriptor.getConstraintsRef(), constraints);
			shapeDescriptor.getAnchorsRef().addAnchors(anchors);
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

	IShapeDescriptorBuilder<S> anchor(Iterable<? extends IUIAnchor> anchors);

	IShapeDescriptorBuilder<S> constrain(Iterable<? extends IUIConstraint> constraints);

	IShapeDescriptor<S> build();
}
