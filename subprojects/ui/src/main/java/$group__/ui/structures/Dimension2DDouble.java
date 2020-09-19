package $group__.ui.structures;

import $group__.utilities.ObjectUtilities;
import $group__.utilities.collections.MapUtilities;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NonNls;

import java.awt.geom.Dimension2D;
import java.beans.Transient;
import java.io.Serializable;
import java.util.function.Function;

public class Dimension2DDouble extends Dimension2D implements Serializable {
	private static final ImmutableList<Function<? super Dimension2DDouble, ?>> OBJECT_VARIABLES = ImmutableList.of(
			Dimension2DDouble::getWidth, Dimension2DDouble::getHeight);

	@NonNls
	private static final ImmutableMap<String, Function<? super Dimension2DDouble, ?>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchKeysValues(getObjectVariables().size(),
			ImmutableList.of("width", "height"),
			getObjectVariables()));
	private static final long serialVersionUID = 4432299344969417136L;
	private double width, height;

	public Dimension2DDouble() { this(0, 0); }

	public Dimension2DDouble(double width, double height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public int hashCode() {
		return ObjectUtilities.hashCode(this, null, getObjectVariables());
	}

	public static ImmutableList<Function<? super Dimension2DDouble, ?>> getObjectVariables() { return OBJECT_VARIABLES; }

	@Override
	public double getWidth() { return width; }

	@Override
	public double getHeight() { return height; }

	@Override
	public void setSize(double width, double height) {
		this.width = width;
		this.height = height;
	}

	@Transient
	public Dimension2DDouble getSize() { return new Dimension2DDouble(width, height); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equals(this, obj, true, null, getObjectVariables()); }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, getObjectVariablesMap()); }

	public static ImmutableMap<String, Function<? super Dimension2DDouble, ?>> getObjectVariablesMap() { return OBJECT_VARIABLES_MAP; }

	@Override
	public Dimension2DDouble clone() { return (Dimension2DDouble) super.clone(); }
}
