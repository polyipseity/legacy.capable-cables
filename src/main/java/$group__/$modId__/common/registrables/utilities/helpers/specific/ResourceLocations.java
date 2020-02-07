package $group__.$modId__.common.registrables.utilities.helpers.specific;

import net.minecraft.util.ResourceLocation;

import static $group__.$modId__.utilities.variables.Constants.MOD_ID;

public enum ResourceLocations {
	/* MARK empty */;


	/* SECTION static methods */

	public static String appendDefaultDomain(String path) { return newResourceLocation(path).toString(); }

	public static ResourceLocation newResourceLocation(String path) { return new ResourceLocation(MOD_ID, path); }
}
