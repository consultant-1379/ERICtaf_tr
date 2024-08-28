package com.ericsson.cifwk.taf.query;

public enum QueryOperator {

    EQUAL("="),
    NOT_EQUAL("!="),
    CONTAINS("~");

    private final String syntax;

    QueryOperator(String syntax) {
        this.syntax = syntax;
    }

    @Override
    public String toString() {
        return syntax;
    }

    public String syntax() {
        return toString();
    }

}
