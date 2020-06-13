package $group__.common.registrables.utilities.helpers;

import net.minecraft.util.ResourceLocation;

public enum Registries {
	/* MARK empty */;


	@SuppressWarnings("SpellCheckingInspection")
	public static String getNamespacedUnlocalizedNameForRegistry(ResourceLocation name) { return getNamespacedUnlocalizedNameForRegistry(name.getResourceDomain(), name.getResourcePath()); }

	@SuppressWarnings("SpellCheckingInspection")
	public static String getNamespacedUnlocalizedNameForRegistry(String modId, String name) { return modId + '.' + name; }
}
