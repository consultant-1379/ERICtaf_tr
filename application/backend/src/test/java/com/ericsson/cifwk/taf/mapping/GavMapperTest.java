package com.ericsson.cifwk.taf.mapping;

import com.ericsson.cifwk.scanner.model.Gav;
import com.ericsson.cifwk.taf.model.GavInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GavMapperTest {

    GavMapper mapper;

    @Mock
    Gav gav;

    @Before
    public void setUp() {
        mapper = new GavMapper();

        when(gav.getGroupId()).thenReturn("groupId");
        when(gav.getArtifactId()).thenReturn("artifactId");
        when(gav.getVersion()).thenReturn("version");
    }

    @Test
    public void testMap() {
        GavInfo gavInfo = mapper.map(gav);

        assertThat(gavInfo.getGroupId(), is(gav.getGroupId()));
        assertThat(gavInfo.getArtifactId(), is(gav.getArtifactId()));
        assertThat(gavInfo.getVersion(), is(gav.getVersion()));
    }
}
