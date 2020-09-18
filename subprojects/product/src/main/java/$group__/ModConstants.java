package $group__;

import $group__.utilities.compile.EnumBuildType;

public enum ModConstants {
	;

	public static final String MOD_ID = "${modID}";
	public static final String DISPLAY_NAME = "${name}";
	public static final String BUILD_TYPE_STRING = "${buildType}";
	public static final EnumBuildType BUILD_TYPE = EnumBuildType.valueOfSafe(BUILD_TYPE_STRING);
}
