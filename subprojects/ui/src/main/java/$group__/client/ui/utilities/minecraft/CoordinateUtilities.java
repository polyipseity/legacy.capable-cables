package $group__.client.ui.utilities.minecraft;

import $group__.client.ui.structures.Dimension2DDouble;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

@OnlyIn(Dist.CLIENT)
public enum CoordinateUtilities {
	;

	public static Rectangle2D toScaledRectangle(Rectangle2D rectangle) {
		Rectangle2D r = (Rectangle2D) rectangle.clone();
		r.setFrame(toScaledPoint(new Point2D.Double(rectangle.getX(), rectangle.getMaxY())), toScaledDimension(new Dimension2DDouble(rectangle.getWidth(), rectangle.getHeight())));
		return r;
	}

	public static Point2D toScaledPoint(Point2D point) {
		Point2D p = (Point2D) point.clone();
		p.setLocation(toScaledCoordinate(p.getX()), toScaledCoordinate(Minecraft.getInstance().getMainWindow().getFramebufferHeight() - p.getY()));
		return p;
	}

	public static Dimension2D toScaledDimension(Dimension2D dimension) {
		Dimension2D dim = (Dimension2D) dimension.clone();
		dim.setSize(toScaledCoordinate(dim.getWidth()), toScaledCoordinate(dim.getHeight()));
		return dim;
	}

	public static double toScaledCoordinate(double d) { return d / Minecraft.getInstance().getMainWindow().getGuiScaleFactor(); }

	public static Rectangle2D toNativeRectangle(Rectangle2D rectangle) {
		Rectangle2D r = (Rectangle2D) rectangle.clone();
		r.setFrame(toNativePoint(new Point2D.Double(rectangle.getX(), rectangle.getMaxY())), toNativeDimension(new Dimension2DDouble(rectangle.getWidth(), rectangle.getHeight())));
		return r;
	}

	public static Point2D toNativePoint(Point2D point) {
		Point2D p = (Point2D) point.clone();
		p.setLocation(toNativeCoordinate(p.getX()), toNativeCoordinate(Minecraft.getInstance().getMainWindow().getScaledHeight() - p.getY()));
		return p;
	}

	public static Dimension2D toNativeDimension(Dimension2D dimension) {
		Dimension2D dim = (Dimension2D) dimension.clone();
		dim.setSize(toNativeCoordinate(dim.getWidth()), toNativeCoordinate(dim.getHeight()));
		return dim;
	}

	public static double toNativeCoordinate(double d) { return d * Minecraft.getInstance().getMainWindow().getGuiScaleFactor(); }
}
