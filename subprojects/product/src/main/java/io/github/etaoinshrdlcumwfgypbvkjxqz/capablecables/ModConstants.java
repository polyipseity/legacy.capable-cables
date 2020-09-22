package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.compile.EnumBuildType;
import org.jetbrains.annotations.NonNls;

public enum ModConstants {
	;

	@NonNls
	public static final String MOD_ID = "${modID}";
	@NonNls
	public static final String BUILD_TYPE_STRING = "${buildType}";
	public static final EnumBuildType BUILD_TYPE = EnumBuildType.valueOfSafe(BUILD_TYPE_STRING);
}
