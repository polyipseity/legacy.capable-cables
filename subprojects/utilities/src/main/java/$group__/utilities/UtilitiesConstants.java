package $group__.utilities;

import $group__.utilities.compile.EnumBuildType;

public enum UtilitiesConstants {
	;

	public static final String DISPLAY_NAME = "${displayName}";
	public static final String BUILD_TYPE_STRING = "${buildType}";
	public static final EnumBuildType BUILD_TYPE = EnumBuildType.valueOfSafe(BUILD_TYPE_STRING);
}
