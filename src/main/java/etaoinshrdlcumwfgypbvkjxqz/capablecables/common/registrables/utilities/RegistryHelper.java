package etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.utilities;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.Constants.MOD_ID;

public enum RegistryHelper {
    ;
    @SuppressWarnings("SpellCheckingInspection")
    public static String getNamespacedUnlocalizedNameForRegistry(String name) { return String.format("%s.%s", MOD_ID, name); }
}
