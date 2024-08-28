package com.ericsson.cifwk.taf.service;

import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestwareAnalysisServiceITest {

    private MavenProjectAnalysisService service;

    @Before
    public void setUp() {
        service = new MavenProjectAnalysisService();
    }

    @Test
    public void testRetrieveMavenProjectWithTafVersion() {
        Optional<MavenProject> maybeMavenProject = service.retrieveMavenProject("https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443" +
                "/nexus/content/repositories/releases/com/ericsson/oss/services/cm/" +
                "test-pom/1.0.2094/test-pom-1.0.2094.pom");

        assertThat(maybeMavenProject.isPresent(), is(true));

        String tafVersion = service.getTafVersion(maybeMavenProject.get());

        assertThat(tafVersion, is("2.3.1"));
    }
}
