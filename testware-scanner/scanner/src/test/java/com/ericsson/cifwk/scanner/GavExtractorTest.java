package com.ericsson.cifwk.scanner;

import com.ericsson.cifwk.scanner.model.Gav;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;

public class GavExtractorTest {

    private InputStream pomWithParent;
    private InputStream standAlonePom;

    @Before
    public void setUp() {
        standAlonePom = checkNotNull(GavExtractorTest.class.getClassLoader().getResourceAsStream("pom-stand-alone.xml"));
        pomWithParent = checkNotNull(GavExtractorTest.class.getClassLoader().getResourceAsStream("pom-with-parent.xml"));
    }

    @Test
    public void get() {
        assertGav(GavExtractor.get(standAlonePom));
        assertGav(GavExtractor.get(pomWithParent));
    }

    private void assertGav(Gav gav) {
        assertEquals("com.ericsson.cifwk", gav.getGroupId());
        assertEquals("tr-testware-scanner", gav.getArtifactId());
        assertEquals("1.0.22-SNAPSHOT", gav.getVersion());
    }
}