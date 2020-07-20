package $group__.client.gui.utilities;

import com.google.common.util.concurrent.AtomicDouble;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.Vector4f;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public enum Matrices {
	;

	private static final Matrix4f IDENTITY = new Matrix4f();

	static {
		IDENTITY.setIdentity();
	}

	public static Matrix4f getIdentity() { return IDENTITY.copy(); }

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

	public static void transformDimension(Dimension2D dimension, Matrix4f matrix) {
		transform(() -> new Vector4f((float) dimension.getWidth(), (float) dimension.getHeight(), 0, 1),
				vec -> dimension.setSize(vec.getX(), vec.getY()), matrix);
	}

	public static void transformVector3f(Vector3f vector3, Matrix4f matrix) {
		transform(() -> new Vector4f(vector3.getX(), vector3.getY(), vector3.getZ(), 1),
				vec -> vector3.set(vec.getX(), vec.getY(), vec.getZ()), matrix);
	}

	public static <T extends Vector4f> void transform(Supplier<? extends T> supplier, Consumer<? super T> consumer, Matrix4f matrix) {
		T vec = supplier.get();
		vec.transform(matrix);
		consumer.accept(vec);
	}
}
