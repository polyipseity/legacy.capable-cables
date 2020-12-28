package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.NamespaceUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import net.minecraft.resources.IResource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.io.InputStream;

@OnlyIn(Dist.CLIENT)
public enum MinecraftResourceUtilities {
	;

	public static InputStream getInputStream(IIdentifier location) throws IOException { return getResource(location).getInputStream(); }

	public static IResource getResource(IIdentifier location) throws IOException { return MinecraftClientUtilities.getMinecraftNonnull().getResourceManager().getResource(NamespaceUtilities.toResourceLocation(location)); }
}
