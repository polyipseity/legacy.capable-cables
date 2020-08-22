package $group__.client.ui.structures.shapes.builders;

import $group__.client.ui.core.structures.shapes.IShapeDescriptor;
import $group__.client.ui.core.structures.shapes.IShapeDescriptorBuilder;
import $group__.client.ui.core.structures.shapes.IUIAnchor;
import $group__.client.ui.core.structures.shapes.IUIConstraint;
import $group__.client.ui.utilities.UIObjectUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.MapUtilities;
import $group__.utilities.ThrowableUtilities.BecauseOf;
import $group__.utilities.interfaces.IHasGenericClass;
import com.google.common.collect.Iterables;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

public abstract class AbstractShapeDescriptorBuilder<S extends Shape>
		extends IHasGenericClass.Impl<S>
		implements IShapeDescriptorBuilder<S> {
	protected final AffineTransform transform = new AffineTransform();
	protected final Rectangle2D bounds = IShapeDescriptor.getShapePlaceholderView();
	protected final List<IUIAnchor> anchors = new LinkedList<>();
	protected final List<IUIConstraint> constraints = new LinkedList<>();
	protected final ConcurrentMap<String, Consumer<?>> properties =
			MapUtilities.getMapMakerSingleThreaded().makeMap();

	protected AbstractShapeDescriptorBuilder(Class<S> genericClass) { super(genericClass); }

	@Override
	public AbstractShapeDescriptorBuilder<S> setProperty(String property, Object value) throws IllegalArgumentException {
		Optional.ofNullable(getProperties().get(property))
				.filter(c -> {
					c.accept(CastUtilities.castUnchecked(value)); // COMMENT ClassCastException may be thrown
					return true;
				})
				.orElseThrow(() ->
						BecauseOf.illegalArgument("Property does not exist",
								"property", property,
								"getProperties()", getProperties(),
								"value", value));
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
	public AbstractShapeDescriptorBuilder<S> anchor(Iterable<? extends IUIAnchor> anchors) {
		Iterables.addAll(getAnchors(), anchors);
		return this;
	}

	@Override
	public AbstractShapeDescriptorBuilder<S> constrain(Iterable<? extends IUIConstraint> constraints) {
		Iterables.addAll(getConstraints(), constraints);
		return this;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IUIConstraint> getConstraints() { return constraints; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IUIAnchor> getAnchors() { return anchors; }

	protected Rectangle2D getBounds() { return bounds; }

	protected AffineTransform getTransform() { return transform; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<String, Consumer<?>> getProperties() { return properties; }
}
