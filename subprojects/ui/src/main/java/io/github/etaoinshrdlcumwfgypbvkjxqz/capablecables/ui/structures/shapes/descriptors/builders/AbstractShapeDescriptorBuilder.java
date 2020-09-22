package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.shapes.descriptors.builders;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptorBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.interactions.IShapeConstraint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.utilities.UIObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IHasGenericClass;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
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
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
	private final AffineTransform transform = new AffineTransform();
	private final Rectangle2D bounds = IShapeDescriptor.StaticHolder.getShapePlaceholder();
	private final List<IShapeConstraint> constraints = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);
	private final ConcurrentMap<String, Consumer<?>> properties =
			MapUtilities.newMapMakerSingleThreaded().makeMap();

	protected AbstractShapeDescriptorBuilder(Class<S> genericClass) { super(genericClass); }

	@Override
	public AbstractShapeDescriptorBuilder<S> setProperty(String key, @Nullable Object value) throws IllegalArgumentException {
		@Nullable Consumer<?> c = getProperties().get(key);
		if (c == null)
			throw new IllegalStateException(
					new LogMessageBuilder()
							.addMarkers(UIMarkers.getInstance()::getMarkerShape)
							.addKeyValue("key", key).addKeyValue("value", value)
							.addMessages(() -> getResourceBundle().getString("property.key.missing"))
							.build()
			);

		c.accept(CastUtilities.castUncheckedNullable(value)); // COMMENT ClassCastException may be thrown
		return this;
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

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
