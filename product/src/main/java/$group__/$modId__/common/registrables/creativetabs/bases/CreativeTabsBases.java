package $group__.$modId__.common.registrables.creativetabs.bases;

import static $group__.$modId__.common.registrables.utilities.helpers.Registries.getNamespacedUnlocalizedNameForRegistry;
import static $group__.$modId__.utilities.Constants.MOD_ID;

public enum CreativeTabsBases {
	/* MARK empty */;


	/* SECTION static methods */

	public static String initBaseLabel(String label) { return getNamespacedUnlocalizedNameForRegistry(MOD_ID, label); }
}
