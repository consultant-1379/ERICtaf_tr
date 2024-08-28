/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.scanner.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SharedOperator implements Comparable<SharedOperator> {

    private String name;

    private Gav gav;

    private Set<String> context = new HashSet<>();

    private List<PublicMethod> publicMethods = new ArrayList<>();

    public SharedOperator() {
        context.add("UNKNOWN");
    }

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

    public Set<String> getContext() {
        return Collections.unmodifiableSet(context);
    }

    public void addContext(String ctx) {
        removeUnknownContext();
        context.add(ctx);
    }

    private void removeUnknownContext() {
        context.remove("UNKNOWN");
    }

    public List<PublicMethod> getPublicMethods() {
        return publicMethods;
    }

    public void addPublicMethod(PublicMethod publicMethod) {
        publicMethods.add(publicMethod);
    }

    @Override
    public int compareTo(SharedOperator o) {
        return name.compareTo(o.name);
    }
}
