package com.ericsson.cifwk.taf.model;

public enum NexusRepositoryType {
    RELEASES("releases"),
    OSS_RELEASES("oss_releases");

    private final String value;

    NexusRepositoryType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
