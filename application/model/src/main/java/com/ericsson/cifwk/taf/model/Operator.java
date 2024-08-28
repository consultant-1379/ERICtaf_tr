package com.ericsson.cifwk.taf.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Entity(value = "operators", noClassnameStored = true)
public class Operator implements HasId<String> {

    @Id
    @NotNull
    private String id;

    private String name;

    private GavInfo gav;

    private Set<String> context = Sets.newHashSet();

    @Embedded
    private List<TestStep> publicMethods = Lists.newArrayList();

    private int publicMethodsCount;

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

    public GavInfo getGav() {
        return gav;
    }

    public void setGav(GavInfo gav) {
        this.gav = gav;
    }

    public Set<String> getContext() {
        return context;
    }

    public void setContext(Set<String> context) {
        this.context = context;
    }

    public void addAllContext(Set<String> context) {
        this.context.addAll(context);
    }

    public List<TestStep> getPublicMethods() {
        return publicMethods;
    }

    public void addPublicMethod(TestStep testStep) {
        publicMethods.add(testStep);
    }

    public void addAllPublicMethods(List<TestStep> testStepList) {
        publicMethods.addAll(testStepList);
    }

    public int getPublicMethodsCount() {
        return publicMethodsCount;
    }

    public void setPublicMethodsCount(int publicMethodsCount) {
        this.publicMethodsCount = publicMethodsCount;
    }
}
