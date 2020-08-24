package $group__.client.ui.structures.shapes.interactions;

import $group__.client.ui.core.structures.shapes.interactions.IShapeAnchor;
import $group__.client.ui.core.structures.shapes.interactions.IShapeAnchorSet;
import $group__.client.ui.core.structures.shapes.interactions.IShapeDescriptorProvider;
import $group__.client.ui.structures.EnumUISide;
import $group__.utilities.ObjectUtilities;
import com.google.common.collect.Sets;
import sun.misc.Cleaner;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;
import java.util.ConcurrentModificationException;
import java.util.Optional;

@Immutable
public final class ShapeAnchor implements IShapeAnchor {
	protected final WeakReference<IShapeDescriptorProvider> target;
	protected final EnumUISide originSide, targetSide;
	protected final double borderThickness;
	protected WeakReference<IShapeAnchorSet> container = new WeakReference<>(null);

	public ShapeAnchor(IShapeDescriptorProvider target, EnumUISide originSide, EnumUISide targetSide) { this(target, originSide, targetSide, 0); }

	public ShapeAnchor(IShapeDescriptorProvider target, EnumUISide originSide, EnumUISide targetSide, double borderThickness) {
		this.target = new WeakReference<>(target);
		this.originSide = originSide;
		this.targetSide = targetSide;
		this.borderThickness = borderThickness;
		Cleaner.create(target, () ->
				getContainer().ifPresent(c ->
						c.removeSides(Sets.immutableEnumSet(getOriginSide()))));
	}

	@Override
	public Optional<? extends IShapeAnchorSet> getContainer() { return Optional.ofNullable(container.get()); }

	protected void setContainer(@Nullable IShapeAnchorSet container) { this.container = new WeakReference<>(container); }

	@Override
	public double getBorderThickness() { return borderThickness; }

	@Override
	public Optional<? extends IShapeDescriptorProvider> getTarget() { return Optional.ofNullable(target.get()); }

	@Override
	public EnumUISide getOriginSide() { return originSide; }

	@Override
	public EnumUISide getTargetSide() { return targetSide; }

	@Override
	public void anchor(IShapeDescriptorProvider from)
			throws ConcurrentModificationException {
		getTarget().ifPresent(t -> {
			Rectangle2D bounds = from.getShapeDescriptor().getShapeOutput().getBounds2D();
			Double targetValue =
					getOriginSide().getApplyBorder().apply(
							getTargetSide().getGetter().apply(t.getShapeDescriptor().getShapeOutput().getBounds2D()),
							getBorderThickness());
			getOriginSide().getOpposite() // COMMENT set opposite side, avoid overshooting
					.ifPresent(so -> {
						Double oppositeTargetValue = getOriginSide().getApplyBorder().apply(targetValue, 1d);
						oppositeTargetValue = oppositeTargetValue < targetValue ?
								Math.min(oppositeTargetValue, so.getGetter().apply(bounds)) // COMMENT lesser means larger area
								: Math.max(oppositeTargetValue, so.getGetter().apply(bounds)); // COMMENT greater means larger area
						so.getSetter().accept(bounds, oppositeTargetValue);
					});
			getOriginSide().getSetter().accept(bounds, targetValue);
			from.modifyShape(() ->
					from.getShapeDescriptor().bound(bounds));
		});
	}

	@Override
	public void onContainerRemoved() { setContainer(null); }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, OBJECT_VARIABLES_MAP); }

	@Override
	public void onContainerAdded(IShapeAnchorSet container) {
		setContainer(container);
		Cleaner.create(container,
				this::onContainerRemoved);
	}
}
