package $group__.utilities.compile;

import $group__.utilities.ThrowableUtilities;
import $group__.utilities.UtilitiesConfiguration;

public enum EnumBuildType {
	DEBUG,
	RELEASE,
	;

	public static EnumBuildType valueOfSafe(String name) {
		return ThrowableUtilities.Try.call(() ->
				valueOf(name), UtilitiesConfiguration.INSTANCE.getLogger())
				.orElse(DEBUG); // COMMENT default value in case the string did not get replaced
	}

	public boolean isDebug() { return this == DEBUG; }

	public boolean isRelease() { return this == RELEASE; }
}
