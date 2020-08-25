package $group__.utilities;

import $group__.utilities.interfaces.INamespacePrefixedString;
import net.minecraft.util.ResourceLocation;

public enum NamespaceUtilities {
	;

	public static String getNamespacePrefixedString(String separator, String namespace, String string) { return namespace + separator + string; }

	public static ResourceLocation toResourceLocation(INamespacePrefixedString string) { return new ResourceLocation(string.getNamespace(), string.getPath()); }
}
