package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AffineTransformUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui.MinecraftVectorUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl.UIObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Vector4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Marker;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.nio.FloatBuffer;
import java.util.ResourceBundle;

@OnlyIn(Dist.CLIENT)
public enum MinecraftMatrixUtilities {
	;

	private static final Marker CLASS_MARKER = UtilitiesMarkers.getInstance().getClassMarker();
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());

	public static final int MATRIX_ARRAY_LENGTH = 16;
	private static final Matrix4f IDENTITY = FunctionUtilities.accept(new Matrix4f(), Matrix4f::setIdentity);

	public static Matrix4f getTransformFromTo(RectangularShape from, RectangularShape to) {
		if (from.getWidth() == 0 || from.getHeight() == 0)
			throw new IllegalArgumentException(
					new LogMessageBuilder()
							.addMarkers(MinecraftMatrixUtilities::getClassMarker)
							.addKeyValue("from", from).addKeyValue("to", to)
							.addMessages(() -> getResourceBundle().getString("transform.from_to.un_computable"))
							.build()
			);
		double scaleX = to.getWidth() / from.getWidth(),
				scaleY = to.getHeight() / from.getHeight();
		Matrix4f ret = getIdentity();
		ret.mul(Matrix4f.makeTranslate((float) (to.getX() - from.getX() * scaleX), (float) (to.getY() - from.getY() * scaleY), 0));
		ret.mul(Matrix4f.makeScale((float) scaleX, (float) scaleY, 1));
		return ret;
	}

	public static Marker getClassMarker() { return CLASS_MARKER; }

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	public static <R extends RectangularShape> R transformRectangular(Matrix4f matrix, RectangularShape source, R destination) {
		Point2D[] points = UIObjectUtilities.getDiagonalsFromRectangular(source);
		destination.setFrameFromDiagonal(transformPoint(matrix, points[0], points[0]),
				transformPoint(matrix, points[1], points[1]));
		return destination;
	}

	public static <R extends Point2D> R transformPoint(Matrix4f matrix, Point2D source, R destination) {
		Vector4f vector4f = new Vector4f((float) source.getX(), (float) source.getY(), 0, 1);
		transformVector4f(matrix, vector4f, vector4f);
		destination.setLocation(vector4f.getX(), vector4f.getY());
		return destination;
	}

	@SuppressWarnings("UnusedReturnValue")
	public static <R extends Vector4f> Vector4f transformVector4f(Matrix4f matrix, Vector4f source, R destination) {
		MinecraftVectorUtilities.setVector4f(destination, source);
		destination.transform(matrix);
		return destination;
	}

	public static Matrix4f getIdentity() {
		return IDENTITY.copy();
	}

	public static AffineTransform toAffineTransform(Matrix4f matrix) {
		// COMMENT not all matrix data are converted
		FloatBuffer buf = FloatBuffer.allocate(getMatrixArrayLength());
		matrix.write(buf);
		return new AffineTransform(
				buf.get(AffineTransformUtilities.toMatrix4x4ArrayIndex(0, 0)), buf.get(AffineTransformUtilities.toMatrix4x4ArrayIndex(1, 0)),
				buf.get(AffineTransformUtilities.toMatrix4x4ArrayIndex(0, 1)), buf.get(AffineTransformUtilities.toMatrix4x4ArrayIndex(1, 1)),
				buf.get(AffineTransformUtilities.toMatrix4x4ArrayIndex(0, 3)), buf.get(AffineTransformUtilities.toMatrix4x4ArrayIndex(1, 3))
		);
	}

	public static int getMatrixArrayLength() {
		return MATRIX_ARRAY_LENGTH;
	}
}
