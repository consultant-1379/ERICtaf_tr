package com.ericsson.cifwk.scanner.model;

import java.util.ArrayList;
import java.util.List;

public class PublicMethod implements Comparable<PublicMethod> {

    private String name;

    private String className;

    private String returnType;

    private List<Argument> arguments = new ArrayList<>();

    public PublicMethod(String name, String className) {
        this.name = name;
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public void addArgument(Argument argument) {
        arguments.add(argument);
    }

    @Override
    public String toString() {
        return String.format("%s %s from %s", getReturnType(), getName(), getClassName());
    }

    @Override
    public int compareTo(PublicMethod publicMethod) {
        return name.compareTo(publicMethod.name);
    }
}
