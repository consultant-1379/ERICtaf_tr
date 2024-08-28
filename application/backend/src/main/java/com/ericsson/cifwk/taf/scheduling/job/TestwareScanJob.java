package com.ericsson.cifwk.taf.scheduling.job;


import com.ericsson.cifwk.taf.model.GAV;
import com.ericsson.cifwk.taf.service.TestwareImportService;
import com.ericsson.cifwk.taf.service.TestwareScanService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
public class TestwareScanJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestwareScanJob.class);

    @Inject
    TestwareImportService testwareImportService;

    @Inject
    TestwareScanService testwareScanService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("Testware scan job started...");

        Optional<List<GAV>> maybeGavList = testwareImportService.getLatestTestwareGAV();
        if (!maybeGavList.isPresent()) {
            LOGGER.info("Service didn't return any GAVs, skipping import...");
            return;
        }

        testwareScanService.scan(maybeGavList.get());

        LOGGER.info("Testware scan job completed...");
    }
}
