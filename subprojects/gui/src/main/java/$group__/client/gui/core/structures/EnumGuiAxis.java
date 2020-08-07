package $group__.client.gui.core.structures;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;

@OnlyIn(Dist.CLIENT)
public enum EnumGuiAxis {
	X {
		@Override
		public double getCoordinate(Point2D point) { return point.getX(); }
	},
	Y {
		@Override
		public double getCoordinate(Point2D point) { return point.getY(); }
	};

	public abstract double getCoordinate(Point2D point);
}
