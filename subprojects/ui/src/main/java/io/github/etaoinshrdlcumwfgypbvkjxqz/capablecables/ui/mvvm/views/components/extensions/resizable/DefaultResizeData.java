package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions.resizable;

import com.google.common.collect.Sets;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserResizableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.utilities.EnumUIAxis;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.util.Optional;
import java.util.Set;

public class DefaultResizeData implements IUIComponentUserResizableExtension.IResizeData {
	protected final Point2D previousPoint;
	protected final Set<EnumUISide> sides;
	@Nullable
	protected final Point2D base;
	protected final long initialCursorHandle;

	public DefaultResizeData(Point2D previousPoint, Set<EnumUISide> sides, @Nullable Point2D base, long initialCursorHandle) {
		this.previousPoint = (Point2D) previousPoint.clone();
		this.sides = Sets.immutableEnumSet(sides);
		this.base = Optional.ofNullable(base).map(Point2D::clone).map(CastUtilities::<Point2D>castUnchecked).orElse(null);
		this.initialCursorHandle = initialCursorHandle;
	}

	@Override
	public Point2D getPreviousPointView() { return (Point2D) getPreviousPoint().clone(); }

	@Override
	public Set<? extends EnumUISide> getSidesView() { return Sets.immutableEnumSet(getSides()); }

	@Override
	public Optional<? extends Point2D> getBaseView() { return getBase().map(Point2D::clone).map(CastUtilities::castUnchecked); }

	@Override
	public long getInitialCursorHandle() { return initialCursorHandle; }

	@Override
	public <R extends RectangularShape> R handle(Point2D point, RectangularShape source, R destination) {
		Point2D previousPoint = getPreviousPoint();
		getSides().forEach(side -> {
			EnumUIAxis axis = side.getAxis();
			side.getSetter().accept(destination, AssertionUtilities.assertNonnull(side.getGetter().applyAsDouble(source))
					+ (axis.getCoordinate(point) - axis.getCoordinate(previousPoint)));
		});
		return destination;
	}

	protected Optional<Point2D> getBase() { return Optional.ofNullable(base); }

	protected Set<EnumUISide> getSides() { return sides; }

	protected Point2D getPreviousPoint() { return previousPoint; }
}
