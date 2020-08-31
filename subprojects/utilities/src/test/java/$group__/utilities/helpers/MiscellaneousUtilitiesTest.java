package $group__.utilities.helpers;

import $group__.utilities.MiscellaneousUtilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import java.util.Optional;

import static $group__.utilities.PrimitiveUtilities.PRIMITIVE_DATA_TYPE_SET;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Testable
public class MiscellaneousUtilitiesTest {
	@Test
	void testMarkUnused() {
		// COMMENT positive
		PRIMITIVE_DATA_TYPE_SET.stream().unordered()
				.map(MiscellaneousUtilities::getDefaultValue)
				.map(Optional::isPresent)
				.forEach(Assertions::assertTrue);

		// COMMENT negative
		assertFalse(MiscellaneousUtilities.getDefaultValue(null).isPresent());
		assertFalse(MiscellaneousUtilities.getDefaultValue(Object.class).isPresent());
	}
}
