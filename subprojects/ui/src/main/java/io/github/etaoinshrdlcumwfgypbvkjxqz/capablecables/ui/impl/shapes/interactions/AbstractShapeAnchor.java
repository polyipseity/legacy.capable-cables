package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.shapes.interactions.IShapeAnchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.shapes.interactions.IShapeAnchorSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.shapes.interactions.IShapeDescriptorProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AffineTransformUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
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
			// COMMENT calculate new absolute bounds
			Rectangle2D absoluteBounds = from.getAbsoluteShape().getBounds2D();
			Rectangle2D newAbsoluteBounds = (Rectangle2D) absoluteBounds.clone();
			double targetValue =
					getTargetSide().getValue(target.getAbsoluteShape().getBounds2D())
							+ getTargetSide().outwardsBy(getBorderThickness())
							.orElse(0);
			getOriginSide().getOpposite() // COMMENT set opposite side, avoid overshooting
					.ifPresent(oppositeOriginSide -> {
						double oppositeOriginSideCurrentValue = oppositeOriginSide.getValue(newAbsoluteBounds);
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
							oppositeOriginSide.setValue(newAbsoluteBounds,
									targetValue
											+ oppositeOriginSide.outwardsBy(1D).orElseThrow(AssertionError::new));
						}
					});
			getOriginSide().setValue(newAbsoluteBounds, targetValue);

			// COMMENT convert new absolute bounds to the relative coordinate space
			Rectangle2D relativeBounds = from.getShapeDescriptor().getShapeOutput().getBounds2D();
			AffineTransform toRelativeTransform = AffineTransformUtilities.getTransformFromTo(absoluteBounds, relativeBounds);
			Rectangle2D newRelativeBounds = toRelativeTransform.createTransformedShape(newAbsoluteBounds).getBounds2D();

			// COMMENT actually transform
			AffineTransform transform = AffineTransformUtilities.getTransformFromTo(relativeBounds, newRelativeBounds);
			from.modifyShape(() -> from.getShapeDescriptor().transform(transform));
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
