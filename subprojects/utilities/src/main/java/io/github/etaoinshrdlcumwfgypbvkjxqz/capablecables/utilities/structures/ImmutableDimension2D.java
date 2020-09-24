package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ui.UIObjectUtilities;

import javax.annotation.concurrent.Immutable;
import java.awt.geom.Dimension2D;

@Immutable
public final class ImmutableDimension2D
		extends Dimension2D {
	private static final ImmutableDimension2D DEFAULT = new ImmutableDimension2D(0, 0);
	private final double width;
	private final double height;

	private ImmutableDimension2D(double width, double height) {
		this.width = width;
		this.height = height;
	}

	public static ImmutableDimension2D of() { return DEFAULT; }

	public static ImmutableDimension2D of(Dimension2D dimension) {
		if (dimension instanceof ImmutableDimension2D)
			return (ImmutableDimension2D) dimension;
		return UIObjectUtilities.applyDimension(dimension, ImmutableDimension2D::of);
	}

	public static ImmutableDimension2D of(double width, double height) { return new ImmutableDimension2D(width, height); }

	@Override
	public double getWidth() { return width; }

	@Override
	public double getHeight() { return height; }

	@Override
	@Deprecated
	public void setSize(double width, double height)
			throws UnsupportedOperationException { throw new UnsupportedOperationException(); }

	@Override
	public ImmutableDimension2D clone() { return (ImmutableDimension2D) super.clone(); }
}
