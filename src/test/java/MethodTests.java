import $group__.$modId__.utilities.variables.Constants;
import $group__.$modId__.utilities.variables.References;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
public class MethodTests {
	/* SECTION methods */

	@Test
	void testMarkUnused() { Constants.PRIMITIVE_DATA_TYPES.keySet().forEach(References::markUnused); }
}
