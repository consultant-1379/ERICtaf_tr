package com.ericsson.cifwk.taf.service;

import com.ericsson.cifwk.taf.model.GAV;
import com.ericsson.cifwk.taf.model.NexusRepositoryType;
import com.ericsson.cifwk.taf.model.Testware;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.http.client.fluent.Request;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

public class MavenProjectAnalysisService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MavenProjectAnalysisService.class);

    @Inject
    @Named("nexus.url.template")
    String nexusUrlTemplate;

    public Optional<MavenProject> retrieveMavenProject(String pomUrl) {
        try {
            return Request.Get(pomUrl).execute().handleResponse(response -> {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    try (InputStream pom = response.getEntity().getContent()) {
                        return Optional.of(parseMavenProject(pom));
                    }
                }
                return Optional.<MavenProject>empty();
            });
        } catch (IOException e) {
            LOGGER.error("Failed to execute request to " + pomUrl, e);
        }
        return Optional.empty();
    }

    public Optional<MavenProject> retrieveMavenProject(Testware testware) {
        Optional<MavenProject> mavenProject = retrieveMavenProject(testware, NexusRepositoryType.RELEASES);
        return mavenProject.isPresent() ? mavenProject : retrieveMavenProject(testware, NexusRepositoryType.OSS_RELEASES);
    }

    private Optional<MavenProject> retrieveMavenProject(Testware testware, NexusRepositoryType type) {
        Map<String, String> map = ImmutableMap.of(
                "type", type.getValue(),
                "extension", "pom",
                "groupId", testware.getGroupId(),
                "artifactId", testware.getArtifactId(),
                "version", testware.getVersion());

        String pomUrl = StrSubstitutor.replace(nexusUrlTemplate, map);
        return retrieveMavenProject(pomUrl);
    }

    /**
     * Parse POM input stream and build maven project from it
     * Doesn't close Input stream, should be done outside of the method
     *
     * @param pomInputStream
     * @return Maven project
     */
    @VisibleForTesting
    protected MavenProject parseMavenProject(InputStream pomInputStream) {
        Model model = null;
        MavenXpp3Reader mavenReader = new MavenXpp3Reader();
        try {
            model = mavenReader.read(pomInputStream);
        } catch (Exception ex) {
            LOGGER.error("Failed to process pom", ex);
        }
        return new MavenProject(model);
    }

    /**
     * Scans dependency declarations and properties in Maven project for taf version
     *
     * @param project
     * @return taf version
     */
    public String getTafVersion(MavenProject project) {
        Set<String> artifactIds = Sets.newHashSet("taf-bom", "all-taf-sdk");

        DependencyManagement dm = project.getDependencyManagement();
        List<Dependency> dependencies = dm != null ? dm.getDependencies() : project.getDependencies();

        for (Dependency dependency : dependencies) {
            if (artifactIds.contains(dependency.getArtifactId())) {
                String version = dependency.getVersion();
                if (version != null && version.startsWith("${")) {
                    String property = version.substring(2, version.length() - 1);
                    return project.getProperties().getProperty(property);
                }
                return version;
            }
        }

        //Last attempt by property names
        return findInProperties(project.getProperties(), "taf-version", "taf.version", "taf_version");
    }

    private String findInProperties(Properties properties, String... names) {
        for (String name : names) {
            String property = properties.getProperty(name);
            if (property != null) {
                return property;
            }
        }
        return null;
    }

    /**
     * Scans dependency declarationsin Maven project for operator artifact id
     *
     * @param project
     * @return taf version
     */
    public List<GAV> getOperatorArtifacts(MavenProject project) {
        DependencyManagement dm = project.getDependencyManagement();
        Properties props = project.getProperties();

        List<GAV> operators = findIn(project.getDependencies(), props, project);
        if (dm != null) {
            operators.addAll(findIn(dm.getDependencies(), props, project));
        }
        return operators;
    }

    private List<GAV> findIn(List<Dependency> dependencies, Properties props, MavenProject project) {
        List<GAV> operators = Lists.newArrayList();

        if (dependencies != null) {
            for (Dependency dependency : dependencies) {
                if (dependency.getArtifactId().toLowerCase().contains("operator")) {
                    GAV gav = new GAV();
                    gav.setGroupId(getGroupId(dependency.getGroupId(), project));
                    gav.setArtifactId(dependency.getArtifactId());
                    gav.setVersion(getDependencyVersion(dependency, props, project));

                    if (gav.isValid()) {
                        operators.add(gav);
                    }
                }
            }
        }
        return operators;
    }

    /**
     * Handle ${project.groupId} cases
     *
     * @param groupId
     * @param project
     * @return
     */
    private String getGroupId(String groupId, MavenProject project) {
        if (groupId.startsWith("${")) {
            return project.getGroupId();
        }
        return groupId;
    }

    /**
     * Handle ${project.version}, ${custom.version} cases
     *
     * @param dependency
     * @param props
     * @return
     */
    private String getDependencyVersion(Dependency dependency, Properties props, MavenProject project) {
        String version = dependency.getVersion();
        if (version == null) {
            return project.getVersion();
        }
        if (version.startsWith("${")) {
            if ("${project.version}".equals(version)) {
                return project.getVersion();
            } else {
                String property = version.substring(2, version.length() - 1);
                return props.getProperty(property);
            }
        }
        return version;
    }
}
