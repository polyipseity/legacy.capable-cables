package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.compile.EnumBuildType;
import org.jetbrains.annotations.NonNls;

public enum ModConstants {
	;

	@NonNls
	public static final String MOD_ID = "${modID}";
	@NonNls
	public static final String BUILD_TYPE_STRING = "${buildType}";
	private static final EnumBuildType BUILD_TYPE = EnumBuildType.valueOfSafe(getBuildTypeString());

	public static String getModId() {
		return MOD_ID;
	}

	public static String getBuildTypeString() {
		return BUILD_TYPE_STRING;
	}

	public static EnumBuildType getBuildType() {
		return BUILD_TYPE;
	}
}
