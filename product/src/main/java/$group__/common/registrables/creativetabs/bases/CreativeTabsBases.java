package $group__.common.registrables.creativetabs.bases;

import $group__.utilities.Constants;

import static $group__.common.registrables.utilities.helpers.Registries.getNamespacedUnlocalizedNameForRegistry;

public enum CreativeTabsBases {
	/* MARK empty */;


	/* SECTION static methods */

	public static String initBaseLabel(String label) { return getNamespacedUnlocalizedNameForRegistry(Constants.MOD_ID, label); }
}
