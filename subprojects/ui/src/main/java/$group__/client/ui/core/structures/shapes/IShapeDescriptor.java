package $group__.client.ui.core.structures.shapes;

import $group__.client.ui.structures.shapes.descriptors.GenericShapeDescriptor;
import $group__.client.ui.structures.shapes.interactions.UIConstraint;
import com.google.common.collect.Streams;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.function.Supplier;

// TODO needs better design, but I cannot think of one
public interface IShapeDescriptor<S extends Shape> {
	IUIConstraint CONSTRAINT_MINIMUM = new UIConstraint(null, null, null, null, 1d, 1d, null, null);
	Rectangle2D SHAPE_PLACEHOLDER = new Rectangle2D.Double(0, 0, 1, 1);

	static void checkIsBeingModified(IShapeDescriptor<?> shapeDescriptor) throws IllegalStateException {
		if (!shapeDescriptor.isBeingModified())
			throw new IllegalStateException("Not marked as being modified");
	}

	static IShapeDescriptor<?> getShapeDescriptorPlaceholderView() { return new GenericShapeDescriptor(getShapePlaceholderView()); }

	static Rectangle2D getShapePlaceholderView() { return (Rectangle2D) SHAPE_PLACEHOLDER.clone(); }

	boolean isBeingModified();

	S getShapeOutput();

	List<IUIConstraint> getConstraintsView();

	List<IUIConstraint> getConstraintsRef()
			throws IllegalStateException;

	@SuppressWarnings({"UnstableApiUsage", "unchecked"})
	static void constrain(Rectangle2D bounds, Iterable<? extends IUIConstraint> constraints) {
		Streams.stream((Iterable<IUIConstraint>) constraints).unordered() // COMMENT should be safe
				.reduce(CONSTRAINT_MINIMUM, IUIConstraint::createIntersection)
				.constrain(bounds);
	}

	boolean modify(Supplier<? extends Boolean> action)
			throws ConcurrentModificationException;

	boolean bound(Rectangle2D bounds)
			throws IllegalStateException;

	boolean transform(AffineTransform transform)
			throws IllegalStateException;

	IUIAnchorSet<IUIAnchor> getAnchorsRef()
			throws IllegalStateException;
}
