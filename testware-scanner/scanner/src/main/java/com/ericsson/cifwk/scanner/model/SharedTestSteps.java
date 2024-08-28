package com.ericsson.cifwk.scanner.model;

import java.util.ArrayList;
import java.util.List;

public class SharedTestSteps implements Comparable<SharedTestSteps> {

    private String name;

    private Gav gav;

    private List<TestStep> testSteps = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gav getGav() {
        return gav;
    }

    public void setGav(Gav gav) {
        this.gav = gav;
    }

    public List<TestStep> getTestSteps() {
        return testSteps;
    }

    public void addTestStep(TestStep testStep) {
        testSteps.add(testStep);
    }

    @Override
    public int compareTo(SharedTestSteps o) {
        return name.compareTo(o.name);
    }
}
