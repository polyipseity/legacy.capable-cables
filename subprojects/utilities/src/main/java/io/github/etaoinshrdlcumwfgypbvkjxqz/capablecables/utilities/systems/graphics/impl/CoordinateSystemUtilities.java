package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.core.ICoordinateSystem;

import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

public class CoordinateSystemUtilities {
	;

	public static <R extends RectangularShape> R convertRectangularShape(RectangularShape source, R destination, ICoordinateSystem from, ICoordinateSystem to) {
		Point2D[] points = UIObjectUtilities.getDiagonalsFromRectangular(source);
		destination.setFrameFromDiagonal(convertPoint(points[0], points[0], from, to), convertPoint(points[1], points[1], from, to));
		return destination;
	}

	public static <R extends Point2D> R convertPoint(Point2D source, R destination, ICoordinateSystem from, ICoordinateSystem to) {
		return to.specialize(from.generalize(source, destination), destination);
	}
}
