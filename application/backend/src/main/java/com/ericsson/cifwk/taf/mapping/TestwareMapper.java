package com.ericsson.cifwk.taf.mapping;

import com.ericsson.cifwk.taf.model.GAV;
import com.ericsson.cifwk.taf.model.Testware;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.text.StrSubstitutor;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;
import java.util.UUID;

public class TestwareMapper {

    @Inject
    @Named("nexus.url.template")
    String nexusUrlTemplate;

    public Testware map(GAV gav) {
        Testware testware = new Testware();

        testware.setId(UUID.randomUUID().toString());
        testware.setArtifactId(gav.getArtifactId());
        testware.setGroupId(gav.getGroupId());
        testware.setVersion(gav.getVersion());
        testware.setPomLocation(pomLocation(gav));
        testware.setGitLocation("");
        testware.setDescription("Testware imported by job");

        return testware;
    }

    private String pomLocation(GAV gav) {
        Map<String, String> map = ImmutableMap.of(
                "type", "releases",
                "extension", "pom",
                "groupId", gav.getGroupId(),
                "artifactId", gav.getArtifactId(),
                "version", gav.getVersion());

        return StrSubstitutor.replace(nexusUrlTemplate, map);
    }
}
