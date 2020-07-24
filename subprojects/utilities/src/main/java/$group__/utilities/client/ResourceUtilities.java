package $group__.utilities.client;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public enum ResourceUtilities {
	;

	public static IResource getResource(ResourceLocation location) throws IOException { return Minecraft.getInstance().getResourceManager().getResource(location); }
}
