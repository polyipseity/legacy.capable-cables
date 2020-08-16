package $group__.client.ui.mvvm.core.structures;

import $group__.client.ui.mvvm.structures.UIConstraint;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.function.Supplier;

import static $group__.client.ui.mvvm.core.structures.IUIConstraint.CONSTRAINT_NULL_VALUE;
import static $group__.client.ui.mvvm.core.structures.IUIConstraint.Constants.getConstraintNullRectangleView;

// TODO some sort of builder
// TODO needs better design, but I cannot think of one
@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public interface IShapeDescriptor<S extends Shape> {
	static void checkIsBeingModified(IShapeDescriptor<?> shapeDescriptor) throws IllegalStateException {
		if (!shapeDescriptor.isBeingModified())
			throw new IllegalStateException("Not marked as being modified");
	}

	boolean isBeingModified();

	S getShapeOutput();

	List<IUIConstraint> getConstraintsView();

	List<IUIConstraint> getConstraintsRef()
			throws IllegalStateException;

	IUIAnchorSet<IUIAnchor> getAnchorSet();

	boolean modify(Supplier<? extends Boolean> action)
			throws ConcurrentModificationException;

	boolean bound(Rectangle2D bounds)
			throws IllegalStateException;

	boolean transform(AffineTransform transform)
			throws IllegalStateException;

	enum Constants {
		;

		private static final UIConstraint CONSTRAINT_MINIMUM = new UIConstraint(new Rectangle2D.Double(CONSTRAINT_NULL_VALUE, CONSTRAINT_NULL_VALUE, 1, 1), getConstraintNullRectangleView());

		public static IUIConstraint getConstraintMinimumView() { return CONSTRAINT_MINIMUM.copy(); }
	}
}
