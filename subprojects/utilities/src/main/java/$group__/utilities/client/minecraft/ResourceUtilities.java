package $group__.utilities.client.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public enum ResourceUtilities {
	;

	public static IResource getResource(ResourceLocation location) throws IOException { return Minecraft.getInstance().getResourceManager().getResource(location); }
}
