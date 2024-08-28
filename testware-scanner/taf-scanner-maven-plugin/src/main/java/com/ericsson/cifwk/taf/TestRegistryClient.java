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


import com.ericsson.cifwk.taf.model.DataImport;
import com.ericsson.cifwk.taf.model.HasId;
import com.ericsson.cifwk.taf.model.Operator;
import com.ericsson.cifwk.taf.model.Testware;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Throwables;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.maven.plugin.logging.Log;

import java.io.IOException;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;

public class TestRegistryClient {

    public static final String ROUTE_CONTEXT = "/registry/api";

    public static final String ROUTE_TESTWARE = "/testware/";

    public static final String ROUTE_OPERATORS = "/operators/";

    public static final String ROUTE_DATA_IMPORTS = "/data-imports/";

    private static final String HEADER_CONTENT_LOCATION = "Content-Location";

    private static final int HTTP_STATUS_CREATED = 201;

    private HttpClient httpClient = HttpClients.createDefault();

    private Gson gson = new GsonBuilder().setDateFormat("EEE MMM dd kk:mm:ss z yyyy").create();

    private String trHost;

    private Log log;

    public TestRegistryClient(String trHost, Log log) {
        this.trHost = trHost;
        this.log = log;
    }

    public String saveTestware(Testware testware) {
        return postEntity(testware, ROUTE_TESTWARE);
    }

    public void saveOperators(List<Operator> operators) {
        for (Operator operator : operators) {
            postEntity(operator, ROUTE_OPERATORS);
        }
    }

    public void saveImport(DataImport dataImport) {
        postEntity(dataImport, ROUTE_DATA_IMPORTS);
    }

    private String postEntity(HasId<String> entity, String restSuffix) {
        HttpPost httpPost = new HttpPost(trHost + ROUTE_CONTEXT + restSuffix);
        String json = gson.toJson(entity);

        try {
            httpPost.setEntity(new StringEntity(json, "utf-8"));
            HttpResponse httpResponse = httpClient.execute(httpPost);

            String errorMessage = "Test Registry didn't return 201 HTTP status code";
            checkState(HTTP_STATUS_CREATED == httpResponse.getStatusLine().getStatusCode(), errorMessage);
            String createdResourceUrl = httpResponse.getFirstHeader(HEADER_CONTENT_LOCATION).getValue();
            String id = extractId(createdResourceUrl);
            entity.setId(id);
            log.info(String.format("Entity %s has been saved with id '%s'", entity.getClass().getSimpleName(), id));

            // closing connection
            EntityUtils.consume(httpResponse.getEntity());
            return id;
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    @VisibleForTesting
    protected String extractId(String resourceUrl) {
        return resourceUrl.substring(resourceUrl.lastIndexOf("/") + 1);
    }

}
