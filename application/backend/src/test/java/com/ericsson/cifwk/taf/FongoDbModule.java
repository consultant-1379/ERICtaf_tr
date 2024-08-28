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

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mongodb.MongoClient;

public class FongoDbModule extends AbstractModule {

    private final MongoClient mongoClient;

    public FongoDbModule(MongoClient mongoClient) {
        super();
        this.mongoClient = mongoClient;
    }

    @Override
    protected void configure() {
    }

    @Provides
    public MongoClient connectToMongoDb() {
        return mongoClient;
    }

}
