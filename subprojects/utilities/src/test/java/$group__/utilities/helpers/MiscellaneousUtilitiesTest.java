package $group__.utilities.helpers;

import $group__.utilities.MiscellaneousUtilities;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import static $group__.utilities.MiscellaneousUtilities.getDefaultValue;
import static $group__.utilities.PrimitiveUtilities.PRIMITIVE_DATA_TYPE_SET;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testable
public class MiscellaneousUtilitiesTest {
	@Test
	void testMarkUnused() {
		// COMMENT positive
		PRIMITIVE_DATA_TYPE_SET.forEach(type -> assertTrue(MiscellaneousUtilities.getDefaultValue(type).isPresent()));

		// COMMENT negative
		assertFalse(getDefaultValue(null).isPresent());
		assertFalse(getDefaultValue(Object.class).isPresent());
	}
}
