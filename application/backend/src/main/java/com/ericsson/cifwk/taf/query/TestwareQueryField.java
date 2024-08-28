package com.ericsson.cifwk.taf.query;

public enum TestwareQueryField {
    GROUP_ID("groupId", "groupId", "Group ID"),
    ARTIFACT_ID("artifactId", "artifactId", "Artifact ID"),
    VERSION("version", "version", "Version"),
    SUITES("suites", "suites", "Suite Name"),
    TAF_VERSION("tafVersion", "tafVersion", "TAF Version"),
    JAVADOC_LOCATION("javaDocLocation", "javaDocLocation", "Javadoc Location"),
    GIT_LOCATION("gitLocation", "gitLocation", "Git Location"),
    POM_LOCATION("pomLocation", "pomLocation", "POM Location"),
    OWNERS("owner", "owners.id", "Owner"),
    DESCRIPTION("description", "testSteps.description", "Description"),
    NAME("name", "testSteps.name", "Name"),
    COMPONENT("component", "testSteps.component", "Component");

    private final String queryStringField;

    private final String actualField;

    private final String displayName;

    TestwareQueryField(String queryStringField, String actualField, String displayName) {
        this.queryStringField = queryStringField;
        this.actualField = actualField;
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public String queryStringField() {
        return queryStringField;
    }

    public String actualField() {
        return actualField;
    }

    public String displayName() {
        return displayName;
    }
}