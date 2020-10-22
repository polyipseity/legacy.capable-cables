package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.compile.EnumBuildType;
import org.jetbrains.annotations.NonNls;

public enum UIConstants {
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

	public enum Local {
		;

		@NonNls
		public static final String XJC_MAIN_UI_CONTEXT_PATH = "${xjcMainUIContextPath}";

		public static String getXJCMainUIContextPath() {
			return XJC_MAIN_UI_CONTEXT_PATH;
		}
	}
}
