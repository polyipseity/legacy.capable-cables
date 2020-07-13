package $group__.client.utilities;

import $group__.client.gui.coordinates.XY;
import $group__.client.gui.polygons.Rectangle;
import $group__.utilities.concurrent.MutatorImmutable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ResourceLocationTexture extends ResourceLocation {
	public final XY<?, Float> size;


	public ResourceLocationTexture(ResourceLocation location, XY<?, Float> size) {
		super(location.getResourceDomain(), location.getResourcePath());
		this.size = size;
	}


	public Rectangle<?, Float> makeRectangle(XY<?, Float> offset) { return new Rectangle<>(offset, size, MutatorImmutable.INSTANCE, offset.getLogging()); }
}
