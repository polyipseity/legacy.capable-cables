package $group__;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static $group__.ConstantsProduct.MOD_ID;
import static $group__.ConstantsProduct.NAME;

public enum Globals {
	;

	public static final Logger LOGGER = LogManager.getLogger(NAME + '|' + MOD_ID);
}
