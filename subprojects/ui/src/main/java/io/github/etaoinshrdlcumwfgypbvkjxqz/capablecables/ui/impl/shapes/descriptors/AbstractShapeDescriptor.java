package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeConstraint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.BooleanSupplier;

public abstract class AbstractShapeDescriptor<S extends Shape>
		implements IShapeDescriptor<S> {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
	private final List<IShapeConstraint> constraints = new ArrayList<>(CapacityUtilities.getInitialCapacitySmall());
	private boolean beingModified = false;

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean modify(BooleanSupplier action)
			throws ConcurrentModificationException {
		if (isBeingModified())
			throw new ConcurrentModificationException(
					new LogMessageBuilder()
							.addMarkers(UIMarkers.getInstance()::getMarkerShape)
							.addKeyValue("this", this).addKeyValue("action", action)
							.addMessages(() -> getResourceBundle().getString("modify.concurrent"))
							.build()
			);
		setBeingModified(true);
		boolean ret = modify0(action);
		setBeingModified(false);
		return ret;
	}

	protected AbstractShapeDescriptor() {}


	@Override
	public List<IShapeConstraint> getConstraintsView() { return ImmutableList.copyOf(getConstraints()); }

	@Override
	public boolean isBeingModified() { return beingModified; }

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	protected void setBeingModified(boolean beingModified) { this.beingModified = beingModified; }

	@Override
	public List<IShapeConstraint> getConstraintsRef()
			throws IllegalStateException {
		IShapeDescriptor.checkIsBeingModified(this);
		return getConstraints();
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean crop(Rectangle2D bounds)
			throws IllegalStateException {
		IShapeDescriptor.checkIsBeingModified(this);
		return false;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean adapt(Rectangle2D frame) throws IllegalStateException {
		IShapeDescriptor.checkIsBeingModified(this);
		return false;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean transform(AffineTransform transform)
			throws IllegalStateException {
		IShapeDescriptor.checkIsBeingModified(this);
		return false;
	}

	protected boolean modify0(BooleanSupplier action) {
		boolean ret = action.getAsBoolean();
		if (ret) {
			Rectangle2D bounds = getShapeOutput().getBounds2D();
			adapt(IShapeDescriptor.constrain(getConstraints(), bounds, bounds)); // TODO what if the shape is dynamic
		}
		return ret;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IShapeConstraint> getConstraints() { return constraints; }

	protected abstract boolean adapt0(Rectangle2D frame);
}
