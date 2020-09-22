package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.NamespaceUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;
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
