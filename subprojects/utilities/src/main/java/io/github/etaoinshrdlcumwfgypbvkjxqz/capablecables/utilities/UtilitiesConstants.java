package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.compile.EnumBuildType;
import org.jetbrains.annotations.NonNls;

public enum UtilitiesConstants {
	;

	@NonNls
	public static final String MODULE_NAME = "${moduleName}";
	@NonNls
	public static final String BUILD_TYPE_STRING = "${buildType}";
	private static final EnumBuildType BUILD_TYPE = EnumBuildType.valueOfSafe(getBuildTypeString());

	public static String getModuleName() {
		return MODULE_NAME;
	}

	public static String getBuildTypeString() {
		return BUILD_TYPE_STRING;
	}

	public static EnumBuildType getBuildType() {
		return BUILD_TYPE;
	}
}
