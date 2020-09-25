package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftMatrixUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ui.UIObjectUtilities;
import net.minecraft.client.renderer.Matrix4f;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Marker;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.util.ResourceBundle;

public enum AffineTransformUtilities {
	;

	private static final int SCALE_X_INDEX = toFlatMatrixArrayIndex(0, 0);
	private static final int SHEAR_X_INDEX = toFlatMatrixArrayIndex(0, 1);
	private static final int SHEAR_Y_INDEX = toFlatMatrixArrayIndex(1, 0);
	private static final int SCALE_Y_INDEX = toFlatMatrixArrayIndex(1, 1);
	private static final int TRANSLATE_X_INDEX = toFlatMatrixArrayIndex(0, 2);
	private static final int TRANSLATE_Y_INDEX = toFlatMatrixArrayIndex(1, 2);
	@SuppressWarnings("DuplicateStringLiteralInspection")
	private static final @NonNls ImmutableMap<String, Integer> NAME_TO_MATRIX_INDEX_MAP = ImmutableMap.<String, Integer>builder()
			.put("translateX", AffineTransformUtilities.getTranslateXIndex())
			.put("translateY", AffineTransformUtilities.getTranslateYIndex())
			.put("scaleX", AffineTransformUtilities.getScaleXIndex())
			.put("scaleY", AffineTransformUtilities.getScaleYIndex())
			.put("shearX", AffineTransformUtilities.getShearXIndex())
			.put("shearY", AffineTransformUtilities.getShearYIndex())
			.build();
	private static final Marker CLASS_MARKER = UtilitiesMarkers.getInstance().getClassMarker();
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());
	private static final AffineTransform IDENTITY = new AffineTransform();
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

	public static Marker getClassMarker() { return CLASS_MARKER; }

	public static AffineTransform getTransformFromTo(RectangularShape from, RectangularShape to) {
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
		AffineTransform transform = AffineTransform.getTranslateInstance(to.getX() - from.getX() * scaleX, to.getY() - from.getY() * scaleY);
		transform.scale(scaleX, scaleY);
		return transform;
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	public static <R extends RectangularShape> R transformRectangularShape(AffineTransform transform, RectangularShape source, R destination) {
		Point2D[] points = UIObjectUtilities.getDiagonalsFromRectangular(source);
		destination.setFrameFromDiagonal(transformPoint(transform, points[0], points[0]), transformPoint(transform, points[1], points[1]));
		return destination;
	}

	public static <R extends Point2D> R transformPoint(AffineTransform transform, Point2D source, R destination) {
		transform.transform(source, destination);
		return destination;
	}

	public static double[] getFlatMatrixIdentity() { return FLAT_MATRIX_IDENTITY.clone(); }

	public static AffineTransform getIdentity() { return (AffineTransform) IDENTITY.clone(); }

	public static Matrix4f toMatrix(AffineTransform transform) {
		final double[] fm = new double[6];
		transform.getMatrix(fm);
		final float[] m4 = getMatrix4x4Identity();
		m4[toMatrix4x4ArrayIndex(0, 0)] = (float) fm[getScaleXIndex()];
		m4[toMatrix4x4ArrayIndex(1, 0)] = (float) fm[getShearXIndex()];
		m4[toMatrix4x4ArrayIndex(0, 1)] = (float) fm[getShearYIndex()];
		m4[toMatrix4x4ArrayIndex(1, 1)] = (float) fm[getScaleYIndex()];
		m4[toMatrix4x4ArrayIndex(0, 3)] = (float) fm[getTranslateXIndex()];
		m4[toMatrix4x4ArrayIndex(1, 3)] = (float) fm[getTranslateYIndex()];
		return new Matrix4f(m4);
	}

	public static float[] getMatrix4x4Identity() { return MATRIX_4X4_IDENTITY.clone(); }

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

	public static int getScaleXIndex() {
		return SCALE_X_INDEX;
	}

	public static int getShearXIndex() {
		return SHEAR_X_INDEX;
	}

	public static int getShearYIndex() {
		return SHEAR_Y_INDEX;
	}

	public static int getScaleYIndex() {
		return SCALE_Y_INDEX;
	}

	public static int getTranslateXIndex() {
		return TRANSLATE_X_INDEX;
	}

	public static int getTranslateYIndex() {
		return TRANSLATE_Y_INDEX;
	}

	public static ImmutableMap<String, Integer> getNameToMatrixIndexMap() { return NAME_TO_MATRIX_INDEX_MAP; }
}
