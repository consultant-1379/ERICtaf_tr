package com.ericsson.cifwk.taf.query;

public enum OperatorQueryField {

    NAME("name", "name", "Name"),
    METHOD_NAME("methodName", "publicMethods.name", "Method Name"),
    CONTEXT("context", "context", "Context"),
    GROUP_ID("groupId", "gav.groupId", "Group ID"),
    ARTIFACT_ID("artifactId", "gav.artifactId", "Artifact ID"),
    VERSION("version", "gav.version", "Version");

    private final String queryStringField;

    private final String actualField;

    private final String displayName;

    OperatorQueryField(String queryStringField, String actualField, String displayName) {
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