package $group__.ui.structures.shapes.descriptors.builders;

import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptorBuilder;
import $group__.ui.core.structures.shapes.interactions.IShapeConstraint;
import $group__.ui.utilities.UIObjectUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.MapUtilities;
import $group__.utilities.ThrowableUtilities.BecauseOf;
import $group__.utilities.interfaces.IHasGenericClass;
import com.google.common.collect.Iterables;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

public abstract class AbstractShapeDescriptorBuilder<S extends Shape>
		extends IHasGenericClass.Impl<S>
		implements IShapeDescriptorBuilder<S> {
	protected final AffineTransform transform = new AffineTransform();
	protected final Rectangle2D bounds = IShapeDescriptor.getShapePlaceholderCopy();
	protected final List<IShapeConstraint> constraints = new LinkedList<>();
	protected final ConcurrentMap<String, Consumer<?>> properties =
			MapUtilities.getMapMakerSingleThreaded().makeMap();

	protected AbstractShapeDescriptorBuilder(Class<S> genericClass) { super(genericClass); }

	@Override
	public AbstractShapeDescriptorBuilder<S> setProperty(String property, Object value) throws IllegalArgumentException {
		@Nullable Consumer<?> c = getProperties().get(property);
		if (c == null)
			throw BecauseOf.illegalArgument("Property does not exist",
					"property", property,
					"getProperties()", getProperties(),
					"value", value);
		c.accept(CastUtilities.castUnchecked(value)); // COMMENT ClassCastException may be thrown
		return this;
	}

	@Override
	public AbstractShapeDescriptorBuilder<S> transformConcatenate(AffineTransform transform) {
		getTransform().concatenate(transform);
		return this;
	}

	@Override
	public AbstractShapeDescriptorBuilder<S> transformPreConcatenate(AffineTransform transform) {
		getTransform().preConcatenate(transform);
		return this;
	}

	@Override
	public AbstractShapeDescriptorBuilder<S> setWidth(double width) {
		UIObjectUtilities.acceptRectangular(getBounds(), (rx, ry, rw, rh) ->
				getBounds().setFrame(rx, ry, width, rh));
		return this;
	}

	@Override
	public AbstractShapeDescriptorBuilder<S> setHeight(double height) {
		UIObjectUtilities.acceptRectangular(getBounds(), (rx, ry, rw, rh) ->
				getBounds().setFrame(rx, ry, rw, height));
		return this;
	}

	@Override
	public AbstractShapeDescriptorBuilder<S> setX(double x) {
		UIObjectUtilities.acceptRectangular(getBounds(), (rx, ry, rw, rh) ->
				getBounds().setFrame(x, ry, rw, rh));
		return this;
	}

	@Override
	public AbstractShapeDescriptorBuilder<S> setY(double y) {
		UIObjectUtilities.acceptRectangular(getBounds(), (rx, ry, rw, rh) ->
				getBounds().setFrame(rx, y, rw, rh));
		return this;
	}

	@Override
	public AbstractShapeDescriptorBuilder<S> constrain(Iterable<? extends IShapeConstraint> constraints) {
		Iterables.addAll(getConstraints(), constraints);
		return this;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IShapeConstraint> getConstraints() { return constraints; }

	protected Rectangle2D getBounds() { return bounds; }

	protected AffineTransform getTransform() { return transform; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<String, Consumer<?>> getProperties() { return properties; }
}
