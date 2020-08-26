package $group__.utilities.client;

import $group__.utilities.ThrowableUtilities;
import net.minecraft.client.renderer.Matrix4f;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public enum AffineTransformUtilities {
	;

	private static final AffineTransform IDENTITY = new AffineTransform();
	public static final int SCALE_X_INDEX = toFlatMatrixArrayIndex(0, 0);
	public static final int SHEAR_X_INDEX = toFlatMatrixArrayIndex(0, 1);
	public static final int SHEAR_Y_INDEX = toFlatMatrixArrayIndex(1, 0);
	public static final int SCALE_Y_INDEX = toFlatMatrixArrayIndex(1, 1);
	public static final int TRANSLATE_X_INDEX = toFlatMatrixArrayIndex(0, 2);

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

	public static final int TRANSLATE_Y_INDEX = toFlatMatrixArrayIndex(1, 2);
	private static final double[] FLAT_MATRIX_IDENTITY = {
			// COMMENT column-major
			1, 0,
			0, 1,
			0, 0,
	};
	private static final float[] MATRIX_4X4_IDENTITY = {
			1, 0, 0, 0,
			0, 1, 0, 0,
			0, 0, 1, 0,
			0, 0, 0, 1,
	};

	public static double[] getFlatMatrixIdentityCopy() { return FLAT_MATRIX_IDENTITY.clone(); }

	public static AffineTransform getIdentityCopy() { return (AffineTransform) IDENTITY.clone(); }

	public static Matrix4f toMatrix(AffineTransform transform) {
		final double[] fm = new double[6];
		transform.getMatrix(fm);
		final float[] m4 = getMatrix4x4IdentityCopy();
		m4[toMatrix4x4ArrayIndex(0, 0)] = (float) fm[SCALE_X_INDEX];
		m4[toMatrix4x4ArrayIndex(1, 0)] = (float) fm[SHEAR_X_INDEX];
		m4[toMatrix4x4ArrayIndex(0, 1)] = (float) fm[SHEAR_Y_INDEX];
		m4[toMatrix4x4ArrayIndex(1, 1)] = (float) fm[SCALE_Y_INDEX];
		m4[toMatrix4x4ArrayIndex(0, 3)] = (float) fm[TRANSLATE_X_INDEX];
		m4[toMatrix4x4ArrayIndex(1, 3)] = (float) fm[TRANSLATE_Y_INDEX];
		return new Matrix4f(m4);
	}

	public static float[] getMatrix4x4IdentityCopy() { return MATRIX_4X4_IDENTITY.clone(); }

	public static int toMatrix4x4ArrayIndex(int row, int column) { return (row << 2) + column; }

	public static int toFlatMatrixArrayIndex(int row, int column) { return (column << 1) + row; }

	public static double getM00(AffineTransform transform) { return transform.getScaleX(); }

	public static double getM01(AffineTransform transform) { return transform.getShearX(); }

	public static double getM10(AffineTransform transform) { return transform.getShearY(); }

	public static double getM11(AffineTransform transform) { return transform.getScaleY(); }

	public static double getM02(AffineTransform transform) { return transform.getTranslateX(); }

	public static double getM12(AffineTransform transform) { return transform.getTranslateY(); }

	public static void assertProperFlatMatrix(double[] matrix) {
		assertFullFlatMatrix(matrix);
		assertPartialFlatMatrix(matrix);
	}

	public static void assertFullFlatMatrix(double[] matrix) { assert matrix.length == 6; }

	public static void assertPartialFlatMatrix(double[] matrix) { assert matrix.length == 4; }
}
