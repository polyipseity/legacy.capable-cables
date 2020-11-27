package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors;

import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeConstraint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors.GenericShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions.ShapeConstraint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.BooleanSupplier;

// TODO needs better design, but I cannot think of one
public interface IShapeDescriptor<S extends Shape> {

	static void checkIsBeingModified(IShapeDescriptor<?> shapeDescriptor) throws IllegalStateException {
		if (!shapeDescriptor.isBeingModified())
			throw new IllegalStateException(
					new LogMessageBuilder()
							.addMarkers(UIMarkers.getInstance()::getMarkerShape)
							.addKeyValue("shapeDescriptor", shapeDescriptor)
							.addMessages(() -> StaticHolder.getResourceBundle().getString("modifying.check.fail"))
							.build()
			);
	}

	@SuppressWarnings({"UnstableApiUsage", "unchecked"})
	static <R extends RectangularShape> R constrain(Iterable<? extends IShapeConstraint> constraints, RectangularShape source, R destination) {
		return Streams.stream((Iterable<IShapeConstraint>) constraints).unordered() // COMMENT should be safe
				.reduce(StaticHolder.getConstraintMinimum(), IShapeConstraint::createIntersection)
				.constrain(source, destination);
	}

	boolean isBeingModified();

	boolean isDynamic(); // TODO implement this for dynamic shape descriptors i.e. returned shape can change by itself

	S getShapeOutput();

	List<IShapeConstraint> getConstraintsView();

	List<IShapeConstraint> getConstraintsRef()
			throws IllegalStateException;

	boolean modify(BooleanSupplier action)
			throws ConcurrentModificationException;

	boolean crop(Rectangle2D bounds)
			throws IllegalStateException;

	boolean adapt(Rectangle2D frame)
			throws IllegalStateException;

	boolean transform(AffineTransform transform)
			throws IllegalStateException;

	enum StaticHolder {
		;
		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
		private static final IShapeConstraint CONSTRAINT_MINIMUM = new ShapeConstraint(null, null, null, null, 1d, 1d, null, null);
		private static final Rectangle2D SHAPE_PLACEHOLDER = new Rectangle2D.Double(0, 0, 1, 1);

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

		public static IShapeDescriptor<?> getShapeDescriptorPlaceholder() { return new GenericShapeDescriptor(getShapePlaceholder()); }

		public static Rectangle2D getShapePlaceholder() { return (Rectangle2D) SHAPE_PLACEHOLDER.clone(); }

		public static IShapeConstraint getConstraintMinimum() { return CONSTRAINT_MINIMUM; }
	}
}
