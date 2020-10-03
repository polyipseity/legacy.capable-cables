package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions.resizable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserResizableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.EnumUIAxis;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public class DefaultResizeData implements IUIComponentUserResizableExtension.IResizeData {
	protected final Point2D point;
	protected final Set<EnumUISide> sides;
	@Nullable
	protected final Point2D base;
	protected final long initialCursorHandle;

	public DefaultResizeData(Point2D point, Set<EnumUISide> sides, @Nullable Point2D base, long initialCursorHandle) {
		this.point = (Point2D) point.clone();
		this.sides = EnumSet.copyOf(sides);
		this.base = Optional.ofNullable(base).map(Point2D::clone).map(CastUtilities::<Point2D>castUnchecked).orElse(null);
		this.initialCursorHandle = initialCursorHandle;
	}

	@Override
	public Point2D getPointView() { return getPoint(); }

	@Override
	public Set<? extends EnumUISide> getSidesView() { return EnumSet.copyOf(getSides()); }

	@Override
	public Optional<? extends Point2D> getBaseView() { return getBase().map(Point2D::clone).map(CastUtilities::castUnchecked); }

	@Override
	public long getInitialCursorHandle() { return initialCursorHandle; }

	@Override
	public <R extends RectangularShape> R handle(Point2D point, RectangularShape source, R destination) {
		Point2D previousCursorPosition = getPoint();
		for (EnumUISide side : getSides()) {
			EnumUIAxis axis = side.getAxis();
			side.getSetter().accept(destination, AssertionUtilities.assertNonnull(side.getGetter().apply(source))
					+ (axis.getCoordinate(point) - axis.getCoordinate(previousCursorPosition)));
		}
		return destination;
	}

	protected Optional<Point2D> getBase() { return Optional.ofNullable(base); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<EnumUISide> getSides() { return sides; }

	protected Point2D getPoint() { return point; }
}
