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
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class ConfigModule extends AbstractModule {

    @Override
    protected void configure() {
        Environment environment = new Environment();
        bind(Environment.class).toInstance(environment);
        Names.bindProperties(binder(), environment.toProperties());
    }

}
