package $group__.common.registrables.utilities.helpers.specific;

import $group__.utilities.Constants;
import net.minecraft.util.ResourceLocation;

public enum ResourceLocations {
	/* MARK empty */;


	/* SECTION static methods */

	public static String appendDefaultDomain(String path) { return newResourceLocation(path).toString(); }

	public static ResourceLocation newResourceLocation(String path) { return new ResourceLocation(Constants.MOD_ID, path); }
}
