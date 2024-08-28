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
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity(value="imports", noClassnameStored=true)
public class DataImport implements HasId<String> {

    @Id
    @NotNull
    private String id;

    @NotNull
    private String user;

    @NotNull
    private String source;

    private String host;

    private Date timestamp = new Date();

    @Reference
    private Testware testware;

    private List<Operator> operators = Lists.newArrayList();

    @Override
    public String toString() {
        return "Import{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", user='" + user + '\'' +
                ", host='" + host + '\'' +
                ", source='" + source + '\'' +
                '}';
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Testware getTestware() {
        return testware;
    }

    public void setTestware(Testware testware) {
        this.testware = testware;
    }

    public List<Operator> getOperators() {
        return operators;
    }

    public void addOperators(List<Operator> operators) {
        this.operators.addAll(operators);
    }
}
