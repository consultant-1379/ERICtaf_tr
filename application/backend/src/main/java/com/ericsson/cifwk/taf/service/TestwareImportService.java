package com.ericsson.cifwk.taf.service;

import com.ericsson.cifwk.taf.dao.DataImportDAO;
import com.ericsson.cifwk.taf.dao.OperatorDAO;
import com.ericsson.cifwk.taf.dao.TestwareDAO;
import com.ericsson.cifwk.taf.mapping.TestStepMapper;
import com.ericsson.cifwk.taf.mapping.TestwareMapper;
import com.ericsson.cifwk.taf.model.ArtifactItems;
import com.ericsson.cifwk.taf.model.DataImport;
import com.ericsson.cifwk.taf.model.GAV;
import com.ericsson.cifwk.taf.model.NexusRepositoryType;
import com.ericsson.cifwk.taf.model.Operator;
import com.ericsson.cifwk.taf.model.Testware;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteStreams;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import spark.utils.IOUtils;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Service to receive list of Testware with links to test-pom
 * Original service location: https://cifwk-oss.lmera.ericsson.se/getLatestTestware/
 * Service documentation available: http://confluence-oss.lmera.ericsson.se/display/CIOSS/Get+Latest+Testware+Information
 */
public class TestwareImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestwareImportService.class);
    private StringResponseHandler stringResponseHandler;
    private TempFileResponseHandler tempFileResponseHandler;

    @Inject
    TestwareMapper testwareMapper;

    @Inject
    TestStepMapper testStepMapper;

    @Inject
    DataImportDAO dataImportDAO;

    @Inject
    TestwareDAO testwareDAO;

    @Inject
    OperatorDAO operatorDAO;

    @Inject
    @Named("ci-portal.testware.service")
    String latestTestwareService;

    @Inject
    @Named("nexus.url")
    String nexusUrl;

    @Inject
    @Named("nexus.url.template")
    String nexusUrlTemplate;

    public TestwareImportService() throws JAXBException {
        tempFileResponseHandler = new TempFileResponseHandler();
        stringResponseHandler = new StringResponseHandler();
    }

    public Optional<List<GAV>> getLatestTestwareGAV() {
        Optional<String> maybeResponseBody = executeGetRequest(latestTestwareService, stringResponseHandler);

        if (maybeResponseBody.isPresent()) {
            try {
                ArtifactItems artifacts = unmarshallBody(maybeResponseBody.get());
                return Optional.of(artifacts.getArtifacts());
            } catch (JAXBException e) {
                LOGGER.error("Failed to parse response", e);
            }
        }
        return Optional.empty();
    }

    private ArtifactItems unmarshallBody(String maybeResponseBody) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(ArtifactItems.class);
        Unmarshaller um = ctx.createUnmarshaller();
        return (ArtifactItems) um.unmarshal(new InputSource(new StringReader(maybeResponseBody)));
    }

    public Optional<File> downloadJar(Testware t) {
        Optional<File> maybeJar = downloadJar(t.getGroupId(), t.getArtifactId(), t.getVersion(), NexusRepositoryType.RELEASES);
        return maybeJar.isPresent() ? maybeJar : downloadJar(t.getGroupId(), t.getArtifactId(), t.getVersion(), NexusRepositoryType.OSS_RELEASES);
    }

    public Optional<File> downloadJar(GAV g) {
        Optional<File> maybeJar = downloadJar(g.getGroupId(), g.getArtifactId(), g.getVersion(), NexusRepositoryType.RELEASES);
        return maybeJar.isPresent() ? maybeJar : downloadJar(g.getGroupId(), g.getArtifactId(), g.getVersion(), NexusRepositoryType.OSS_RELEASES);
    }

    private Optional<File> downloadJar(String groupId, String artifactId, String version, NexusRepositoryType type) {
        Map<String, String> map = ImmutableMap.of(
                "type", type.getValue(),
                "extension", "jar",
                "groupId", groupId,
                "artifactId", artifactId,
                "version", version);

        String jarUrl = StrSubstitutor.replace(nexusUrlTemplate, map);

        Optional<File> maybeJar = executeGetRequest(jarUrl, tempFileResponseHandler);
        if (maybeJar.isPresent()) {
            LOGGER.info("Successfully downloaded JAR from Nexus for " + groupId + ":" + artifactId + ":" + version);
        }
        return maybeJar;
    }

    public void importTestwareData(Testware testware, List<Operator> operators) {
        DataImport dataImport = createDataImport(testware, operators);
        testwareDAO.save(testware);
        dataImportDAO.save(dataImport);
        for (Operator operator : operators) {
            operatorDAO.save(operator);
        }
        LOGGER.info(String.format("Saved %d operators from Testware[%s]", operators.size(), testware.toString()));
    }

    private DataImport createDataImport(Testware testware, List<Operator> operators) {
        DataImport dataImport = new DataImport();
        dataImport.setId(UUID.randomUUID().toString());
        dataImport.setTestware(testware);
        dataImport.setHost(nexusUrl);
        dataImport.setSource("job");
        dataImport.setUser("root");
        dataImport.addOperators(operators);
        return dataImport;
    }

    private <T> Optional<T> executeGetRequest(String url, ResponseHandler<Optional<T>> handler) {
        try {
            return Request.Get(url).execute().handleResponse(handler);
        } catch (IOException | InvalidResponseException e) {
            LOGGER.error("Request to " + url + " failed", e);
        } catch (NotFoundResponseException e) {
            LOGGER.info("(404) Not Found at " + url, e);
        }
        return Optional.empty();
    }

    /**
     * Saves input stream from response as temporary jar file
     */
    public static class TempFileResponseHandler implements ResponseHandler<Optional<File>> {

        @Override
        public Optional<File> handleResponse(HttpResponse response) throws IOException {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                File tempFile = File.createTempFile("testware-", ".jar");
                try (InputStream content = response.getEntity().getContent();
                     FileOutputStream tmpFileStream = new FileOutputStream(tempFile)) {
                    ByteStreams.copy(content, tmpFileStream);
                }
                return Optional.of(tempFile);
            }
            if (statusCode == 404) {
                throw new NotFoundResponseException();
            }
            throw new InvalidResponseException("Response returned " + statusCode + " status code");
        }
    }

    public static class StringResponseHandler implements ResponseHandler<Optional<String>> {

        @Override
        public Optional<String> handleResponse(HttpResponse response) throws IOException {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                try (InputStream is = response.getEntity().getContent()) {
                    return Optional.of(IOUtils.toString(is));
                }
            }
            if (statusCode == 404) {
                return Optional.empty();
            }
            throw new InvalidResponseException("Response returned " + statusCode + " status code");
        }
    }

    private static class InvalidResponseException extends RuntimeException {
        public InvalidResponseException(String s) {
            super(s);
        }
    }

    private static class NotFoundResponseException extends RuntimeException {
        public NotFoundResponseException() {
        }
    }
}
