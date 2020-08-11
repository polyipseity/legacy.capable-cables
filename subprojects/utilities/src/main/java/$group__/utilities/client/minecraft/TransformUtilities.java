package $group__.utilities.client.minecraft;

import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Vector4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.nio.FloatBuffer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public enum TransformUtilities {
	;

	public static final int MATRIX_ARRAY_LENGTH = 16;

	private static final Vector4f VECTOR = new Vector4f();
	private static final Point2D
			POINT = new Point2D.Double(),
			POINT_2 = new Point2D.Double();

	public static int toArrayIndex(int row, int column) { return (row << 2) + column; }

	@OnlyIn(Dist.CLIENT)
	public enum MatrixUtilities {
		;

		private static final Matrix4f IDENTITY = new Matrix4f();
		private static final FloatBuffer TO_AFFINE_TRANSFORM_BUFFER = FloatBuffer.allocate(MATRIX_ARRAY_LENGTH);

		static {
			IDENTITY.setIdentity();
		}

		public static Matrix4f getIdentity() { return IDENTITY.copy(); }

		public static void transformFromTo(MatrixStack matrix, Rectangle2D from, Rectangle2D to) {
			if (from.getWidth() == 0 || from.getHeight() == 0)
				throw BecauseOf.illegalArgument("Cannot calculate transform", "from", from, "to", to);
			double scaleX = to.getWidth() / from.getWidth(),
					scaleY = to.getHeight() / from.getHeight();
			matrix.translate(from.getX() * scaleX - to.getX(), from.getY() * scaleY - to.getY(), 0);
			matrix.scale((float) scaleX, (float) scaleY, 1);
		}

		public static double transformX(double x, Matrix4f matrix) {
			VECTOR.set((float) x, 0, 0, 1);
			transform(() -> VECTOR, vec -> {}, matrix);
			return VECTOR.getX();
		}

		public static <T extends Vector4f> void transform(Supplier<? extends T> supplier, Consumer<? super T> consumer, Matrix4f matrix) {
			T vec = supplier.get();
			vec.transform(matrix);
			consumer.accept(vec);
		}

		public static double transformY(double y, Matrix4f matrix) {
			VECTOR.set(0, (float) y, 0, 1);
			transform(() -> VECTOR, vec -> {}, matrix);
			return VECTOR.getY();
		}

		public static double transformZ(double z, Matrix4f matrix) {
			VECTOR.set(0, 0, (float) z, 1);
			transform(() -> VECTOR, vec -> {}, matrix);
			return VECTOR.getZ();
		}

		public static double transformW(double w, Matrix4f matrix) {
			VECTOR.set(0, 0, 0, (float) w);
			transform(() -> VECTOR, vec -> {}, matrix);
			return VECTOR.getW();
		}

		public static void transformPoint(Point2D point, Matrix4f matrix) {
			VECTOR.set((float) point.getX(), (float) point.getY(), 0, 1);
			transform(() -> VECTOR, vec -> point.setLocation(vec.getX(), vec.getY()), matrix);
		}

		public static void transformRectangle(Rectangle2D rectangle, Matrix4f matrix) {
			VECTOR.set((float) rectangle.getX(), (float) rectangle.getY(), 0, 1);
			transform(() -> VECTOR, vec -> POINT.setLocation(vec.getX(), vec.getY()), matrix);
			VECTOR.set((float) rectangle.getMaxX(), (float) rectangle.getMaxY(), 0, 1);
			transform(() -> VECTOR, vec -> rectangle.setFrameFromDiagonal(POINT.getX(), POINT.getY(), vec.getX(), vec.getY()), matrix);
		}

		public static AffineTransform toAffineTransform(Matrix4f matrix) {
			// COMMENT not all matrix data are converted
			matrix.write(TO_AFFINE_TRANSFORM_BUFFER);
			return new AffineTransform(
					TO_AFFINE_TRANSFORM_BUFFER.get(toArrayIndex(0, 0)), TO_AFFINE_TRANSFORM_BUFFER.get(toArrayIndex(1, 0)),
					TO_AFFINE_TRANSFORM_BUFFER.get(toArrayIndex(0, 1)), TO_AFFINE_TRANSFORM_BUFFER.get(toArrayIndex(1, 1)),
					TO_AFFINE_TRANSFORM_BUFFER.get(toArrayIndex(0, 3)), TO_AFFINE_TRANSFORM_BUFFER.get(toArrayIndex(1, 3))
			);
		}
	}

	@OnlyIn(Dist.CLIENT)
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
				throw BecauseOf.illegalArgument("Cannot calculate transform", "from", from, "to", to);
			double scaleX = to.getWidth() / from.getWidth(),
					scaleY = to.getHeight() / from.getHeight();
			AffineTransform transform = AffineTransform.getTranslateInstance(to.getX() - from.getX() * scaleX, to.getY() - from.getY() * scaleY);
			transform.scale(scaleX, scaleY);
			return transform;
		}

		public static double transformX(double x, AffineTransform transform) {
			POINT.setLocation(x, 0);
			transform(() -> POINT, p -> {}, transform);
			return POINT.getX();
		}

		public static <T extends Point2D> void transform(Supplier<? extends T> supplier, Consumer<? super T> consumer, AffineTransform transform) {
			T p = supplier.get();
			transform.transform(p, p);
			consumer.accept(p);
		}

		public static double transformY(double y, AffineTransform transform) {
			POINT.setLocation(0, y);
			transform(() -> POINT, p -> {}, transform);
			return POINT.getY();
		}

		public static void transformPoint(Point2D point, AffineTransform transform) { transform(() -> point, p -> {}, transform); }

		public static void transformRectangle(Rectangle2D rectangle, AffineTransform transform) {
			POINT.setLocation(rectangle.getX(), rectangle.getY());
			POINT_2.setLocation(rectangle.getMaxX(), rectangle.getMaxY());
			transform(() -> POINT, p2 -> {}, transform);
			transform(() -> POINT_2, p2 -> rectangle.setFrameFromDiagonal(POINT.getX(), POINT.getY(), p2.getX(), p2.getY()), transform);
		}

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
	}
}
