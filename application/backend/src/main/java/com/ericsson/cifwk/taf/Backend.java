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
import com.ericsson.cifwk.taf.modules.MongoDbModule;
import com.ericsson.cifwk.taf.modules.SchedulingModule;
import com.google.inject.Guice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.servlet.SparkApplication;

public class Backend implements SparkApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(Backend.class);

    @Override
    public void init() {
        LOGGER.info("Starting...");

        Guice.createInjector(
                new ConfigModule(),
                new MongoDbModule(),
                new ApplicationModule(),
                new SchedulingModule()
        );
    }

}
