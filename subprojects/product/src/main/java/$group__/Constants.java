package $group__;

import $group__.utilities.compile.EnumBuildType;

public enum Constants {
	;

	public static final String MOD_ID = "${modId}";
	public static final String GROUP = "${group}";
	public static final String NAME = "${name}";
	public static final String BUILD_TYPE_STRING = "${buildType}";
	public static final EnumBuildType BUILD_TYPE = EnumBuildType.fromString(BUILD_TYPE_STRING);
}
