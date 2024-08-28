package com.ericsson.cifwk.taf.service;

import com.ericsson.cifwk.taf.model.GAV;
import com.google.common.io.Resources;
import org.apache.maven.project.MavenProject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class MavenProjectAnalysisTest {

    @InjectMocks
    MavenProjectAnalysisService testwareAnalysisService;

    InputStream pomWithoutTaf;
    InputStream pomWithTaf;
    InputStream pomWithOperators;
    GAV scannedGAV_1;
    GAV scannedGAV_2;

    @Before
    public void setUp() throws IOException {
        pomWithTaf = Resources.getResource("test-pom/has-taf.pom").openStream();
        pomWithoutTaf = Resources.getResource("test-pom/no-taf.pom").openStream();
        pomWithOperators = Resources.getResource("test-pom/testware-with-operators.pom").openStream();

        scannedGAV_1 = new GAV();
        scannedGAV_1.setGroupId("com.ericsson.nms.litp.taf");
        scannedGAV_1.setArtifactId("LITP2TAFOperators");
        scannedGAV_1.setVersion("1.0.77");

        scannedGAV_2 = new GAV();
        scannedGAV_2.setGroupId("com.ericsson.nms.litp.taf");
        scannedGAV_2.setArtifactId("LITP2TAFOperators");
        scannedGAV_2.setVersion("1.0.49");
    }

    @Test
    public void testParseProject_TafIsFound() {
        MavenProject mavenProject = testwareAnalysisService.parseMavenProject(pomWithTaf);

        String dependencyVersion = testwareAnalysisService.getTafVersion(mavenProject);
        assertThat(dependencyVersion, is("2.3.1"));
    }

    @Test
    public void testParseProject_TafNotFound() {
        MavenProject mavenProject = testwareAnalysisService.parseMavenProject(pomWithoutTaf);

        String dependencyVersion = testwareAnalysisService.getTafVersion(mavenProject);
        assertThat(dependencyVersion, nullValue());
    }

    @Test
    public void testGetOparatorArtifacts_ShouldFindTwo() {
        MavenProject mavenProject = testwareAnalysisService.parseMavenProject(pomWithOperators);
        List<GAV> operatorArtifacts = testwareAnalysisService.getOperatorArtifacts(mavenProject);

        assertThat(operatorArtifacts.size(), is(2));
        assertThat(operatorArtifacts, hasItems(scannedGAV_1, scannedGAV_2));
    }

    @After
    public void tearDown() throws IOException {
        pomWithTaf.close();
        pomWithoutTaf.close();
    }
}
