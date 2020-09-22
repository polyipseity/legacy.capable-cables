package $group__.ui.core.structures.shapes.descriptors;

import $group__.ui.core.structures.shapes.interactions.IShapeConstraint;
import $group__.ui.structures.shapes.descriptors.GenericShapeDescriptor;
import $group__.ui.structures.shapes.interactions.ShapeConstraint;
import com.google.common.collect.Streams;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.function.Supplier;

// TODO needs better design, but I cannot think of one
public interface IShapeDescriptor<S extends Shape> {
	IShapeConstraint CONSTRAINT_MINIMUM = new ShapeConstraint(null, null, null, null, 1d, 1d, null, null);
	Rectangle2D SHAPE_PLACEHOLDER = new Rectangle2D.Double(0, 0, 1, 1);

	static void checkIsBeingModified(IShapeDescriptor<?> shapeDescriptor) throws IllegalStateException {
		if (!shapeDescriptor.isBeingModified())
			throw new IllegalStateException("Not marked as being modified");
	}

	static IShapeDescriptor<?> getShapeDescriptorPlaceholder() { return new GenericShapeDescriptor(getShapePlaceholderCopy()); }

	static Rectangle2D getShapePlaceholderCopy() { return (Rectangle2D) SHAPE_PLACEHOLDER.clone(); }

	boolean isBeingModified();

	S getShapeOutput();

	@SuppressWarnings({"UnstableApiUsage", "unchecked"})
	static void constrain(Rectangle2D bounds, Iterable<? extends IShapeConstraint> constraints) {
		Streams.stream((Iterable<IShapeConstraint>) constraints).unordered() // COMMENT should be safe
				.reduce(CONSTRAINT_MINIMUM, IShapeConstraint::createIntersection)
				.constrain(bounds);
	}

	List<IShapeConstraint> getConstraintsView();

	List<IShapeConstraint> getConstraintsRef()
			throws IllegalStateException;

	boolean modify(Supplier<? extends Boolean> action)
			throws ConcurrentModificationException;

	boolean bound(Rectangle2D bounds)
			throws IllegalStateException;

	boolean transform(AffineTransform transform)
			throws IllegalStateException;
}
