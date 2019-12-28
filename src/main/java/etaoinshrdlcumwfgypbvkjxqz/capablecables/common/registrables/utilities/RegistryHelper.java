package etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.utilities;

import javax.annotation.concurrent.Immutable;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.Constants.MOD_ID;

@Immutable
public enum RegistryHelper {
    ;
    @SuppressWarnings("SpellCheckingInspection")
    public static String getNamespacedUnlocalizedNameForRegistry(String name) { return String.format("%s.%s", MOD_ID, name); }
}
