package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.compile;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;

public enum EnumBuildType {
	DEBUG,
	RELEASE,
	;

	public static EnumBuildType valueOfSafe(CharSequence name) {
		return ThrowableUtilities.getQuietly(() ->
				valueOf(name.toString()), IllegalArgumentException.class, UtilitiesConfiguration.getInstance().getThrowableHandler())
				.orElse(DEBUG); // COMMENT default value in case the string did not get replaced
	}

	public boolean isDebug() { return this == DEBUG; }

	public boolean isRelease() { return this == RELEASE; }
}
