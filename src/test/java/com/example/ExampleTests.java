package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;

// TODO: (Optional) Write your tests.
@Testable
@DisplayName("Example Tests")
public class ExampleTests {
    @Test
    @DisplayName("Find Dummy")
    void findDummy() {
        assertResourceExists("/dummy.txt");
    }

    private static URL assertResourceExists(String name) {
        URL resource = ExampleTests.class.getResource(name);
        assertNotNull(resource, "Resource '" + name + "' should exists");
        return resource;
    }
}
