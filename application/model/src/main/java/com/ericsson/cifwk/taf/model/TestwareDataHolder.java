package com.ericsson.cifwk.taf.model;

import com.google.common.collect.Lists;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class TestwareDataHolder {
    private Testware testware;
    private List<TestStep> testSteps = Lists.newArrayList();
    private List<Operator> operators = Lists.newArrayList();
    private List<File> artifactJars = Lists.newArrayList();

    public TestwareDataHolder(Testware testware) {
        this.testware = testware;
    }

    public Testware getTestware() {
        return testware;
    }

    public void addTestSteps(List<TestStep> testSteps) {
        this.testSteps.addAll(testSteps);
    }

    public void addJar(File file) {
        artifactJars.add(file);
    }

    public List<TestStep> getTestSteps() {
        return Collections.unmodifiableList(testSteps);
    }

    public List<File> getArtifactJars() {
        return Collections.unmodifiableList(artifactJars);
    }

    public boolean jarExists() {
        return !artifactJars.isEmpty();
    }

    public List<Operator> getOperators() {
        return operators;
    }

    public void addOperators(List<Operator> operators) {
        this.operators.addAll(operators);
    }
}
