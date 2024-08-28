package com.ericsson.cifwk.taf;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class JsonTransformerTest {

    JsonTransformer transformer;

    @Before
    public void setUp() {
        transformer = new JsonTransformer();
    }

    @Test
    public void testRender_ShouldSerializeNullFields() {
        String render = transformer.render(new MockObject());

        assertThat(render, is("{\"nullString\":null,\"notNullString\":\"notNull\"}"));
    }

    private static class MockObject {
        private String nullString;
        private String notNullString = "notNull";

        private String getNullString() {
            return nullString;
        }

        private String getNotNullString() {
            return notNullString;
        }
    }
}
