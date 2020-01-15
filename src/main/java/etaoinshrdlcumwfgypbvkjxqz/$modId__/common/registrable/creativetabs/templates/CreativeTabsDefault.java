package etaoinshrdlcumwfgypbvkjxqz.$modId__.common.registrable.creativetabs.templates;

import net.minecraft.creativetab.CreativeTabs;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.common.registrable.utilities.helpers.RegistryHelper.getNamespacedUnlocalizedNameForRegistry;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.variables.Constants.MOD_ID;

public abstract class CreativeTabsDefault extends CreativeTabs {
	/* SECTION constructors */

	public CreativeTabsDefault(String label) { super(getNamespacedUnlocalizedNameForRegistry(MOD_ID, label)); }

	@SuppressWarnings("unused")
	public CreativeTabsDefault(int index, String label) { super(index, getNamespacedUnlocalizedNameForRegistry(MOD_ID, label)); }
}
