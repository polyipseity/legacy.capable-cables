package $group__.$modId__.client.utilities;

import $group__.$modId__.client.gui.coordinates.XY;
import $group__.$modId__.client.gui.polygons.Rectangle;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static $group__.$modId__.utilities.helpers.specific.Numbers.isNegative;

@SideOnly(Side.CLIENT)
public class ResourceLocationTexture extends ResourceLocation {
	/* SECTION variables */

	public final XY.Immutable<Float, ?> size;


	/* SECTION constructors */

	public ResourceLocationTexture(ResourceLocation location, float sizeX, float sizeY) { this(location, new XY.Immutable<>(sizeX, sizeY, )); }

	public ResourceLocationTexture(ResourceLocation location, XY.Immutable<Float, ?> size) {
		super(location.toString());
		this.size = size;
	}


	/* SECTION methods */

	public Rectangle.Immutable<Float, ?> generateRectangle(float offsetX, float offsetY) {
		return new Rectangle.Immutable<>(new XY<>(
				isNegative(offsetX) ? size.getX() + offsetX : offsetX,
				isNegative(offsetY) ? size.getY() + offsetY : offsetY, ), size);
	}
}
