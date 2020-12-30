package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.shapes.interactions.IShapeConstraint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.MathUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl.OptionalUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.util.OptionalDouble;
import java.util.function.Supplier;

public class ShapeConstraintSupplier
		implements IShapeConstraint {
	private final Supplier<@Nullable ? extends Double> minXSupplier;
	private final Supplier<@Nullable ? extends Double> minYSupplier;
	private final Supplier<@Nullable ? extends Double> maxXSupplier;
	private final Supplier<@Nullable ? extends Double> maxYSupplier;
	private final Supplier<@Nullable ? extends Double> minWidthSupplier;
	private final Supplier<@Nullable ? extends Double> minHeightSupplier;
	private final Supplier<@Nullable ? extends Double> maxWidthSupplier;
	private final Supplier<@Nullable ? extends Double> maxHeightSupplier;

	public ShapeConstraintSupplier(Supplier<@Nullable ? extends Double> minXSupplier,
	                               Supplier<@Nullable ? extends Double> minYSupplier,
	                               Supplier<@Nullable ? extends Double> maxXSupplier,
	                               Supplier<@Nullable ? extends Double> maxYSupplier,
	                               Supplier<@Nullable ? extends Double> minWidthSupplier,
	                               Supplier<@Nullable ? extends Double> minHeightSupplier,
	                               Supplier<@Nullable ? extends Double> maxWidthSupplier,
	                               Supplier<@Nullable ? extends Double> maxHeightSupplier) {
		this.minXSupplier = minXSupplier;
		this.minYSupplier = minYSupplier;
		this.maxXSupplier = maxXSupplier;
		this.maxYSupplier = maxYSupplier;
		this.minWidthSupplier = minWidthSupplier;
		this.minHeightSupplier = minHeightSupplier;
		this.maxWidthSupplier = maxWidthSupplier;
		this.maxHeightSupplier = maxHeightSupplier;
	}

	@Override
	public ShapeConstraintSupplier clone() {
		try {
			// COMMENT makes no sense to clone suppliers - the behavior is the thing to be cloned, which can be achieved by simply not cloning them
			return (ShapeConstraintSupplier) super.clone();
		} catch (CloneNotSupportedException e) {
			throw ThrowableUtilities.propagate(e);
		}
	}

	@Override
	public IShapeConstraint createIntersection(IShapeConstraint constraint) {
		return new ShapeConstraintSupplier(
				() -> MathUtilities.maxNullable(OptionalUtilities.valueOf(getMinX()), OptionalUtilities.valueOf(constraint.getMinX())),
				() -> MathUtilities.minNullable(OptionalUtilities.valueOf(getMinY()), OptionalUtilities.valueOf(constraint.getMinY())),
				() -> MathUtilities.maxNullable(OptionalUtilities.valueOf(getMaxX()), OptionalUtilities.valueOf(constraint.getMaxX())),
				() -> MathUtilities.minNullable(OptionalUtilities.valueOf(getMaxY()), OptionalUtilities.valueOf(constraint.getMaxY())),
				() -> MathUtilities.maxNullable(OptionalUtilities.valueOf(getMinWidth()), OptionalUtilities.valueOf(constraint.getMinWidth())),
				() -> MathUtilities.minNullable(OptionalUtilities.valueOf(getMinHeight()), OptionalUtilities.valueOf(constraint.getMinHeight())),
				() -> MathUtilities.maxNullable(OptionalUtilities.valueOf(getMaxWidth()), OptionalUtilities.valueOf(constraint.getMaxWidth())),
				() -> MathUtilities.minNullable(OptionalUtilities.valueOf(getMaxHeight()), OptionalUtilities.valueOf(constraint.getMaxHeight()))
		);
	}

	@Override
	public OptionalDouble getMinX() { return OptionalUtilities.ofDouble(getMinXSupplier().get()); }

	@Override
	public OptionalDouble getMinY() { return OptionalUtilities.ofDouble(getMinYSupplier().get()); }

	@Override
	public OptionalDouble getMaxX() { return OptionalUtilities.ofDouble(getMaxXSupplier().get()); }

	@Override
	public OptionalDouble getMaxY() { return OptionalUtilities.ofDouble(getMaxYSupplier().get()); }

	@Override
	public OptionalDouble getMinWidth() { return OptionalUtilities.ofDouble(getMinWidthSupplier().get()); }

	@Override
	public OptionalDouble getMinHeight() { return OptionalUtilities.ofDouble(getMinHeightSupplier().get()); }

	@Override
	public OptionalDouble getMaxWidth() { return OptionalUtilities.ofDouble(getMaxWidthSupplier().get()); }

	@Override
	public OptionalDouble getMaxHeight() { return OptionalUtilities.ofDouble(getMaxHeightSupplier().get()); }

	protected Supplier<@Nullable ? extends Double> getMinXSupplier() { return minXSupplier; }

	protected Supplier<@Nullable ? extends Double> getMinYSupplier() { return minYSupplier; }

	protected Supplier<@Nullable ? extends Double> getMaxXSupplier() { return maxXSupplier; }

	protected Supplier<@Nullable ? extends Double> getMaxYSupplier() { return maxYSupplier; }

	protected Supplier<@Nullable ? extends Double> getMinWidthSupplier() { return minWidthSupplier; }

	protected Supplier<@Nullable ? extends Double> getMinHeightSupplier() { return minHeightSupplier; }

	protected Supplier<@Nullable ? extends Double> getMaxWidthSupplier() { return maxWidthSupplier; }

	protected Supplier<@Nullable ? extends Double> getMaxHeightSupplier() { return maxHeightSupplier; }
}
