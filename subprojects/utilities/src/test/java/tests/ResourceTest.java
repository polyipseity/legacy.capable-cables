package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testable
public class ResourceTest {
	@Test
	@BeforeEach
	void testResourcesProperlyCopied() {
		assertNotNull(getClass().getResource("/test.txt"), "Resources NOT properly copied");
	}
}
