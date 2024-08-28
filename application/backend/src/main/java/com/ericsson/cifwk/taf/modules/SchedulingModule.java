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

import com.ericsson.cifwk.taf.scheduling.GuiceJobFactory;
import com.ericsson.cifwk.taf.scheduling.JobScheduler;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class SchedulingModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SchedulerFactory.class).to(StdSchedulerFactory.class).in(Scopes.SINGLETON);
        bind(GuiceJobFactory.class).in(Scopes.SINGLETON);
        bind(JobScheduler.class).asEagerSingleton();
    }
}
