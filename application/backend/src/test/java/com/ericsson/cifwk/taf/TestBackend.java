/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.taf;

import com.ericsson.cifwk.taf.modules.ApplicationModule;
import com.ericsson.cifwk.taf.modules.ConfigModule;
import com.ericsson.cifwk.taf.modules.SchedulingModule;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mongodb.MongoClient;

import java.util.List;

public class TestBackend {

    private final MongoClient mongoClient;
    private boolean scheduling;
    private Injector injector;

    public TestBackend(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public TestBackend(MongoClient mongoClient, boolean scheduling) {
        this.mongoClient = mongoClient;
        this.scheduling = scheduling;
    }

    public void init() {
        List<AbstractModule> modules = Lists.newArrayList(
                new ConfigModule(),
                new FongoDbModule(mongoClient),
                new ApplicationModule());

        if (scheduling) {
            modules.add(new SchedulingModule());
        }
        injector = Guice.createInjector(modules);
    }

    public Injector getInjector() {
        return injector;
    }

}
