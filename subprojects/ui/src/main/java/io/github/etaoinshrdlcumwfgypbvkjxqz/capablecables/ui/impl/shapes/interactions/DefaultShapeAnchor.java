package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions;

import com.google.common.collect.Sets;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeAnchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeAnchorSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeDescriptorProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AffineTransformUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl.UIObjectUtilities;
import sun.misc.Cleaner;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;
import java.util.Optional;

@Immutable
public final class DefaultShapeAnchor implements IShapeAnchor {
	private final OptionalWeakReference<IShapeDescriptorProvider> target;
	private final EnumUISide originSide, targetSide;
	private final double borderThickness;
	private OptionalWeakReference<IShapeAnchorSet> container = new OptionalWeakReference<>(null);

	public DefaultShapeAnchor(IShapeDescriptorProvider target, EnumUISide originSide, EnumUISide targetSide) { this(target, originSide, targetSide, 0); }

	public DefaultShapeAnchor(IShapeDescriptorProvider target, EnumUISide originSide, EnumUISide targetSide, double borderThickness) {
		this.target = new OptionalWeakReference<>(target);
		this.originSide = originSide;
		this.targetSide = targetSide;
		this.borderThickness = borderThickness;
		Cleaner.create(target, () ->
				getContainer().ifPresent(c ->
						c.removeSides(Sets.immutableEnumSet(getOriginSide()))));
	}

	@Override
	public String toString() { return ObjectUtilities.toStringImpl(this, StaticHolder.getObjectVariableMap()); }

	@Override
	public Optional<? extends IShapeDescriptorProvider> getTarget() { return target.getOptional(); }

	@Override
	public Optional<? extends IShapeAnchorSet> getContainer() { return container.getOptional(); }

	@Override
	public double getBorderThickness() { return borderThickness; }

	protected void setContainer(@Nullable IShapeAnchorSet container) { this.container = new OptionalWeakReference<>(container); }

	@Override
	public EnumUISide getOriginSide() { return originSide; }

	@Override
	public EnumUISide getTargetSide() { return targetSide; }

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
	public void onContainerRemoved() { setContainer(null); }


	@Override
	public void onContainerAdded(IShapeAnchorSet container) {
		setContainer(container);
		Cleaner.create(container,
				this::onContainerRemoved);
	}
}
