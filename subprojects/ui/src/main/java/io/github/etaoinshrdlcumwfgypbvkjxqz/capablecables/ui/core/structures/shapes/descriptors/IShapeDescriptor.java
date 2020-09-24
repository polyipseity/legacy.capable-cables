package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors;

import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.interactions.IShapeConstraint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.shapes.descriptors.GenericShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.shapes.interactions.ShapeConstraint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableRectangle2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Supplier;

// TODO needs better design, but I cannot think of one
public interface IShapeDescriptor<S extends Shape> {

	boolean isBeingModified();

	S getShapeOutput();

	List<IShapeConstraint> getConstraintsView();

	List<IShapeConstraint> getConstraintsRef()
			throws IllegalStateException;

	boolean modify(Supplier<? extends Boolean> action)
			throws ConcurrentModificationException;

	boolean bound(Rectangle2D bounds)
			throws IllegalStateException;

	boolean transform(AffineTransform transform)
			throws IllegalStateException;

	enum StaticHolder {
		;
		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
		private static final IShapeConstraint CONSTRAINT_MINIMUM = new ShapeConstraint(null, null, null, null, 1d, 1d, null, null);
		private static final ImmutableRectangle2D SHAPE_PLACEHOLDER = ImmutableRectangle2D.of(0, 0, 1, 1);

		public static void checkIsBeingModified(IShapeDescriptor<?> shapeDescriptor) throws IllegalStateException {
			if (!shapeDescriptor.isBeingModified())
				throw new IllegalStateException(
						new LogMessageBuilder()
								.addMarkers(UIMarkers.getInstance()::getMarkerShape)
								.addKeyValue("shapeDescriptor", shapeDescriptor)
								.addMessages(() -> getResourceBundle().getString("modifying.check.fail"))
								.build()
				);
		}

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

		public static IShapeDescriptor<?> getShapeDescriptorPlaceholder() { return new GenericShapeDescriptor(getShapePlaceholder()); }

		public static ImmutableRectangle2D getShapePlaceholder() { return SHAPE_PLACEHOLDER; }

		@SuppressWarnings({"UnstableApiUsage", "unchecked"})
		public static ImmutableRectangle2D constrain(Rectangle2D bounds, Iterable<? extends IShapeConstraint> constraints) {
			return Streams.stream((Iterable<IShapeConstraint>) constraints).unordered() // COMMENT should be safe
					.reduce(getConstraintMinimum(), IShapeConstraint::createIntersection)
					.constrain(bounds);
		}

		public static IShapeConstraint getConstraintMinimum() { return CONSTRAINT_MINIMUM; }
	}
}
