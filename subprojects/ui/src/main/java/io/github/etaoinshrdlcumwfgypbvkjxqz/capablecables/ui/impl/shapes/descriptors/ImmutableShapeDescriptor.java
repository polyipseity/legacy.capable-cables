package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICloneable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl.UIObjectUtilities;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.function.Function;

public final class ImmutableShapeDescriptor<S extends Shape>
		extends AbstractShapeDescriptor<S> {
	private final S shape;
	private final Function<? super S, ? extends S> copier;

	private ImmutableShapeDescriptor(S shape, Function<? super S, ? extends S> copier) {
		this.copier = copier;
		this.shape = this.copier.apply(shape);
	}

	public static ImmutableShapeDescriptor<Shape> ofGeneric(Shape shape) {
		return of(shape, UIObjectUtilities::copyShape);
	}

	public static <S extends Shape> ImmutableShapeDescriptor<S> of(S shape, Function<? super S, ? extends S> copier) {
		return new ImmutableShapeDescriptor<>(shape, copier);
	}

	public static <R extends Shape & Cloneable> ImmutableShapeDescriptor<R> ofCloneable(R shape) {
		return of(shape, ICloneable::clone);
	}

	@Override
	public boolean isDynamic() {
		return false;
	}

	@Override
	public S getShapeOutput() {
		return getCopier().apply(getShape());
	}

	protected Function<? super S, ? extends S> getCopier() {
		return copier;
	}

	protected S getShape() {
		return shape;
	}

	@Override
	protected boolean crop0(Rectangle2D bounds) {
		// COMMENT NOOP
		return false;
	}

	@Override
	protected boolean transform0(AffineTransform transform) {
		// COMMENT NOOP
		return false;
	}

	@Override
	protected boolean adapt0(Rectangle2D frame) {
		// COMMENT NOOP
		return false;
	}
}
