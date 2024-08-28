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

import com.ericsson.cifwk.scanner.SharedOperatorScanner;
import com.ericsson.cifwk.scanner.SharedTestStepsScanner;
import com.ericsson.cifwk.scanner.SuitesScanner;
import com.ericsson.cifwk.scanner.model.Argument;
import com.ericsson.cifwk.scanner.model.Gav;
import com.ericsson.cifwk.scanner.model.PublicMethod;
import com.ericsson.cifwk.scanner.model.SharedOperator;
import com.ericsson.cifwk.scanner.model.SharedTestSteps;
import com.ericsson.cifwk.taf.model.DataImport;
import com.ericsson.cifwk.taf.model.GavInfo;
import com.ericsson.cifwk.taf.model.Operator;
import com.ericsson.cifwk.taf.model.Owner;
import com.ericsson.cifwk.taf.model.TestStep;
import com.ericsson.cifwk.taf.model.Testware;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.model.Developer;
import org.apache.maven.model.Scm;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.RepositorySystem;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.UUID;

import static com.google.common.base.MoreObjects.firstNonNull;

@Mojo(name = "scan", defaultPhase = LifecyclePhase.DEPLOY, requiresProject = false)
public class ScanJarMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.groupId}")
    String groupId;

    @Parameter(defaultValue = "${project.artifactId}")
    String artifactId;

    @Parameter(defaultValue = "${project.version}")
    String version;

    @Parameter(defaultValue = "${project}", readonly = true)
    MavenProject project;

    @Component
    RepositorySystem repositorySystem;

    @Component
    ArtifactResolver artifactResolver;

    @Parameter(defaultValue = "${project.remoteArtifactRepositories}", readonly = true)
    List<ArtifactRepository> remoteRepositories;

    @Parameter(defaultValue = "${localRepository}", readonly = true)
    ArtifactRepository localRepository;

    @Parameter(property = "scan.failBuild", defaultValue = "false")
    boolean failBuild;

    @Parameter(property = "scan.publishTestware", defaultValue = "true")
    boolean publishTestware;

    @Parameter(property = "scan.trHost", defaultValue = "http://taf-registry.lmera.ericsson.se")
    String trHost;

    @Parameter(property = "scan.nexusBaseUrl", defaultValue = "https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/")
    String nexusBaseUrl;

    @Parameter(property = "scan.nexusRepository", defaultValue = "releases")
    String nexusRepository;

    @Parameter(property = "scan.javadocRepository", defaultValue = "tor")
    String javadocRepository;

    private SharedOperatorScanner sharedOperatorScanner = new SharedOperatorScanner();
    private SharedTestStepsScanner sharedTestStepsScanner = new SharedTestStepsScanner();

    private SuitesScanner suitesScanner = new SuitesScanner();

    private TestRegistryClient trClient;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        // only release versions could be published
        if (version.contains("SNAPSHOT") && publishTestware) {
            getLog().info("taf-scanner-maven-plugin is skipped for testware SNAPSHOT versions");
            return;
        }

        getLog().info("taf-scanner-maven-plugin configuration:");
        getLog().info("  failBuild: " + failBuild);
        getLog().info("  trHost: " + trHost);
        getLog().info("  publishTestware: " + publishTestware);

        trClient = new TestRegistryClient(trHost, getLog());
        File artifactFile = download();
        if (artifactFile != null) {
            getLog().info("Found artifact to be scanned: " + artifactFile.getAbsolutePath());
        } else {
            String errorMessage = "Artifact %s:%s:%s.jar not found. Testware will not be scanned and published.";
            getLog().warn(String.format(errorMessage, groupId, artifactId, version));
            return;
        }

        if (isAccepted(artifactFile)) {
            try {
                scanAndSend(artifactFile);
            } catch (Exception e) {
                String errorMessage = "Exception during artifact publishing (please make sure Test Step Registry is available): ";
                getLog().warn(errorMessage, e);
                if (failBuild) {
                    Throwables.propagate(e);
                }
            }
        }
    }

    protected boolean isAccepted(File artifactFile) {
        return artifactFile.getName().endsWith(".jar");
    }

    private void scanAndSend(File artifactFile) {
        if (!publishTestware) {
            getLog().info("Publishing testware is disabled.");
            return;
        }

        Testware testware = createTestware();

        List<String> suites = Lists.newArrayList();
        try {
            suites = suitesScanner.scanSuites(artifactFile);
        } catch (IOException e) {
            getLog().error("Failed to scan suites", e);
        }
        testware.addSuites(suites);

        // Scan for Shared Operators
        List<SharedOperator> sharedOperators = sharedOperatorScanner.scan(artifactFile);
        getLog().info(String.format("Scanning '%s' completed. Found %s operator(-s).",
                artifactFile.getName(), sharedOperators.size()));
        List<Operator> operators = Lists.newArrayList();
        for (SharedOperator sharedOperator : sharedOperators) {
            operators.add(sharedOperatorToEntity(sharedOperator));
        }

        // Scan for Shared TestSteps
        List<SharedTestSteps> sharedTestStepsList = sharedTestStepsScanner.scan(artifactFile);
        List<TestStep> testSteps = Lists.newArrayList();
        for (SharedTestSteps sharedTestSteps : sharedTestStepsList) {
            testSteps.addAll(testStepsToEntities(sharedTestSteps.getTestSteps()));
        }
        testware.addTestSteps(testSteps);
        testware.setTestStepsCount(testSteps.size());

        getLog().info(String.format("Scanning '%s' completed. Found " +
                        "%d Shared Operator(-s), %d Shared TestSteps class(-es) and %d suites files.",
                artifactFile.getName(), sharedOperators.size(), sharedTestStepsList.size(), suites.size()));

        DataImport dataImport = new DataImport();
        dataImport.setUser(System.getProperty("user.name"));
        dataImport.setSource("taf-scanner-maven-plugin");
        dataImport.setHost(getHost());
        dataImport.setTestware(testware);
        dataImport.addOperators(operators);

        trClient.saveTestware(testware);
        trClient.saveOperators(operators);
        trClient.saveImport(dataImport);
    }

    private Testware createTestware() {
        Testware testware = new Testware();
        testware.setGroupId(groupId);
        testware.setArtifactId(artifactId);
        testware.setVersion(version);
        testware.setDescription(project.getDescription());
        testware.setJavaDocLocation(getJavadocLocation());
        testware.setGitLocation(getGitLocation());
        testware.setPomLocation(getPomLocation());

        // developers
        List<Owner> owners = Lists.newArrayList();
        for (Developer developer : project.getDevelopers()) {
            owners.add(createOwner(developer));
        }
        testware.addOwners(owners);

        return testware;
    }

    private Owner createOwner(Developer developer) {
        Owner owner = new Owner();
        owner.setId(developer.getId());
        owner.setName(developer.getName());
        owner.setEmail(developer.getEmail());
        owner.setOrganization(developer.getOrganization());
        return owner;
    }

    private Operator sharedOperatorToEntity(SharedOperator sharedOperator) {
        Operator operator = new Operator();
        operator.setId(UUID.randomUUID().toString());
        operator.setName(sharedOperator.getName());
        operator.addAllContext(sharedOperator.getContext());
        operator.setGav(gavToEntity(sharedOperator.getGav()));

        List<PublicMethod> publicMethods = sharedOperator.getPublicMethods();
        operator.addAllPublicMethods(publicMethodsToEntities(publicMethods));
        operator.setPublicMethodsCount(publicMethods.size());
        return operator;
    }

    private GavInfo gavToEntity(Gav gav) {
        if (gav == null) {
            return null;
        }
        return new GavInfo(gav.getGroupId(), gav.getArtifactId(), gav.getVersion());
    }

    private List<TestStep> publicMethodsToEntities(List<PublicMethod> publicMethodList) {
        List<TestStep> testSteps = Lists.newArrayList();
        for (PublicMethod publicMethod : publicMethodList) {
            testSteps.add(publicMethodToEntity(publicMethod));
        }
        return testSteps;
    }

    private List<TestStep> testStepsToEntities(List<com.ericsson.cifwk.scanner.model.TestStep> testStepList) {
        List<TestStep> testSteps = Lists.newArrayList();
        for (com.ericsson.cifwk.scanner.model.TestStep testStep : testStepList) {
            TestStep testStepEntity = publicMethodToEntity(testStep);
            testStepEntity.setDescription(testStep.getDescription());
            testSteps.add(testStepEntity);
        }
        return testSteps;
    }

    private TestStep publicMethodToEntity(com.ericsson.cifwk.scanner.model.PublicMethod publicMethod) {
        TestStep testStepEntity = new TestStep();
        testStepEntity.setId(UUID.randomUUID().toString());
        testStepEntity.setName(publicMethod.getName());
        testStepEntity.setComponent(publicMethod.getClassName());
        testStepEntity.setReturnType(publicMethod.getReturnType());
        for (Argument argument : publicMethod.getArguments()) {
            com.ericsson.cifwk.taf.model.Parameter parameterEntity = new com.ericsson.cifwk.taf.model.Parameter();
            parameterEntity.setType(argument.getType());
            parameterEntity.setName(argument.getName());
            testStepEntity.getAttributes().add(parameterEntity);
        }
        return testStepEntity;
    }

    protected String getHost() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw Throwables.propagate(e);
        }
    }

    protected String getPomLocation() {
        return String.format("%s%s-%s.pom", getBaseNexusPath(), artifactId, version);
    }

    protected String getJavadocLocation() {
        String repoName = getRepoName();
        return String.format("https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/service/local/repositories/%s/content/%s/apidocs/index.html", javadocRepository, repoName);
    }

    private String getRepoName() {
        String gitLocation = getGitLocation();
        return gitLocation.substring(gitLocation.lastIndexOf("/") + 1);
    }

    private String getGitLocation() {
        Scm scm = project.getScm();
        return firstNonNull(scm.getConnection() == null ? scm.getDeveloperConnection() : scm.getConnection(), scm.getUrl());
    }

    protected String getBaseNexusPath() {
        return String.format("%s%s/%s/%s/%s/", nexusBaseUrl, nexusRepository, this.groupId.replaceAll("\\.", "/"), artifactId, version);
    }

    private File download() throws MojoFailureException {
        try {
            Artifact jarArtifact = repositorySystem.createArtifact(groupId, artifactId, version, "", "jar");
            artifactResolver.resolve(jarArtifact, this.remoteRepositories, this.localRepository);
            return jarArtifact.getFile();
        } catch (ArtifactResolutionException | ArtifactNotFoundException e) {
            throw new MojoFailureException("can't resolve parent pom", e);
        }
    }

}
