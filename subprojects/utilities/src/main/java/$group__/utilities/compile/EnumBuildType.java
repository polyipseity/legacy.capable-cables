package $group__.utilities.compile;

import $group__.utilities.UtilitiesConfiguration;
import $group__.utilities.throwable.ThrowableUtilities;

public enum EnumBuildType {
	DEBUG,
	RELEASE,
	;

	public static EnumBuildType valueOfSafe(String name) {
		return ThrowableUtilities.getQuietly(() ->
				valueOf(name), IllegalArgumentException.class, UtilitiesConfiguration.getInstance().getThrowableHandler())
				.orElse(DEBUG); // COMMENT default value in case the string did not get replaced
	}

	public boolean isDebug() { return this == DEBUG; }

	public boolean isRelease() { return this == RELEASE; }
}
