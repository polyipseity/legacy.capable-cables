package $group__.client.ui.mvvm.core.structures;

import $group__.client.ui.mvvm.structures.UIConstraint;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.function.Function;

import static $group__.client.ui.mvvm.core.structures.IUIConstraint.CONSTRAINT_NULL_VALUE;
import static $group__.client.ui.mvvm.core.structures.IUIConstraint.Constants.getConstraintNullRectangleView;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public interface IShapeDescriptor<S extends Shape, A extends IUIAnchorSet<?>> {
	static void checkIsBeingModified(IShapeDescriptor<?, ?> shapeDescriptor) throws IllegalStateException {
		if (!shapeDescriptor.isBeingModified())
			throw new IllegalStateException("Not marked as being modified");
	}

	boolean isBeingModified();

	Shape getShapeProcessed();

	List<IUIConstraint> getConstraintsView();

	List<IUIConstraint> getConstraintsRef()
			throws IllegalStateException;

	A getAnchorSet();

	<T extends IShapeDescriptor<?, ?>> boolean modify(T self, Function<? super T, Boolean> action)
			throws ConcurrentModificationException;

	boolean setShape(S shape)
			throws IllegalStateException;

	S getShapeRef()
			throws IllegalStateException;

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
