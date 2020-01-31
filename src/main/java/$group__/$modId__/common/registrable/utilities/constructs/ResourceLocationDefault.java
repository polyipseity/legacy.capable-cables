package $group__.$modId__.common.registrable.utilities.constructs;

import $group__.$modId__.client.gui.utilities.constructs.XY;
import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static $group__.$modId__.utilities.helpers.Numbers.isNegative;
import static $group__.$modId__.utilities.variables.Constants.MOD_ID;

public class ResourceLocationDefault extends ResourceLocation {
	/* SECTION constructors */

	public ResourceLocationDefault(String resourceName) { super(MOD_ID, resourceName); }


	/* SECTION static classes */

	@SideOnly(Side.CLIENT)
	public static class Texture extends ResourceLocationDefault {
		/* SECTION variables */

		public final XY.Immutable<Float, ?> size;


		/* SECTION constructors */

		public Texture(String resourceName, float sizeX, float sizeY) { this(resourceName, new XY.Immutable<>(sizeX, sizeY)); }

		public Texture(String resourceName, XY.Immutable<Float, ?> size) {
			super(resourceName);
			this.size = size;
		}


		/* SECTION methods */

		public Rectangle.Immutable<Float, ?> generateRect(float offsetX, float offsetY) {
			return new Rectangle.Immutable<>(new XY<>(
					isNegative(offsetX) ? size.getX() + offsetX : offsetX,
					isNegative(offsetY) ? size.getY() + offsetY : offsetY), size);
		}
	}
}
