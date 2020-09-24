package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AffineTransformUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutablePoint2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableRectangle2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Vector4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Marker;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.nio.FloatBuffer;
import java.util.ResourceBundle;

@OnlyIn(Dist.CLIENT)
public enum MinecraftMatrixUtilities {
	;

	private static final Marker CLASS_MARKER = UtilitiesMarkers.getInstance().getClassMarker();
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());

	public static void transformFromTo(MatrixStack matrix, Rectangle2D from, Rectangle2D to) {
		if (from.getWidth() == 0 || from.getHeight() == 0)
			throw new IllegalArgumentException(
					new LogMessageBuilder()
							.addMarkers(MinecraftMatrixUtilities::getClassMarker)
							.addKeyValue("matrix", matrix).addKeyValue("from", from).addKeyValue("to", to)
							.addMessages(() -> getResourceBundle().getString("transform.from_to.un_computable"))
							.build()
			);
		double scaleX = to.getWidth() / from.getWidth(),
				scaleY = to.getHeight() / from.getHeight();
		matrix.translate(from.getX() * scaleX - to.getX(), from.getY() * scaleY - to.getY(), 0);
		matrix.scale((float) scaleX, (float) scaleY, 1);
	}

	public static Marker getClassMarker() { return CLASS_MARKER; }

	public static final int MATRIX_ARRAY_LENGTH = 16;
	private static final Matrix4f IDENTITY = new Matrix4f();

	static {
		IDENTITY.setIdentity();
	}

	public static Matrix4f getIdentity() { return IDENTITY.copy(); }

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	public static double transformX(double x, Matrix4f matrix) { return transformVector4f(new Vector4f((float) x, 0, 0, 1), matrix).getX(); }

	public static Vector4f transformVector4f(Vector4f vec, Matrix4f matrix) {
		Vector4f ret = copyVector4f(vec);
		vec.transform(matrix);
		return ret;
	}

	public static Vector4f copyVector4f(Vector4f vector) {
		return new Vector4f(vector.getX(), vector.getY(), vector.getZ(), vector.getW());
	}

	public static double transformY(double y, Matrix4f matrix) { return transformVector4f(new Vector4f(0, (float) y, 0, 1), matrix).getY(); }

	public static double transformZ(double z, Matrix4f matrix) { return transformVector4f(new Vector4f(0, 0, (float) z, 1), matrix).getZ(); }

	public static double transformW(double w, Matrix4f matrix) { return transformVector4f(new Vector4f(0, 0, 0, (float) w), matrix).getW(); }

	public static ImmutableRectangle2D transformRectangle(Rectangle2D rectangle, Matrix4f matrix) {
		return ImmutableRectangle2D.fromDiagonal(transformPoint(ImmutablePoint2D.of(rectangle.getX(), rectangle.getY()), matrix),
				transformPoint(ImmutablePoint2D.of(rectangle.getMaxX(), rectangle.getMaxY()), matrix));
	}

	public static ImmutablePoint2D transformPoint(Point2D point, Matrix4f matrix) {
		Vector4f vec = transformVector4f(new Vector4f((float) point.getX(), (float) point.getY(), 0, 1), matrix);
		return ImmutablePoint2D.of(vec.getX(), vec.getY());
	}

	public static AffineTransform toAffineTransform(Matrix4f matrix) {
		// COMMENT not all matrix data are converted
		FloatBuffer buf = FloatBuffer.allocate(MATRIX_ARRAY_LENGTH);
		matrix.write(buf);
		return new AffineTransform(
				buf.get(AffineTransformUtilities.toMatrix4x4ArrayIndex(0, 0)), buf.get(AffineTransformUtilities.toMatrix4x4ArrayIndex(1, 0)),
				buf.get(AffineTransformUtilities.toMatrix4x4ArrayIndex(0, 1)), buf.get(AffineTransformUtilities.toMatrix4x4ArrayIndex(1, 1)),
				buf.get(AffineTransformUtilities.toMatrix4x4ArrayIndex(0, 3)), buf.get(AffineTransformUtilities.toMatrix4x4ArrayIndex(1, 3))
		);
	}

}
