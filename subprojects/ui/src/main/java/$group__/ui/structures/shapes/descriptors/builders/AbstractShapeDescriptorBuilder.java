package $group__.ui.structures.shapes.descriptors.builders;

import $group__.ui.UIConfiguration;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptorBuilder;
import $group__.ui.core.structures.shapes.interactions.IShapeConstraint;
import $group__.ui.utilities.UIObjectUtilities;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.LogMessageBuilder;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.templates.CommonConfigurationTemplate;
import com.google.common.collect.Iterables;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

public abstract class AbstractShapeDescriptorBuilder<S extends Shape>
		extends IHasGenericClass.Impl<S>
		implements IShapeDescriptorBuilder<S> {
	protected static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
	protected final AffineTransform transform = new AffineTransform();
	protected final Rectangle2D bounds = IShapeDescriptor.getShapePlaceholderCopy();
	protected final List<IShapeConstraint> constraints = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);
	protected final ConcurrentMap<String, Consumer<?>> properties =
			MapUtilities.newMapMakerSingleThreaded().makeMap();

	protected AbstractShapeDescriptorBuilder(Class<S> genericClass) { super(genericClass); }

	@Override
	public AbstractShapeDescriptorBuilder<S> setProperty(String key, @Nullable Object value) throws IllegalArgumentException {
		@Nullable Consumer<?> c = getProperties().get(key);
		if (c == null)
			throw ThrowableUtilities.logAndThrow(
					new IllegalStateException(
							new LogMessageBuilder()
									.addKeyValue("key", key).addKeyValue("value", value)
									.appendMessages(RESOURCE_BUNDLE.getString("property.key.missing"))
									.build()
					),
					UIConfiguration.getInstance().getLogger());

		c.accept(CastUtilities.castUncheckedNullable(value)); // COMMENT ClassCastException may be thrown
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
