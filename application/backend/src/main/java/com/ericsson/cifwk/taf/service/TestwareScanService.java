package com.ericsson.cifwk.taf.service;

import com.ericsson.cifwk.scanner.SuitesScanner;
import com.ericsson.cifwk.taf.dao.TestwareDAO;
import com.ericsson.cifwk.taf.mapping.TestwareMapper;
import com.ericsson.cifwk.taf.model.GAV;
import com.ericsson.cifwk.taf.model.Operator;
import com.ericsson.cifwk.taf.model.TestStep;
import com.ericsson.cifwk.taf.model.Testware;
import com.ericsson.cifwk.taf.model.TestwareDataHolder;
import com.google.common.collect.Lists;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TestwareScanService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestwareScanService.class);

    @Inject
    TestwareImportService testwareImportService;

    @Inject
    MavenProjectAnalysisService projectAnalysisService;

    @Inject
    JarFileScanner jarFileScanner;

    @Inject
    SuitesScanner suitesScanner;

    @Inject
    TestwareDAO testwareDAO;

    @Inject
    TestwareMapper testwareMapper;


    public void scan(List<GAV> gavList) {
        List<TestwareDataHolder> testwareList = filterNewAndMap(gavList);

        testwareList.stream()
                .peek(this::downloadTestwareJar)
                .filter(TestwareDataHolder::jarExists)
                .peek(this::findSuitesInTestwareJar)
                .peek(this::scanTestwarePomAndDownloadOperatorJars)
                .peek(this::scanForOperators)
                .peek(this::scanForTestSteps)
                .peek(holder -> testwareImportService.importTestwareData(holder.getTestware(), holder.getOperators()))
                .forEach(this::cleanResources);
    }

    /**
     * First makes attempt to scan testwares and jars that not exist in DB.
     * Returns list with testwares for specified GAVs. Incorrect gavs are ignored
     *
     * @param gavs
     * @return
     */
    public List<Testware> findTestwaresEagerly(List<GAV> gavs) {
        scan(gavs);

        List<Testware> testwares = Lists.newArrayList();
        for (GAV gav : gavs) {
            Optional<Testware> maybeTestware = Optional.ofNullable(testwareDAO.createQuery()
                    .filter("groupId", gav.getGroupId())
                    .filter("artifactId", gav.getArtifactId())
                    .filter("version", gav.getVersion()).get());
            maybeTestware.ifPresent(testwares::add);
        }

        return testwares;
    }

    public List<GAV> convertToGAVs(String artifactsList) {
        List<GAV> gavs = Lists.newArrayList();
        String[] artifacts = artifactsList.split(",");
        for (String artifact : artifacts) {
            String[] gav = artifact.split(":");
            if (gav.length == 3) {
                gavs.add(new GAV(gav[0], gav[1], gav[2]));
            }
        }
        return gavs;
    }


    private void findSuitesInTestwareJar(TestwareDataHolder testwareDataHolder) { //NOSONAR
        List<File> artifactJars = testwareDataHolder.getArtifactJars();
        for (File artifactJar : artifactJars) {
            try {
                List<String> suites = suitesScanner.scanSuites(artifactJar);
                LOGGER.info("Adding {} suites for Testware: {}", suites.size(), testwareDataHolder.getTestware().toString());
                testwareDataHolder.getTestware().addSuites(suites);
            } catch (IOException e) {
                LOGGER.error("Failed to scan suites", e);
            }
        }
    }

    private List<TestwareDataHolder> filterNewAndMap(List<GAV> rawTestwareList) {
        return rawTestwareList.stream()
                .map(testwareMapper::map)
                .filter(t -> !testwareDAO.exists(t))
                .map(TestwareDataHolder::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void downloadTestwareJar(TestwareDataHolder holder) { //NOSONAR
        Optional<File> testwareJar = testwareImportService.downloadJar(holder.getTestware());
        testwareJar.ifPresent(holder::addJar);
    }

    /**
     * Downloads testware pom, analyzes for Taf Version and Operator dependencies.
     *
     * @param holder TestwareDataHolder
     */
    private void scanTestwarePomAndDownloadOperatorJars(TestwareDataHolder holder) { //NOSONAR
        Optional<MavenProject> testwareProject = projectAnalysisService.retrieveMavenProject(holder.getTestware());
        testwareProject.ifPresent(project -> {
            retrieveAndSetTafVersion(holder.getTestware(), project);
            retrieveAndAddOperatorJars(holder, project);
        });
    }

    private void retrieveAndSetTafVersion(Testware testware, MavenProject testwareProject) {
        String tafVersion = projectAnalysisService.getTafVersion(testwareProject);
        testware.setTafVersion(tafVersion);
    }

    private void retrieveAndAddOperatorJars(TestwareDataHolder holder, MavenProject testwareProject) {
        List<GAV> operators = projectAnalysisService.getOperatorArtifacts(testwareProject);
        operators.stream().forEach(gav -> {
            Optional<File> maybeJar = testwareImportService.downloadJar(gav);
            maybeJar.ifPresent(holder::addJar);
        });
    }

    private void scanForOperators(TestwareDataHolder holder) { //NOSONAR
        holder.getArtifactJars().stream().forEach(jar -> {
            List<Operator> operators = jarFileScanner.scanForSharedOperators(jar);
            holder.addOperators(operators);
        });
    }

    private void scanForTestSteps(TestwareDataHolder holder) { //NOSONAR
        Testware testware = holder.getTestware();
        holder.getArtifactJars().stream().forEach(jar -> {
            List<TestStep> testSteps = jarFileScanner.scanForTestSteps(jar);
            testware.addTestSteps(testSteps);
            testware.setTestStepsCount(testSteps.size());
        });
    }

    private void cleanResources(TestwareDataHolder holder) { //NOSONAR
        try {
            List<File> artifactJars = holder.getArtifactJars();
            artifactJars.forEach(File::delete);
        } catch (Exception e) {
            LOGGER.error("Failed to delete temp jar file, testware: " + holder.getTestware(), e);
        }
    }

}
