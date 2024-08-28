package com.ericsson.cifwk.taf.mapping;

import com.ericsson.cifwk.taf.model.GAV;
import com.ericsson.cifwk.taf.model.Testware;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestwareMapperTest {

    TestwareMapper mapper;

    @Mock
    GAV gav;

    @Before
    public void setUp() {
        mapper = new TestwareMapper();
        mapper.nexusUrlTemplate = "https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/service/local/artifact/maven/redirect?r=${type}&g=${groupId}&a=${artifactId}&v=${version}&e=${extension}";

        when(gav.getGroupId()).thenReturn("com.ericsson");
        when(gav.getArtifactId()).thenReturn("taf");
        when(gav.getVersion()).thenReturn("1.2.3");
    }

    @Test
    public void testMap() {
        Testware result = mapper.map(gav);

        assertThat(result.getGroupId(), is("com.ericsson"));
        assertThat(result.getArtifactId(), is("taf"));
        assertThat(result.getVersion(), is("1.2.3"));
        assertThat(result.getPomLocation(), is("https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/service/local/artifact/maven/redirect?r=releases&g=com.ericsson&a=taf&v=1.2.3&e=pom"));
    }
}
