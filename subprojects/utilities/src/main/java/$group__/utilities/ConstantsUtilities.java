package $group__.utilities;

import $group__.utilities.compile.EnumBuildType;

public enum ConstantsUtilities {
	;

	public static final String BUILD_TYPE_STRING = "${buildType}";
	public static final EnumBuildType BUILD_TYPE = EnumBuildType.fromString(BUILD_TYPE_STRING);
}
