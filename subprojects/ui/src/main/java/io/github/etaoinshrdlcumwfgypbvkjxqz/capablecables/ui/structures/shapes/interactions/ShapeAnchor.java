package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.shapes.interactions;

import com.google.common.collect.Sets;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.interactions.IShapeAnchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.interactions.IShapeAnchorSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.interactions.IShapeDescriptorProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import sun.misc.Cleaner;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;
import java.util.Optional;

@Immutable
public final class ShapeAnchor implements IShapeAnchor {
	protected final OptionalWeakReference<IShapeDescriptorProvider> target;
	protected final EnumUISide originSide, targetSide;
	protected final double borderThickness;
	protected OptionalWeakReference<IShapeAnchorSet> container = new OptionalWeakReference<>(null);

	public ShapeAnchor(IShapeDescriptorProvider target, EnumUISide originSide, EnumUISide targetSide) { this(target, originSide, targetSide, 0); }

	public ShapeAnchor(IShapeDescriptorProvider target, EnumUISide originSide, EnumUISide targetSide, double borderThickness) {
		this.target = new OptionalWeakReference<>(target);
		this.originSide = originSide;
		this.targetSide = targetSide;
		this.borderThickness = borderThickness;
		Cleaner.create(target, () ->
				getContainer().ifPresent(c ->
						c.removeSides(Sets.immutableEnumSet(getOriginSide()))));
	}

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
			// TODO not context aware, fix
			Rectangle2D bounds = from.getShapeDescriptor().getShapeOutput().getBounds2D();
			double targetValue =
					getOriginSide().getApplyBorder().applyAsDouble(
							getTargetSide().getGetter().applyAsDouble(target.getShapeDescriptor().getShapeOutput().getBounds2D()),
							getBorderThickness());
			getOriginSide().getOpposite() // COMMENT set opposite side, avoid overshooting
					.ifPresent(oppositeOriginSide -> {
						double oppositeOriginSideCurrentValue = oppositeOriginSide.getGetter().applyAsDouble(bounds);
						double oppositeOriginSideTargetValue = getOriginSide().getApplyBorder().applyAsDouble(targetValue, 1d);
						oppositeOriginSideTargetValue = oppositeOriginSideTargetValue < targetValue ?
								Math.min(oppositeOriginSideTargetValue, oppositeOriginSideCurrentValue) // COMMENT lesser means larger area
								: Math.max(oppositeOriginSideTargetValue, oppositeOriginSideCurrentValue); // COMMENT greater means larger area
						oppositeOriginSide.getSetter().accept(bounds, oppositeOriginSideTargetValue);
					});
			getOriginSide().getSetter().accept(bounds, targetValue);
			from.modifyShape(() ->
					from.getShapeDescriptor().adapt(bounds));
		});
	}

	@Override
	public void onContainerRemoved() { setContainer(null); }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, StaticHolder.getObjectVariablesMap()); }

	@Override
	public void onContainerAdded(IShapeAnchorSet container) {
		setContainer(container);
		Cleaner.create(container,
				this::onContainerRemoved);
	}
}
