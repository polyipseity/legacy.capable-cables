package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.shapes.interactions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.MathUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.ConstantSupplier;
import jdk.nashorn.internal.ir.annotations.Immutable;

import javax.annotation.Nullable;
import java.util.Optional;

@Immutable
public final class ShapeConstraint
		extends ShapeConstraintSupplier {
	public ShapeConstraint(@Nullable Double minX, @Nullable Double minY, @Nullable Double maxX, @Nullable Double maxY, @Nullable Double minWidth, @Nullable Double minHeight, @Nullable Double maxWidth, @Nullable Double maxHeight) {
		super(
				ConstantSupplier.ofNullable(Optional.ofNullable(minX).map(x -> MathUtilities.minNullable(x, maxX)).orElse(null)),
				ConstantSupplier.ofNullable(Optional.ofNullable(minY).map(y -> MathUtilities.minNullable(y, maxY)).orElse(null)),
				ConstantSupplier.ofNullable(Optional.ofNullable(maxX).map(x -> MathUtilities.maxNullable(minX, x)).orElse(null)),
				ConstantSupplier.ofNullable(Optional.ofNullable(maxY).map(y -> MathUtilities.maxNullable(minY, y)).orElse(null)),
				ConstantSupplier.ofNullable(Optional.ofNullable(minWidth).map(width -> MathUtilities.minNullable(width, maxWidth)).orElse(null)),
				ConstantSupplier.ofNullable(Optional.ofNullable(minHeight).map(height -> MathUtilities.minNullable(height, maxHeight)).orElse(null)),
				ConstantSupplier.ofNullable(Optional.ofNullable(maxWidth).map(width -> MathUtilities.maxNullable(minWidth, width)).orElse(null)),
				ConstantSupplier.ofNullable(Optional.ofNullable(maxHeight).map(height -> MathUtilities.maxNullable(minHeight, height)).orElse(null)));
	}

	@Override
	public ShapeConstraint clone() throws CloneNotSupportedException { return (ShapeConstraint) super.clone(); }

	@Override
	public ShapeConstraint copy() {
		return new ShapeConstraint(
				getMinX().orElse(null),
				getMinY().orElse(null),
				getMaxX().orElse(null),
				getMaxY().orElse(null),
				getMinWidth().orElse(null),
				getMinHeight().orElse(null),
				getMaxWidth().orElse(null),
				getMaxHeight().orElse(null));
	}
}
