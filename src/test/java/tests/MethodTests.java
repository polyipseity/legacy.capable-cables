package tests;

import $group__.$modId__.utilities.helpers.Miscellaneous;
import $group__.$modId__.utilities.variables.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
public class MethodTests {
	/* SECTION methods */

	@Test
	void testMarkUnused() {
		// COMMENT positive
		Constants.PRIMITIVE_DATA_TYPES.keySet().forEach(t -> Assertions.assertNotNull(Miscellaneous.markUnused(t)));
		Assertions.assertNull(Miscellaneous.markUnused());

		// COMMENT negative
		try {
			Miscellaneous.markUnused(Object.class);
			Assertions.fail("Should have caught NullPointerException");
		} catch (NullPointerException e) {
			TestUtilities.consumeCaught(e);
		}
	}
}
