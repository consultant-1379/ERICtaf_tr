package com.ericsson.cifwk.taf.service;

import com.ericsson.cifwk.taf.mapping.TestwareMapper;
import com.ericsson.cifwk.taf.model.GAV;
import com.ericsson.cifwk.taf.model.Testware;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestwareImportServiceITest {

    @Spy
    TestwareMapper testwareMapper;

    @InjectMocks
    TestwareImportService testwareImportService;

    @Mock
    Testware validTestware;

    @Mock
    Testware validTestwareInOssRepository;

    @Mock
    Testware invalidTestware;

    static {
        System.setProperty("jsse.enableSNIExtension", "false");
    }

    @Before
    public void setUp() {
        testwareImportService.latestTestwareService = "https://cifwk-oss.lmera.ericsson.se/getLatestTestware/";
        testwareImportService.nexusUrlTemplate = "https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/service/local/" +
                "artifact/maven/redirect?r=${type}&g=${groupId}&a=${artifactId}&v=${version}&e=${extension}";

        when(validTestware.getGroupId()).thenReturn("com.ericsson.cifwk");
        when(validTestware.getArtifactId()).thenReturn("tr-sample-testware");
        when(validTestware.getVersion()).thenReturn("1.0.10");

        when(invalidTestware.getGroupId()).thenReturn("com.wikipedia.is.cool");
        when(invalidTestware.getArtifactId()).thenReturn("invalid");
        when(invalidTestware.getVersion()).thenReturn("26.01.2015");

        when(validTestwareInOssRepository.getGroupId()).thenReturn("com.ericsson.oss.orchestrator");
        when(validTestwareInOssRepository.getArtifactId()).thenReturn("ERICTAForch_CXP9031018");
        when(validTestwareInOssRepository.getVersion()).thenReturn("1.0.783");
    }

    @Test
    public void testDownloadJar_ReturnsFile() {
        Optional<File> jar = testwareImportService.downloadJar(validTestware);

        assertThat(jar.isPresent(), is(true));
        assertThat(jar.get().getName(), containsString(".jar"));
        jar.get().deleteOnExit();
    }

    @Test
    public void testDownloadJar_ReturnsFile_ArtifactInOSSRepository() {
        Optional<File> jar = testwareImportService.downloadJar(validTestwareInOssRepository);

        assertThat(jar.isPresent(), is(true));
        assertThat(jar.get().getName(), containsString(".jar"));
        jar.get().deleteOnExit();
    }

    @Test
    public void testDownloadJar_ReturnsEmpty() {
        Optional<File> jar = testwareImportService.downloadJar(invalidTestware);
        assertThat(jar.isPresent(), is(false));
    }

    @Test
    public void testGetMappedTestware_ReturnsListWithTestwares() {
        Optional<List<GAV>> mappedTestware = testwareImportService.getLatestTestwareGAV();

        assertThat(mappedTestware.isPresent(), is(true));
        assertThat(mappedTestware.get().size(), greaterThan(0));
    }
}
