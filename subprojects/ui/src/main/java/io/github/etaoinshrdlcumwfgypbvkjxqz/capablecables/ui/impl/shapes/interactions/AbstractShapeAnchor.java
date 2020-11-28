package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeAnchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeAnchorSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeDescriptorProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AffineTransformUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl.UIObjectUtilities;
import sun.misc.Cleaner;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;
import java.util.Optional;

public abstract class AbstractShapeAnchor
		implements IShapeAnchor {
	private OptionalWeakReference<IShapeAnchorSet> container = OptionalWeakReference.of(null);

	@Override
	public Optional<? extends IShapeAnchorSet> getContainer() {
		return container.getOptional();
	}

	protected void setContainer(@Nullable IShapeAnchorSet container) {
		this.container = OptionalWeakReference.of(container);
	}

	@Override
	public void anchor(IShapeDescriptorProvider from)
			throws ConcurrentModificationException {
		getTarget().ifPresent(target -> {
			AffineTransform transform;
			{
				Rectangle2D bounds = from.getAbsoluteShape().getBounds2D();
				Rectangle2D newBounds = (Rectangle2D) bounds.clone();
				double targetValue =
						getTargetSide().getValue(target.getAbsoluteShape().getBounds2D())
								+ getTargetSide().outwardsBy(getBorderThickness())
								.orElse(0);
				getOriginSide().getOpposite() // COMMENT set opposite side, avoid overshooting
						.ifPresent(oppositeOriginSide -> {
							double oppositeOriginSideCurrentValue = oppositeOriginSide.getValue(newBounds);
							boolean overshoots;
							switch (oppositeOriginSide.getType()) {
								case LOCATION:
									// COMMENT smaller means larger area
									overshoots = targetValue <= oppositeOriginSideCurrentValue;
									break;
								case SIZE:
									// COMMENT larger means larger area
									overshoots = targetValue >= oppositeOriginSideCurrentValue;
									break;
								default:
									throw new AssertionError();
							}
							if (overshoots) {
								oppositeOriginSide.setValue(newBounds,
										targetValue
												+ oppositeOriginSide.outwardsBy(1).orElseThrow(AssertionError::new));
							}
						});
				getOriginSide().setValue(newBounds, targetValue);
				transform = AffineTransformUtilities.getTransformFromTo(bounds, newBounds);
			}
			Rectangle2D relativeBounds = from.getShapeDescriptor().getShapeOutput().getBounds2D();
			UIObjectUtilities.transformRectangularShape(transform, relativeBounds, relativeBounds);
			from.modifyShape(() ->
					from.getShapeDescriptor().adapt(relativeBounds));
		});
	}

	@Override
	public void onContainerAdded(IShapeAnchorSet container) {
		setContainer(container);
		Cleaner.create(container,
				this::onContainerRemoved);
	}

	@Override
	public void onContainerRemoved() {
		setContainer(null);
	}

	@Override
	public String toString() {
		return ObjectUtilities.toStringImpl(this, StaticHolder.getObjectVariableMap());
	}
}
