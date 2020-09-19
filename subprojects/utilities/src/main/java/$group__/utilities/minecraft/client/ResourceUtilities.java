package $group__.utilities.minecraft.client;

import $group__.utilities.NamespaceUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import net.minecraft.resources.IResource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.io.InputStream;

@OnlyIn(Dist.CLIENT)
public enum ResourceUtilities {
	;

	public static InputStream getInputStream(INamespacePrefixedString location) throws IOException { return getResource(location).getInputStream(); }

	public static IResource getResource(INamespacePrefixedString location) throws IOException { return ClientUtilities.getMinecraftNonnull().getResourceManager().getResource(NamespaceUtilities.toResourceLocation(location)); }
}
