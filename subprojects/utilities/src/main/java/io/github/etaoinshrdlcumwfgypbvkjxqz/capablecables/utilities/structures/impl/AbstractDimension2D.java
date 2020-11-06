package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import org.jetbrains.annotations.NonNls;

import java.awt.geom.Dimension2D;
import java.io.Serializable;
import java.util.Map;
import java.util.function.Function;

public abstract class AbstractDimension2D
		extends Dimension2D
		implements Serializable {
	private static final long serialVersionUID = -6888542933393503233L;
	@NonNls
	private static final @Immutable Map<String, Function<@Nonnull Dimension2D, @Nullable ?>> OBJECT_VARIABLE_MAP =
			ImmutableMap.<String, Function<@Nonnull Dimension2D, @Nullable ?>>builder()
					.put("width", Dimension2D::getWidth)
					.put("height", Dimension2D::getHeight)
					.build();

	@Override
	public AbstractDimension2D clone() {
		return (AbstractDimension2D) super.clone();
	}

	@Override
	public int hashCode() {
		return ObjectUtilities.hashCodeImpl(this, getObjectVariableMap().values());
	}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(@Nullable Object obj) {
		return ObjectUtilities.equalsImpl(this, obj, Dimension2D.class, true, getObjectVariableMap().values());
	}

	@Override
	public String toString() {
		return ObjectUtilities.toStringImpl(this, getObjectVariableMap());
	}

	public static @Immutable Map<String, Function<@Nonnull Dimension2D, @Nullable ?>> getObjectVariableMap() {
		return OBJECT_VARIABLE_MAP;
	}
}
