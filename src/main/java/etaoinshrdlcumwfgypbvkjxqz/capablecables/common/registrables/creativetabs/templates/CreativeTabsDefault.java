package etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.creativetabs.templates;

import net.minecraft.creativetab.CreativeTabs;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.RegistryHelper.getNamespacedUnlocalizedNameForRegistry;

public abstract class CreativeTabsDefault extends CreativeTabs {
    public CreativeTabsDefault(String label) { super(getNamespacedUnlocalizedNameForRegistry(label)); }
    @SuppressWarnings("unused")
    public CreativeTabsDefault(int index, String label) { super(index, getNamespacedUnlocalizedNameForRegistry(label)); }
}
