package $group__.client.gui.utilities;

import $group__.utilities.helpers.specific.ThrowableUtilities.BecauseOf;
import com.google.common.util.concurrent.AtomicDouble;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Vector4f;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.nio.FloatBuffer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public enum Transforms {
	;

	@OnlyIn(CLIENT)
	public enum Matrices {
		;

		public static final int MATRIX_ARRAY_LENGTH = 16;
		private static final Matrix4f IDENTITY = new Matrix4f();

		static {
			IDENTITY.setIdentity();
		}

		public static Matrix4f getIdentity() { return IDENTITY.copy(); }

		public static void transformFromTo(MatrixStack matrix, Rectangle2D from, Rectangle2D to) {
			if (from.getWidth() == 0 || from.getHeight() == 0)
				throw BecauseOf.illegalArgument("from", from, "to", to);
			double scaleX = to.getWidth() / from.getWidth(),
					scaleY = to.getHeight() / from.getHeight();
			matrix.translate(from.getX() * scaleX - to.getX(), from.getY() * scaleY - to.getY(), 0);
			matrix.scale((float) scaleX, (float) scaleY, 1);
		}

		public static double transformX(double x, Matrix4f matrix) {
			AtomicDouble d = new AtomicDouble();
			transform(() -> new Vector4f((float) x, 0, 0, 1),
					vec -> d.set(vec.getX()), matrix);
			return d.get();
		}

		public static double transformY(double y, Matrix4f matrix) {
			AtomicDouble d = new AtomicDouble();
			transform(() -> new Vector4f(0, (float) y, 0, 1),
					vec -> d.set(vec.getY()), matrix);
			return d.get();
		}

		public static double transformZ(double z, Matrix4f matrix) {
			AtomicDouble d = new AtomicDouble();
			transform(() -> new Vector4f(0, 0, (float) z, 1),
					vec -> d.set(vec.getZ()), matrix);
			return d.get();
		}

		public static double transformW(double w, Matrix4f matrix) {
			AtomicDouble d = new AtomicDouble();
			transform(() -> new Vector4f(0, 0, 0, (float) w),
					vec -> d.set(vec.getW()), matrix);
			return d.get();
		}

		public static void transformPoint(Point2D point, Matrix4f matrix) {
			transform(() -> new Vector4f((float) point.getX(), (float) point.getY(), 0, 1),
					vec -> point.setLocation(vec.getX(), vec.getY()), matrix);
		}

		public static void transformRectangle(Rectangle2D rectangle, Matrix4f matrix) {
			Point2D p = new Point2D.Double();
			transform(() -> new Vector4f((float) rectangle.getX(), (float) rectangle.getY(), 0, 1),
					vec -> p.setLocation(vec.getX(), vec.getY()), matrix);
			transform(() -> new Vector4f((float) rectangle.getMaxX(), (float) rectangle.getMaxY(), 0, 1),
					vec -> rectangle.setFrameFromDiagonal(p.getX(), p.getY(), vec.getX(), vec.getY()), matrix);
		}

		public static <T extends Vector4f> void transform(Supplier<? extends T> supplier, Consumer<? super T> consumer, Matrix4f matrix) {
			T vec = supplier.get();
			vec.transform(matrix);
			consumer.accept(vec);
		}

		public static AffineTransform toAffineTransform(Matrix4f matrix) {
			// COMMENT not all matrix data are converted
			FloatBuffer buffer = FloatBuffer.allocate(MATRIX_ARRAY_LENGTH);
			matrix.write(buffer);
			return new AffineTransform(
					buffer.get(toArrayIndex(0, 0)), buffer.get(toArrayIndex(1, 0)),
					buffer.get(toArrayIndex(0, 1)), buffer.get(toArrayIndex(1, 1)),
					buffer.get(toArrayIndex(0, 3)), buffer.get(toArrayIndex(1, 3))
			);
		}

		public static int toArrayIndex(int row, int column) { return row * 4 + column; }
	}

	@OnlyIn(CLIENT)
	public enum AffineTransforms {
		;

		private static final AffineTransform IDENTITY = new AffineTransform();

		public static AffineTransform getIdentity() { return (AffineTransform) IDENTITY.clone(); }

		public static AffineTransform getTransformFromTo(Rectangle2D from, Rectangle2D to) {
			if (from.getWidth() == 0 || from.getHeight() == 0)
				throw BecauseOf.illegalArgument("from", from, "to", to);
			double scaleX = to.getWidth() / from.getWidth(),
					scaleY = to.getHeight() / from.getHeight();
			AffineTransform transform = AffineTransform.getTranslateInstance(to.getX() - from.getX() * scaleX, to.getY() - from.getY() * scaleY);
			transform.scale(scaleX, scaleY);
			return transform;
		}

		public static double transformX(double x, AffineTransform transform) {
			AtomicDouble d = new AtomicDouble();
			transform(() -> new Point2D.Double(x, 0),
					p -> d.set(p.getX()), transform);
			return d.get();
		}

		public static double transformY(double y, AffineTransform transform) {
			AtomicDouble d = new AtomicDouble();
			transform(() -> new Point2D.Double(0, y),
					p -> d.set(p.getY()), transform);
			return d.get();
		}

		public static void transformPoint(Point2D point, AffineTransform transform) {
			transform(() -> point,
					p -> {}, transform);
		}

		public static void transformRectangle(Rectangle2D rectangle, AffineTransform transform) {
			Point2D p = new Point2D.Double(rectangle.getX(), rectangle.getY());
			transform(() -> p,
					p2 -> {}, transform);
			transform(() -> new Point2D.Double(rectangle.getMaxX(), rectangle.getMaxY()),
					p2 -> rectangle.setFrameFromDiagonal(p.getX(), p.getY(), p2.getX(), p2.getY()), transform);
		}

		public static <T extends Point2D> void transform(Supplier<? extends T> supplier, Consumer<? super T> consumer, AffineTransform transform) {
			T p = supplier.get();
			transform.transform(p, p);
			consumer.accept(p);
		}

		public static Matrix4f toMatrix(AffineTransform transform) {
			double[] matD3x2 = new double[6];
			transform.getMatrix(matD3x2);
			return new Matrix4f(new float[]{
					(float) matD3x2[0], (float) matD3x2[2], 0, (float) matD3x2[4],
					(float) matD3x2[1], (float) matD3x2[3], 0, (float) matD3x2[5],
					0, 0, 1, 0,
					0, 0, 0, 1,
			});
		}
	}
}
