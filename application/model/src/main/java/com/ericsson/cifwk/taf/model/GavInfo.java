package com.ericsson.cifwk.taf.model;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class GavInfo {

    private String groupId;

    private String artifactId;

    private String version;

    public GavInfo() {
    }

    public GavInfo(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

}
