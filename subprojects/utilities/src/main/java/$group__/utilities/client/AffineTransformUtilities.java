package $group__.utilities.client;

import $group__.utilities.specific.ThrowableUtilities;
import net.minecraft.client.renderer.Matrix4f;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public enum AffineTransformUtilities {
	;

	private static final AffineTransform IDENTITY = new AffineTransform();
	private static final double[]
			TO_MATRIX_FLAT_MATRIX = new double[6];
	private static final float[] TO_MATRIX_MATRIX_4F = {
			-1, -1, 0, -1,
			-1, -1, 0, -1,
			0, 0, 1, 0,
			0, 0, 0, 1,
	};

	public static AffineTransform getIdentity() { return (AffineTransform) IDENTITY.clone(); }

	public static AffineTransform getTransformFromTo(Rectangle2D from, Rectangle2D to) {
		if (from.getWidth() == 0 || from.getHeight() == 0)
			throw ThrowableUtilities.BecauseOf.illegalArgument("Cannot calculate transform", "from", from, "to", to);
		double scaleX = to.getWidth() / from.getWidth(),
				scaleY = to.getHeight() / from.getHeight();
		AffineTransform transform = AffineTransform.getTranslateInstance(to.getX() - from.getX() * scaleX, to.getY() - from.getY() * scaleY);
		transform.scale(scaleX, scaleY);
		return transform;
	}

	public static double transformX(double x, AffineTransform transform) { return transformReturns(new Point2D.Double(x, 0), transform).getX(); }

	public static <T extends Point2D> T transformReturns(T point, AffineTransform transform) {
		transform.transform(point, point);
		return point;
	}

	public static double transformY(double y, AffineTransform transform) { return transformReturns(new Point2D.Double(0, y), transform).getY(); }

	public static void transformRectangle(Rectangle2D rectangle, AffineTransform transform) {
		Point2D min = new Point2D.Double(rectangle.getX(), rectangle.getY()), max = new Point2D.Double(rectangle.getMaxX(), rectangle.getMaxY());
		transformPoint(min, transform);
		transformPoint(max, transform);
		rectangle.setFrameFromDiagonal(min, max);
	}

	public static void transformPoint(Point2D point, AffineTransform transform) { transformReturns(point, transform); }

	public static Matrix4f toMatrix(AffineTransform transform) {
		transform.getMatrix(TO_MATRIX_FLAT_MATRIX);
		TO_MATRIX_MATRIX_4F[toArrayIndex(0, 0)] = (float) TO_MATRIX_FLAT_MATRIX[0];
		TO_MATRIX_MATRIX_4F[toArrayIndex(1, 0)] = (float) TO_MATRIX_FLAT_MATRIX[1];
		TO_MATRIX_MATRIX_4F[toArrayIndex(0, 1)] = (float) TO_MATRIX_FLAT_MATRIX[2];
		TO_MATRIX_MATRIX_4F[toArrayIndex(1, 1)] = (float) TO_MATRIX_FLAT_MATRIX[3];
		TO_MATRIX_MATRIX_4F[toArrayIndex(0, 3)] = (float) TO_MATRIX_FLAT_MATRIX[4];
		TO_MATRIX_MATRIX_4F[toArrayIndex(1, 3)] = (float) TO_MATRIX_FLAT_MATRIX[5];
		return new Matrix4f(TO_MATRIX_MATRIX_4F);
	}

	public static int toArrayIndex(int row, int column) { return (row << 2) + column; }
}
