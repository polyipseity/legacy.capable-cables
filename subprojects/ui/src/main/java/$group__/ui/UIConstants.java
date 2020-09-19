package $group__.ui;

import $group__.utilities.compile.EnumBuildType;
import org.jetbrains.annotations.NonNls;

public enum UIConstants {
	;

	@NonNls
	public static final String MODULE_NAME = "${moduleName}";
	@NonNls
	public static final String BUILD_TYPE_STRING = "${buildType}";
	public static final EnumBuildType BUILD_TYPE = EnumBuildType.valueOfSafe(BUILD_TYPE_STRING);

	public enum Local {
		;

		@NonNls
		public static final String XJC_MAIN_COMPONENTS_CONTEXT_PATH = "${xjcMainComponentsContextPath}";
	}
}
