package etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.utilities;

import net.minecraft.util.ResourceLocation;

import javax.annotation.concurrent.Immutable;

@Immutable
public enum RegistryHelper {
    ;
    @SuppressWarnings("SpellCheckingInspection")
    public static String getNamespacedUnlocalizedNameForRegistry(ResourceLocation name) { return String.format("%s.%s", name.getResourceDomain(), name.getResourcePath()); }
    @SuppressWarnings("SpellCheckingInspection")
    public static String getNamespacedUnlocalizedNameForRegistry(String modID, String name) { return String.format("%s.%s", modID, name); }
}
