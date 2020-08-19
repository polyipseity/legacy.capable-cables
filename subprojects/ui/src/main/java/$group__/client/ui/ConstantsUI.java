package $group__.client.ui;

import $group__.utilities.compile.EnumBuildType;

public enum ConstantsUI {
	;

	public static final String BUILD_TYPE_STRING = "${buildType}";
	public static final EnumBuildType BUILD_TYPE = EnumBuildType.valueOfSafe(BUILD_TYPE_STRING);
}
