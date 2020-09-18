package $group__;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static $group__.ModConstants.DISPLAY_NAME;
import static $group__.ModConstants.MOD_ID;

public enum ModGlobals {
	;

	public static final Logger LOGGER = LogManager.getLogger(DISPLAY_NAME + '|' + MOD_ID);
}
