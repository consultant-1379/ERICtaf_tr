package com.ericsson.cifwk.taf.service;

import com.ericsson.cifwk.taf.model.GAV;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@RunWith(MockitoJUnitRunner.class)
public class TestwareScanServiceTest {

    @InjectMocks
    TestwareScanService service;

    @Test
    public void shouldConvertToGavs() {
        List<GAV> gavList = service.convertToGAVs("groupId:artifactId:version,groupId2:artifactId2:version2");
        assertThat(gavList, hasSize(2));
        verifyGAV(gavList.get(0), "groupId", "artifactId", "version");
        verifyGAV(gavList.get(1), "groupId2", "artifactId2", "version2");
    }

    @Test
    public void shouldConvertToGavsIgnoringWrong() {
        List<GAV> gavList = service.convertToGAVs("groupId:artifactId:version,groupId2:artifactId2,groupId3:artifactId3:version3");
        assertThat(gavList, hasSize(2));
        verifyGAV(gavList.get(0), "groupId", "artifactId", "version");
        verifyGAV(gavList.get(1), "groupId3", "artifactId3", "version3");
    }

    private void verifyGAV(GAV gav, String groupId, String artifactId, String version) {
        assertThat(gav.getGroupId(), is(groupId));
        assertThat(gav.getArtifactId(), is(artifactId));
        assertThat(gav.getVersion(), is(version));
    }


}
