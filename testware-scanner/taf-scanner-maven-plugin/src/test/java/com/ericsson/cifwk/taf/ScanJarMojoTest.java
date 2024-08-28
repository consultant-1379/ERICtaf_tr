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

import org.apache.maven.model.Scm;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.stubs.ArtifactStub;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ScanJarMojoTest {

    public static final String BASE_NEXUS_PATH = "https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/";

    public static final String BASE_ARTIFACT_PATH = BASE_NEXUS_PATH + "releases/com/ericsson/cifwk/tr-sample-testware/1.0.0/";

    public static final String POM_PATH = BASE_ARTIFACT_PATH + "tr-sample-testware-1.0.0.pom";

    public static final String JAVADOC_PATH = "https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/service/local/repositories/tor/content/ERICtaf_tr/apidocs/index.html";

    @Rule
    public MojoRule rule = new MojoRule() {
        @Override
        protected void before() {
        }

        @Override
        protected void after() {
        }
    };

    private ScanJarMojo mojo;

    private MavenProject project;

    @Before
    public void setUp() throws Exception {
        File pom = new File(this.getClass().getResource("/test/scan/project-config.xml").getFile());
        mojo = (ScanJarMojo) rule.lookupMojo("scan", pom);
        assertNotNull(mojo);
        prepareProject();
        mojo.failBuild = true;
        mojo.trHost = "http://tr.host";
        mojo.nexusBaseUrl = BASE_NEXUS_PATH;
        mojo.nexusRepository = "releases";
        mojo.javadocRepository = "tor";
    }

    @Test
    public void getHost() {
        assertTrue(!StringUtils.isEmpty(mojo.getHost()));
    }

    @Test
    public void isAccepted() {
        assertTrue(mojo.isAccepted(new File("../artifact.jar")));
        assertFalse(mojo.isAccepted(new File("../artifact.jar.md5")));
    }

    @Test
    public void getBaseNexusPathFor() {
        assertEquals(BASE_ARTIFACT_PATH, mojo.getBaseNexusPath());
    }

    @Test
    public void getPomLocation() {
        assertEquals(POM_PATH, mojo.getPomLocation());
    }

    @Test
    public void getJavadocLocation() {
        assertEquals(JAVADOC_PATH, mojo.getJavadocLocation());
    }

    @Test
    public void shouldScanJarArtifact() throws Exception {
//        mojo.execute();
    }

    private void prepareProject() throws IllegalAccessException {
        ArtifactStub artifact = new ArtifactStub();
        artifact.setGroupId("artifactGroupId");
        artifact.setArtifactId("artifactId");
        artifact.setVersion("artifactVersion");

        project = new MavenProject();
        Scm scm = new Scm();
        scm.setUrl("https://gerrit.ericsson.se/#/admin/projects/OSS/com.ericsson.cifwk/ERICtaf_tr");
        mojo.groupId =  "com.ericsson.cifwk";
        mojo.artifactId = "tr-sample-testware";
        mojo.version = "1.0.0";
        project.setArtifact(artifact);
        project.setScm(scm);
        mojo.project = project;

        rule.setVariableValueToObject(mojo, "project", project);
    }

}
