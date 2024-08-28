/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */
package com.ericsson.cifwk.taf;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class TestRegistryClientTest {

    private TestRegistryClient trClient;

    @Before
    public void setUp() {
        trClient = new TestRegistryClient(null, null);
    }

    @Test
    public void extractId() {
        assertEquals("123-456", trClient.extractId("http://host:8080/registry/api/testware/123-456"));
    }

}
