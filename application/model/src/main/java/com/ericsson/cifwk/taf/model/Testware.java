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
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity(value = "testware", noClassnameStored = true)
public class Testware implements HasId<String> {

    @Id
    @NotNull
    private String id;

    // Is used by search
    private List<String> ids;

    private String groupId;

    private String artifactId;

    private String version;

    private List<String> versions = Lists.newArrayList();

    private String javaDocLocation;

    private String gitLocation;

    private String pomLocation;

    private String tafVersion;

    private int testStepsCount;

    @Embedded
    private List<Owner> owners = Lists.newArrayList();

    @Embedded
    private List<String> suites = Lists.newArrayList();

    @Embedded
    private List<TestStep> testSteps = Lists.newArrayList();

    private Date publishedAt = new Date();

    private String description;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> getVersions() {
        return versions;
    }

    public void addVersions(List<String> versions) {
        this.versions.addAll(versions);
    }

    public String getJavaDocLocation() {
        return javaDocLocation;
    }

    public void setJavaDocLocation(String javaDocLocation) {
        this.javaDocLocation = javaDocLocation;
    }

    public String getGitLocation() {
        return gitLocation;
    }

    public void setGitLocation(String gitLocation) {
        this.gitLocation = gitLocation;
    }

    public String getPomLocation() {
        return pomLocation;
    }

    public void setPomLocation(String pomLocation) {
        this.pomLocation = pomLocation;
    }

    public int getTestStepsCount() {
        return testStepsCount;
    }

    public void setTestStepsCount(int testStepsCount) {
        this.testStepsCount = testStepsCount;
    }

    public List<Owner> getOwners() {
        return owners;
    }

    public void addOwners(List<Owner> owners) {
        this.owners.addAll(owners);
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTafVersion() {
        return tafVersion;
    }

    public void setTafVersion(String tafVersion) {
        this.tafVersion = tafVersion;
    }

    public List<String> getSuites() {
        return suites;
    }

    public void addSuites(List<String> suites) {
        this.suites.addAll(suites);
    }

    public List<TestStep> getTestSteps() {
        return testSteps;
    }

    public void addTestSteps(List<TestStep> testSteps) {
        this.testSteps.addAll(testSteps);
    }

    @Override
    public String toString() {
        return String.join(":", groupId, artifactId, version);
    }
}
