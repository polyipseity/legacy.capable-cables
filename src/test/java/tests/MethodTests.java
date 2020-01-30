package tests;

import $group__.$modId__.utilities.helpers.Miscellaneous;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import static $group__.$modId__.utilities.variables.Constants.PRIMITIVE_DATA_TYPE_SET;
import static $group__.$modId__.utilities.variables.Constants.PRIMITIVE_TYPE_SET;
import static org.junit.jupiter.api.Assertions.*;
import static tests.TestUtilities.consumeCaught;

@Testable
public class MethodTests {
	/* SECTION methods */

	@Test
	void testMarkUnused() {
		// COMMENT positive
		PRIMITIVE_DATA_TYPE_SET.forEach(t -> assertNotNull(Miscellaneous.markUnused(t)));
		assertNull(Miscellaneous.markUnused());

		// COMMENT negative
		try {
			PRIMITIVE_TYPE_SET.forEach(Miscellaneous::markUnused);
			fail("Should have caught NullPointerException");
		} catch (NullPointerException e) { consumeCaught(e); }
		try {
			Miscellaneous.markUnused(Object.class);
			fail("Should have caught NullPointerException");
		} catch (NullPointerException e) { consumeCaught(e); }
	}
}
