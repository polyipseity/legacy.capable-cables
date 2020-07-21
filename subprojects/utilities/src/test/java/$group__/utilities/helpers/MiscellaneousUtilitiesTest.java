package $group__.utilities.helpers;

import $group__.utilities.MiscellaneousUtilities;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import static $group__.utilities.MiscellaneousUtilities.getDefaultValue;
import static $group__.utilities.MiscellaneousUtilities.getDefaultValueNonnull;
import static $group__.utilities.Primitives.PRIMITIVE_DATA_TYPE_SET;
import static $group__.utilities.Primitives.PRIMITIVE_TYPE_SET;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static tests.TestUtilities.consumeCaught;

@Testable
public class MiscellaneousUtilitiesTest {
	@Test
	void testMarkUnused() {
		// COMMENT positive
		PRIMITIVE_DATA_TYPE_SET.forEach(MiscellaneousUtilities::getDefaultValueNonnull);
		assertNull(getDefaultValue(null));

		// COMMENT negative
		try {
			PRIMITIVE_TYPE_SET.forEach(MiscellaneousUtilities::getDefaultValueNonnull);
			fail("Should have caught NullPointerException");
		} catch (NullPointerException e) { consumeCaught(e); }
		try {
			getDefaultValueNonnull(Object.class);
			fail("Should have caught NullPointerException");
		} catch (NullPointerException e) { consumeCaught(e); }
	}
}
