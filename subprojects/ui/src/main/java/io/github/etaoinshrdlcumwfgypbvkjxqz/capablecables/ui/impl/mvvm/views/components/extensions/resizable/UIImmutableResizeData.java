package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.resizable;

import com.google.common.collect.Sets;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserResizableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUIAxis;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.IIntersection;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableIntersection;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.TupleUtilities;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.Set;

@Immutable
public final class UIImmutableResizeData
		implements IUIComponentUserResizableExtension.IResizeData {
	private final IIntersection<WeakReference<? extends IUIComponent>, WeakReference<? extends IUIReshapeExplicitly<?>>> targetComponent;
	private final Point2D previousPoint;
	private final Set<EnumUISide> sides;
	@Nullable
	private final Point2D base;
	private final long initialCursorHandle;

	private <TC extends IUIComponent & IUIReshapeExplicitly<?>> UIImmutableResizeData(TC targetComponent,
	                                                                                  Point2D previousPoint,
	                                                                                  Set<EnumUISide> sides,
	                                                                                  @Nullable Point2D base,
	                                                                                  long initialCursorHandle) {
		WeakReference<TC> targetComponentReference = new WeakReference<>(targetComponent);
		this.targetComponent = ImmutableIntersection.of(targetComponentReference, targetComponentReference);
		this.previousPoint = (Point2D) previousPoint.clone();
		this.sides = Sets.immutableEnumSet(sides);
		this.base = Optional.ofNullable(base).map(Point2D::clone).map(CastUtilities::<Point2D>castUnchecked).orElse(null);
		this.initialCursorHandle = initialCursorHandle;
	}

	public static <TC extends IUIComponent & IUIReshapeExplicitly<?>> UIImmutableResizeData of(TC targetComponent,
	                                                                                           Point2D previousPoint,
	                                                                                           Set<EnumUISide> sides,
	                                                                                           @Nullable Point2D base,
	                                                                                           long initialCursorHandle) {
		return new UIImmutableResizeData(targetComponent, previousPoint, sides, base, initialCursorHandle);
	}

	@Override
	public Optional<? extends IIntersection<? extends IUIComponent, ? extends IUIReshapeExplicitly<?>>> getTargetComponent() {
		return TupleUtilities.flattenWeakIntersection(targetComponent);
	}

	@Override
	public Point2D getPreviousPointView() { return (Point2D) getPreviousPoint().clone(); }

	@Override
	public Set<? extends EnumUISide> getSidesView() { return Sets.immutableEnumSet(getSides()); }

	@Override
	public Optional<? extends Point2D> getBaseView() { return getBase().map(Point2D::clone).map(CastUtilities::castUnchecked); }

	@Override
	public long getInitialCursorHandle() { return initialCursorHandle; }

	protected Optional<Point2D> getBase() { return Optional.ofNullable(base); }

	protected Set<EnumUISide> getSides() { return sides; }

	protected Point2D getPreviousPoint() { return previousPoint; }

	@Override
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	public Optional<? extends Shape> handle(Point2D point) {
		return getTargetComponent()
				.map(ITuple2::getLeft)
				.map(IUIComponent::getShape)
				.map(shape -> {
					Rectangle2D result = shape.getBounds2D();
					Point2D previousPoint = getPreviousPoint();
					getSides().forEach(side -> {
						EnumUIAxis axis = side.getAxis();
						side.setValue(result,
								side.getValue(result)
										+ (axis.getCoordinate(point) - axis.getCoordinate(previousPoint)));
					});
					return result;
				});
	}
}
