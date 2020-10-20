package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeConstraint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.MathUtilities;

import java.util.Optional;
import java.util.function.Supplier;

public class ShapeConstraintSupplier
		implements IShapeConstraint {
	private final Supplier<? extends Double> minXSupplier;
	private final Supplier<? extends Double> minYSupplier;
	private final Supplier<? extends Double> maxXSupplier;
	private final Supplier<? extends Double> maxYSupplier;
	private final Supplier<? extends Double> minWidthSupplier;
	private final Supplier<? extends Double> minHeightSupplier;
	private final Supplier<? extends Double> maxWidthSupplier;
	private final Supplier<? extends Double> maxHeightSupplier;

	public ShapeConstraintSupplier(Supplier<? extends Double> minXSupplier, Supplier<? extends Double> minYSupplier, Supplier<? extends Double> maxXSupplier, Supplier<? extends Double> maxYSupplier, Supplier<? extends Double> minWidthSupplier, Supplier<? extends Double> minHeightSupplier, Supplier<? extends Double> maxWidthSupplier, Supplier<? extends Double> maxHeightSupplier) {
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
	public ShapeConstraintSupplier clone() throws CloneNotSupportedException { return (ShapeConstraintSupplier) super.clone(); }

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

	@Override
	public ShapeConstraintSupplier copy() {
		return new ShapeConstraintSupplier(
				getMinXSupplier(),
				getMinYSupplier(),
				getMaxXSupplier(),
				getMaxYSupplier(),
				getMinWidthSupplier(),
				getMinHeightSupplier(),
				getMaxWidthSupplier(),
				getMaxHeightSupplier());
	}

	protected Supplier<? extends Double> getMinXSupplier() { return minXSupplier; }

	protected Supplier<? extends Double> getMinYSupplier() { return minYSupplier; }

	protected Supplier<? extends Double> getMaxXSupplier() { return maxXSupplier; }

	protected Supplier<? extends Double> getMaxYSupplier() { return maxYSupplier; }

	protected Supplier<? extends Double> getMinWidthSupplier() { return minWidthSupplier; }

	protected Supplier<? extends Double> getMinHeightSupplier() { return minHeightSupplier; }

	protected Supplier<? extends Double> getMaxWidthSupplier() { return maxWidthSupplier; }

	protected Supplier<? extends Double> getMaxHeightSupplier() { return maxHeightSupplier; }
}
