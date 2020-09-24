package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftClientUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.DoubleDimension2D;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

@OnlyIn(Dist.CLIENT)
// TODO, improve this
public enum MinecraftCoordinateUtilities {
	;

	public static Rectangle2D toScaledRectangle(Rectangle2D rectangle) {
		Rectangle2D r = (Rectangle2D) rectangle.clone();
		r.setFrame(toScaledPoint(new Point2D.Double(rectangle.getX(), rectangle.getMaxY())), toScaledDimension(new DoubleDimension2D(rectangle.getWidth(), rectangle.getHeight())));
		return r;
	}

	public static Point2D toScaledPoint(Point2D point) {
		Point2D p = (Point2D) point.clone();
		p.setLocation(toScaledCoordinate(p.getX()), toScaledCoordinate(MinecraftClientUtilities.getMinecraftNonnull().getMainWindow().getFramebufferHeight() - p.getY()));
		return p;
	}

	public static Dimension2D toScaledDimension(Dimension2D dimension) {
		Dimension2D dim = (Dimension2D) dimension.clone();
		dim.setSize(toScaledCoordinate(dim.getWidth()), toScaledCoordinate(dim.getHeight()));
		return dim;
	}

	public static double toScaledCoordinate(double d) { return d / MinecraftClientUtilities.getMinecraftNonnull().getMainWindow().getGuiScaleFactor(); }

	public static Rectangle2D toNativeRectangle(Rectangle2D rectangle) {
		Rectangle2D r = (Rectangle2D) rectangle.clone();
		r.setFrame(toNativePoint(new Point2D.Double(rectangle.getX(), rectangle.getMaxY())), toNativeDimension(new DoubleDimension2D(rectangle.getWidth(), rectangle.getHeight())));
		return r;
	}

	public static Point2D toNativePoint(Point2D point) {
		Point2D p = (Point2D) point.clone();
		p.setLocation(toNativeCoordinate(p.getX()), toNativeCoordinate(MinecraftClientUtilities.getMinecraftNonnull().getMainWindow().getScaledHeight() - p.getY()));
		return p;
	}

	public static Dimension2D toNativeDimension(Dimension2D dimension) {
		Dimension2D dim = (Dimension2D) dimension.clone();
		dim.setSize(toNativeCoordinate(dim.getWidth()), toNativeCoordinate(dim.getHeight()));
		return dim;
	}

	public static double toNativeCoordinate(double d) { return d * MinecraftClientUtilities.getMinecraftNonnull().getMainWindow().getGuiScaleFactor(); }
}
