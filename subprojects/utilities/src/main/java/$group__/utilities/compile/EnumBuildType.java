package $group__.utilities.compile;

import $group__.utilities.ThrowableUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum EnumBuildType {
	DEBUG,
	RELEASE,
	;

	private static final Logger LOGGER = LogManager.getLogger();

	public static EnumBuildType valueOfSafe(String name) {
		return ThrowableUtilities.Try.call(() ->
				valueOf(name), LOGGER)
				.orElse(DEBUG); // COMMENT default value in case the string did not get replaced
	}

	public boolean isDebug() { return this == DEBUG; }

	public boolean isRelease() { return this == RELEASE; }
}
