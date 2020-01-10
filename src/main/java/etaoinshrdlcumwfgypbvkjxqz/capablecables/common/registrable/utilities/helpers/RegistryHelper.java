package etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.utilities.helpers;

import net.minecraft.util.ResourceLocation;

public enum RegistryHelper {
	;

	@SuppressWarnings("SpellCheckingInspection")
	public static String getNamespacedUnlocalizedNameForRegistry(ResourceLocation name) { return name.getResourceDomain() + "." + name.getResourcePath(); }

	@SuppressWarnings("SpellCheckingInspection")
	public static String getNamespacedUnlocalizedNameForRegistry(String modId, String name) { return modId + "." + name; }
}
