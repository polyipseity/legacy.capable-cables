package $group__.utilities.helpers;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import static $group__.utilities.helpers.Miscellaneous.getDefaultValue;
import static $group__.utilities.helpers.Miscellaneous.getDefaultValueNonnull;
import static $group__.utilities.helpers.Primitives.PRIMITIVE_DATA_TYPE_SET;
import static $group__.utilities.helpers.Primitives.PRIMITIVE_TYPE_SET;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static tests.TestUtilities.consumeCaught;

@Testable
public class MiscellaneousTest {
	/* SECTION methods */

	@Test
	void testMarkUnused() {
		// COMMENT positive
		PRIMITIVE_DATA_TYPE_SET.forEach(Miscellaneous::getDefaultValueNonnull);
		assertNull(getDefaultValue(null));

		// COMMENT negative
		try {
			PRIMITIVE_TYPE_SET.forEach(Miscellaneous::getDefaultValueNonnull);
			fail("Should have caught NullPointerException");
		} catch (NullPointerException e) { consumeCaught(e); }
		try {
			getDefaultValueNonnull(Object.class);
			fail("Should have caught NullPointerException");
		} catch (NullPointerException e) { consumeCaught(e); }
	}
}
