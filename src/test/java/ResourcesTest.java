import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
public class ResourcesTest {
	@Test
	@BeforeEach
	void testResourcesProperlyCopied() {
		Assertions.assertNotNull(getClass().getResource("/test.txt"), "Resources NOT properly copied");
	}
}
