package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeConstraint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.MathUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.util.Optional;
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
				() -> MathUtilities.maxNullable(getMinX().orElse(null), constraint.getMinX().orElse(null)),
				() -> MathUtilities.minNullable(getMinY().orElse(null), constraint.getMinY().orElse(null)),
				() -> MathUtilities.maxNullable(getMaxX().orElse(null), constraint.getMaxX().orElse(null)),
				() -> MathUtilities.minNullable(getMaxY().orElse(null), constraint.getMaxY().orElse(null)),
				() -> MathUtilities.maxNullable(getMinWidth().orElse(null), constraint.getMinWidth().orElse(null)),
				() -> MathUtilities.minNullable(getMinHeight().orElse(null), constraint.getMinHeight().orElse(null)),
				() -> MathUtilities.maxNullable(getMaxWidth().orElse(null), constraint.getMaxWidth().orElse(null)),
				() -> MathUtilities.minNullable(getMaxHeight().orElse(null), constraint.getMaxHeight().orElse(null)));
	}

	@Override
	public Optional<? extends Double> getMinX() { return Optional.ofNullable(getMinXSupplier().get()); }

	@Override
	public Optional<? extends Double> getMinY() { return Optional.ofNullable(getMinYSupplier().get()); }

	@Override
	public Optional<? extends Double> getMaxX() { return Optional.ofNullable(getMaxXSupplier().get()); }

	@Override
	public Optional<? extends Double> getMaxY() { return Optional.ofNullable(getMaxYSupplier().get()); }

	@Override
	public Optional<? extends Double> getMinWidth() { return Optional.ofNullable(getMinWidthSupplier().get()); }

	@Override
	public Optional<? extends Double> getMinHeight() { return Optional.ofNullable(getMinHeightSupplier().get()); }

	@Override
	public Optional<? extends Double> getMaxWidth() { return Optional.ofNullable(getMaxWidthSupplier().get()); }

	@Override
	public Optional<? extends Double> getMaxHeight() { return Optional.ofNullable(getMaxHeightSupplier().get()); }

	protected Supplier<@Nullable ? extends Double> getMinXSupplier() { return minXSupplier; }

	protected Supplier<@Nullable ? extends Double> getMinYSupplier() { return minYSupplier; }

	protected Supplier<@Nullable ? extends Double> getMaxXSupplier() { return maxXSupplier; }

	protected Supplier<@Nullable ? extends Double> getMaxYSupplier() { return maxYSupplier; }

	protected Supplier<@Nullable ? extends Double> getMinWidthSupplier() { return minWidthSupplier; }

	protected Supplier<@Nullable ? extends Double> getMinHeightSupplier() { return minHeightSupplier; }

	protected Supplier<@Nullable ? extends Double> getMaxWidthSupplier() { return maxWidthSupplier; }

	protected Supplier<@Nullable ? extends Double> getMaxHeightSupplier() { return maxHeightSupplier; }
}
