package $group__.client.gui.structures;

import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Dimension2D;
import java.beans.Transient;
import java.io.Serializable;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public class Dimension2DDouble extends Dimension2D implements Serializable {
	private static final long serialVersionUID = 4432299344969417136L;
	protected double width, height;

	public Dimension2DDouble() { this(0, 0); }

	public Dimension2DDouble(double width, double height) { setSize(width, height); }

	@Override
	public double getWidth() { return width; }

	@Override
	public double getHeight() { return height; }

	@Override
	public void setSize(double width, double height) {
		this.width = width;
		this.height = height;
	}

	@SuppressWarnings("unused")
	@Transient
	public Dimension2DDouble getSize() {
		return new Dimension2DDouble(width, height);
	}

	public boolean equals(Object obj) {
		if (obj instanceof Dimension2DDouble) {
			Dimension2DDouble d = (Dimension2DDouble) obj;
			return (width == d.width) && (height == d.height);
		}
		return false;
	}

	public int hashCode() {
		double sum = width + height;
		return (int) (sum * (sum + 1) / 2 + width);
	}

	public String toString() { return getClass().getName() + "[width=" + width + ",height=" + height + ']'; }
}
