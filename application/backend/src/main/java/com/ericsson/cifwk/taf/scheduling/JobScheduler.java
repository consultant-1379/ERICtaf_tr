/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.taf.scheduling;

import com.ericsson.cifwk.taf.scheduling.job.TestwareScanJob;
import com.google.inject.Inject;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.inject.Singleton;

@Singleton
public class JobScheduler {

    public static final int JOB_WAIT_TIMEOUT = 30;

    private static final Logger LOGGER = LoggerFactory.getLogger(JobScheduler.class);
    private final Scheduler scheduler;

    @Inject
    public JobScheduler(SchedulerFactory factory, GuiceJobFactory jobFactory) throws SchedulerException {
        scheduler = factory.getScheduler();
        scheduler.setJobFactory(jobFactory);
        scheduler.start();
    }

    @Inject
    public void scheduleJobs() {
        try {
            JobDetail testwareScanJob = JobBuilder.newJob(TestwareScanJob.class)
                    .build();

            scheduler.scheduleJob(testwareScanJob, buildTrigger());
        } catch (SchedulerException e) {
            LOGGER.error("Unable to schedule job", e);
        }
    }

    private Trigger buildTrigger() {
        return TriggerBuilder
                .newTrigger()
                .startNow()
                .withSchedule(
                        //execute at 12:00am every day of every month
                        CronScheduleBuilder.cronSchedule("0 0 * * * ?"))
                .build();
    }

    @PreDestroy
    public void shutdown() {
        LOGGER.info("Shutting down scheduler");
        try {
            scheduler.shutdown(true);

            int sec = 0;
            while (sec++ < JOB_WAIT_TIMEOUT) {
                Thread.sleep(1000);
                if (scheduler.isShutdown()) {
                    LOGGER.info("Shutting down scheduler successful");
                    return;
                }
            }
            LOGGER.error("Unable to shutdown scheduler for sec " + sec);
        } catch (SchedulerException | InterruptedException e) {
            LOGGER.error("Unable to shutdown scheduler: ", e);
        }
    }
}

