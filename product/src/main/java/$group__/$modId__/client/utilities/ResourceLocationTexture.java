package $group__.$modId__.client.utilities;

import $group__.$modId__.client.gui.coordinates.XY;
import $group__.$modId__.client.gui.polygons.Rectangle;
import $group__.$modId__.utilities.concurrent.MutatorImmutable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ResourceLocationTexture extends ResourceLocation {
	/* SECTION variables */

	public final XY<?, Float> size;


	/* SECTION constructors */

	public ResourceLocationTexture(ResourceLocation location, XY<?, Float> size) {
		super(location.getResourceDomain(), location.getResourcePath());
		this.size = size;
	}


	/* SECTION methods */

	public Rectangle<?, Float> makeRectangle(XY<?, Float> offset) { return new Rectangle<>(offset, size, MutatorImmutable.INSTANCE, offset.getLogging()); }
}
