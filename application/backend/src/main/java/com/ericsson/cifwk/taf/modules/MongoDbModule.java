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
import com.google.common.base.Throwables;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;

public class MongoDbModule extends AbstractModule {

    @Override
    protected void configure() {
        // nothing to configure
    }

    @Provides
    public MongoClient connectToMongoDb(@Named(Environment.MONGODB_HOST) String host,
                                        @Named(Environment.MONGODB_PORT) String port) {
        MongoClient mongoClient;
        try {
            mongoClient = new MongoClient(host, Integer.parseInt(port));
        } catch (UnknownHostException e) {
            throw Throwables.propagate(e);
        }
        return mongoClient;
    }

}
