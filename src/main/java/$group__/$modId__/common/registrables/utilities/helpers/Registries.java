package $group__.$modId__.common.registrables.utilities.helpers;

import net.minecraft.util.ResourceLocation;

public enum Registries {
	/* MARK empty */;


	/* SECTION static methods */

	@SuppressWarnings("SpellCheckingInspection")
	public static String getNamespacedUnlocalizedNameForRegistry(ResourceLocation name) { return getNamespacedUnlocalizedNameForRegistry(name.getResourceDomain(), name.getResourcePath()); }

	@SuppressWarnings("SpellCheckingInspection")
	public static String getNamespacedUnlocalizedNameForRegistry(String modId, String name) { return modId + '.' + name; }
}
