package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NonNls;

public enum NamespaceUtilities {
	;

	@NonNls
	public static String getNamespacePrefixedString(@NonNls String separator, @NonNls String namespace, @NonNls String string) { return namespace + separator + string; }

	public static ResourceLocation toResourceLocation(INamespacePrefixedString string) { return new ResourceLocation(string.getNamespace(), string.getPath()); }
}
