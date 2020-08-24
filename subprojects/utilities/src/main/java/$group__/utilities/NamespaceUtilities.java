package $group__.utilities;

import $group__.utilities.interfaces.INamespacePrefixedString;
import net.minecraft.util.ResourceLocation;

public enum NamespaceUtilities {
	;

	public static final String
			NAMESPACE_MINECRAFT_DEFAULT = "minecraft",
			NAMESPACE_SEPARATOR_DEFAULT = INamespacePrefixedString.SEPARATOR,
			NAMESPACE_MINECRAFT_DEFAULT_PREFIX = NAMESPACE_MINECRAFT_DEFAULT + NAMESPACE_SEPARATOR_DEFAULT;

	public static String getNamespacePrefixedString(String separator, String namespace, String string) { return namespace + separator + string; }

	public static ResourceLocation toResourceLocation(INamespacePrefixedString string) { return new ResourceLocation(string.getNamespace(), string.getPath()); }
}
