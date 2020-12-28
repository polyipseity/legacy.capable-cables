package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.compile.EnumBuildType;
import org.jetbrains.annotations.NonNls;

import java.util.function.Supplier;

public enum UtilitiesConstants {
	;

	@NonNls
	public static final String MODULE_NAME = "${moduleName}";
	@NonNls
	public static final String BUILD_TYPE_STRING = "${buildType}";
	private static final Supplier<@Nonnull EnumBuildType> BUILD_TYPE = Suppliers.memoize(() -> EnumBuildType.valueOfSafe(getBuildTypeString())); // COMMENT lazy to prevent calling this before the module is configured

	public static String getModuleName() {
		return MODULE_NAME;
	}

	public static String getBuildTypeString() {
		return BUILD_TYPE_STRING;
	}

	public static EnumBuildType getBuildType() {
		return AssertionUtilities.assertNonnull(BUILD_TYPE.get());
	}
}
