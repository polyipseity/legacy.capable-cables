package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.shapes.descriptors;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.interactions.IShapeConstraint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;

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

	protected final List<IShapeConstraint> constraints = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);
	protected boolean beingModified = false;

	protected AbstractShapeDescriptor() {}

	@Override
	public List<IShapeConstraint> getConstraintsView() { return ImmutableList.copyOf(getConstraints()); }

	@Override
	public List<IShapeConstraint> getConstraintsRef()
			throws IllegalStateException {
		StaticHolder.checkIsBeingModified(this);
		return getConstraints();
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	protected void setBeingModified(boolean beingModified) { this.beingModified = beingModified; }

	protected boolean modify0(BooleanSupplier action) {
		boolean ret = action.getAsBoolean();
		if (ret) {
			Rectangle2D bounds = getShapeOutput().getBounds2D();
			bound(StaticHolder.constrain(getConstraints(), bounds, bounds));
		}
		return ret;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IShapeConstraint> getConstraints() { return constraints; }

	@Override
	public boolean isBeingModified() { return beingModified; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean bound(Rectangle2D bounds)
			throws IllegalStateException {
		StaticHolder.checkIsBeingModified(this);
		return false;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean transform(AffineTransform transform)
			throws IllegalStateException {
		StaticHolder.checkIsBeingModified(this);
		return false;
	}
}
