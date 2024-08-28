/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.taf.modules;

import com.ericsson.cifwk.taf.Environment;
import com.ericsson.cifwk.taf.TestRegistryApplication;
import com.ericsson.cifwk.taf.model.DataImport;
import com.ericsson.cifwk.taf.model.Parameter;
import com.ericsson.cifwk.taf.model.TestStep;
import com.ericsson.cifwk.taf.model.Testware;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.ValidationExtension;

public class ApplicationModule extends AbstractModule {

    private Gson gson = new GsonBuilder().setDateFormat("EEE MMM dd kk:mm:ss z yyyy").create();

    @Override
    protected void configure() {
        bind(TestRegistryApplication.class).asEagerSingleton();
        bind(Gson.class).toInstance(gson);
    }

    @Provides
    public Datastore connectToDatabase(MongoClient mongoClient, @Named(Environment.MONGODB_DBNAME) String database) {
        Morphia morphia = new Morphia();
        morphia.map(DataImport.class)
                .map(TestStep.class)
                .map(Parameter.class)
                .map(Testware.class);

        new ValidationExtension(morphia);
        return morphia.createDatastore(mongoClient, database);
    }

}
