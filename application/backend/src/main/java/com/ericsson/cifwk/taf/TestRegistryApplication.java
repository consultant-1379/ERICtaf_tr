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

import com.ericsson.cifwk.taf.dao.DataImportDAO;
import com.ericsson.cifwk.taf.dao.TestwareDAO;
import com.ericsson.cifwk.taf.model.DataImport;
import com.ericsson.cifwk.taf.model.HasId;
import com.ericsson.cifwk.taf.routes.OperatorRoutes;
import com.ericsson.cifwk.taf.routes.TestwareRoutes;
import com.ericsson.cifwk.taf.service.TestwareScanService;
import com.google.common.net.MediaType;
import com.google.gson.Gson;
import com.google.inject.name.Named;
import com.mongodb.MongoClient;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.BasicDAO;

import javax.inject.Inject;

import static com.ericsson.cifwk.taf.routes.Headers.*;
import static spark.Spark.*;

/**
 *
 */
public final class TestRegistryApplication {

    public static final String ROUTE_CONTEXT = "/registry/api";
    public static final String ROUTE_DATA_IMPORTS = "/data-imports/";

    @Inject
    MongoClient mongoClient;

    @Inject
    TestwareRoutes testwareRoutes;

    @Inject
    OperatorRoutes operatorRoutes;

    @Inject
    Gson gson;

    @Inject
    TestwareDAO testwareDAO;

    @Inject
    DataImportDAO dataImportDAO;

    @Inject
    TestwareScanService testwareScanService;

    @Inject
    @Named(Environment.SPARK_PORT)
    String sparkPort;

    @Inject
    @Named(Environment.SPARK_JETTY)
    Boolean sparkJetty;

    @Inject
    public void start() {
        if (Boolean.TRUE.equals(sparkJetty)) {
            setPort(Integer.parseInt(sparkPort));
        }
        configureRoutes();
    }

    private void configureRoutes() {
        JsonTransformer transformer = new JsonTransformer();
        createCrud(dataImportDAO, DataImport.class, ROUTE_CONTEXT + ROUTE_DATA_IMPORTS, transformer);
        testwareRoutes.configure();
        operatorRoutes.configure();
    }

    private <T extends HasId<String>> void createCrud(BasicDAO<T, String> dao, Class<T> type, String path, JsonTransformer transformer) {
        get(path, MediaType.JSON_UTF_8.toString(), (request, response) -> {
            response.header(HEADER_CONTENT_TYPE, APPLICATION_JSON);
            return dao.find().asList();
        }, transformer);

        get(path + ":id", MediaType.JSON_UTF_8.toString(), (request, response) -> {
            response.header(HEADER_CONTENT_TYPE, APPLICATION_JSON);
            return dao.findOne("id", request.params("id"));
        }, transformer);

        put(path + ":id", MediaType.JSON_UTF_8.toString(), (request, response) -> {
            T resource = gson.fromJson(request.body(), type);
            resource.setId(request.params("id"));
            dao.save(resource);
            response.status(HTTP_STATUS_NO_CONTENT);
            return "";
        });

        delete(path + ":id", MediaType.JSON_UTF_8.toString(), (request, response) -> {
            dao.delete(dao.findOne("id", request.params("id")));
            response.status(HTTP_STATUS_NO_CONTENT);
            return "";
        });

        post(path, MediaType.JSON_UTF_8.toString(), (request, response) -> {
            T resource = gson.fromJson(request.body(), type);
            resource.setId(ObjectId.get().toString());
            dao.save(resource);
            response.status(HTTP_STATUS_CREATED);
            response.header(HEADER_CONTENT_LOCATION, path + resource.getId());
            return "";
        });
    }


}
