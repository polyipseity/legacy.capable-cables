package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors.builders;

import com.google.common.collect.Iterators;
import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.shapes.descriptors.IShapeDescriptorBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.shapes.interactions.IShapeConstraint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl.UIObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import org.jetbrains.annotations.NonNls;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public abstract class AbstractShapeDescriptorBuilder<S extends Shape>
		implements IShapeDescriptorBuilder<S> {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
	private final AffineTransform transform = new AffineTransform();
	private final Rectangle2D bounds = IShapeDescriptor.StaticHolder.getShapePlaceholder();
	private final List<IShapeConstraint> constraints = new ArrayList<>(CapacityUtilities.getInitialCapacitySmall());
	@SuppressWarnings({"FieldCanBeLocal", "UnstableApiUsage"})
	private final TypeToken<S> typeToken;

	@SuppressWarnings("UnstableApiUsage")
	protected AbstractShapeDescriptorBuilder(Class<S> type) {
		this.typeToken = TypeToken.of(type);
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public TypeToken<S> getTypeToken() {
		return typeToken;
	}

	@Override
	public AbstractShapeDescriptorBuilder<S> setProperty(@NonNls CharSequence key, @Nullable Object value) throws IllegalArgumentException {
		throw new IllegalStateException(
				new LogMessageBuilder()
						.addMarkers(UIMarkers.getInstance()::getMarkerShape)
						.addKeyValue("key", key).addKeyValue("value", value)
						.addMessages(() -> getResourceBundle().getString("property.key.missing"))
						.build()
		);
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

	@SuppressWarnings("AutoUnboxing")
	@Override
	public AbstractShapeDescriptorBuilder<S> setWidth(double width) {
		UIObjectUtilities.acceptRectangularShape(getBounds(), (rx, ry, rw, rh) ->
				getBounds().setFrame(rx, ry, width, rh));
		return this;
	}

	@SuppressWarnings("AutoUnboxing")
	@Override
	public AbstractShapeDescriptorBuilder<S> setHeight(double height) {
		UIObjectUtilities.acceptRectangularShape(getBounds(), (rx, ry, rw, rh) ->
				getBounds().setFrame(rx, ry, rw, height));
		return this;
	}

	@SuppressWarnings("AutoUnboxing")
	@Override
	public AbstractShapeDescriptorBuilder<S> setX(double x) {
		UIObjectUtilities.acceptRectangularShape(getBounds(), (rx, ry, rw, rh) ->
				getBounds().setFrame(x, ry, rw, rh));
		return this;
	}

	@SuppressWarnings("AutoUnboxing")
	@Override
	public AbstractShapeDescriptorBuilder<S> setY(double y) {
		UIObjectUtilities.acceptRectangularShape(getBounds(), (rx, ry, rw, rh) ->
				getBounds().setFrame(rx, y, rw, rh));
		return this;
	}

	@Override
	public AbstractShapeDescriptorBuilder<S> constrain(Iterator<? extends IShapeConstraint> constraints) {
		Iterators.addAll(getConstraints(), constraints);
		return this;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IShapeConstraint> getConstraints() { return constraints; }

	protected Rectangle2D getBounds() { return bounds; }

	protected AffineTransform getTransform() { return transform; }

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
}
