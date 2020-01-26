import $group__.$modId__.utilities.helpers.Tracking;
import $group__.$modId__.utilities.variables.Constants;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
public class MethodTests {
	/* SECTION methods */

	@Test
	void testMarkUnused() { Constants.PRIMITIVE_DATA_TYPES.keySet().forEach(Tracking::markUnused); }
}
