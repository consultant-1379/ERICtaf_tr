/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.taf.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.mongodb.morphia.annotations.Embedded;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Embedded
public class TestStep implements HasId<String> {

    @NotNull
    private String id;

    private String name;

    private String description;

    private String component;

    private Set<String> context = Sets.newHashSet();

    private String returnType;

    @Embedded
    private List<Parameter> attributes = Lists.newArrayList();

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Set<String> getContext() {
        return context;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public List<Parameter> getAttributes() {
        return attributes;
    }

}
