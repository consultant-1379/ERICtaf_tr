package com.ericsson.cifwk.taf.routes;

import com.ericsson.cifwk.taf.JsonTransformer;
import com.ericsson.cifwk.taf.dao.TestwareDAO;
import com.ericsson.cifwk.taf.model.GAV;
import com.ericsson.cifwk.taf.model.Testware;
import com.ericsson.cifwk.taf.query.TestwareQueryBuilder;
import com.ericsson.cifwk.taf.service.TestwareScanService;
import com.google.common.net.MediaType;
import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;

import javax.inject.Inject;
import java.util.List;

import static com.ericsson.cifwk.taf.TestRegistryApplication.ROUTE_CONTEXT;
import static com.ericsson.cifwk.taf.routes.Headers.*;
import static spark.Spark.*;

public class TestwareRoutes {

    public static final String ROUTE_TESTWARE = "/testware/";

    public static final String SEARCH_QUERY = "search/query";

    public static final String SEARCH_ANY = "search/any";

    @Inject
    TestwareDAO testwareDAO;
    @Inject
    TestwareScanService testwareScanService;
    @Inject
    TestwareQueryBuilder queryBuilder;

    @Inject
    Gson gson;

    public void configure() { //NOSONAR
        JsonTransformer transformer = new JsonTransformer();
        get(ROUTE_CONTEXT + ROUTE_TESTWARE, MediaType.JSON_UTF_8.toString(), (request, response) -> {
            response.header(HEADER_CONTENT_TYPE, APPLICATION_JSON);

            String artifacts = request.queryParams("search");
            if (artifacts == null) {
                return testwareDAO.find().asList();
            }

            List<GAV> gavs = testwareScanService.convertToGAVs(artifacts);
            List<Testware> testwareList = testwareScanService.findTestwaresEagerly(gavs);
            if (testwareList.isEmpty()) {
                response.status(404);
                return null;
            } else {
                return testwareList;
            }
        }, transformer);

        post(ROUTE_CONTEXT + ROUTE_TESTWARE, MediaType.JSON_UTF_8.toString(), (request, response) -> {
            Testware resource = gson.fromJson(request.body(), Testware.class);
            resource.setId(ObjectId.get().toString());
            testwareDAO.save(resource);
            response.status(HTTP_STATUS_CREATED);
            response.header(HEADER_CONTENT_LOCATION, "/registry/api/testware/" + resource.getId());
            return "";
        });

        get(ROUTE_CONTEXT + ROUTE_TESTWARE + ":id", MediaType.JSON_UTF_8.toString(), (request, response) -> {
            response.header(HEADER_CONTENT_TYPE, APPLICATION_JSON);
            return testwareDAO.findOne("id", request.params("id"));
        }, transformer);

        put(ROUTE_CONTEXT + ROUTE_TESTWARE + ":id", MediaType.JSON_UTF_8.toString(), (request, response) -> {
            Testware resource = gson.fromJson(request.body(), Testware.class);
            resource.setId(request.params("id"));
            testwareDAO.save(resource);
            response.status(HTTP_STATUS_NO_CONTENT);
            return "";
        });

        get(ROUTE_CONTEXT + ROUTE_TESTWARE + SEARCH_QUERY, MediaType.JSON_UTF_8.toString(), (request, response) -> {
            Query query = testwareDAO.createQuery();
            queryBuilder.addFilters(query, request.queryString());
            response.header(HEADER_CONTENT_TYPE, APPLICATION_JSON);
            return testwareDAO.filterAndFindLatest(query);
        }, transformer);

        get(ROUTE_CONTEXT + ROUTE_TESTWARE + SEARCH_ANY, MediaType.JSON_UTF_8.toString(), (request, response) -> {
            Query<Testware> query = testwareDAO.createQuery();
            queryBuilder.addAnyTestwareFieldLikeFilter(query, request.queryString());
            response.header(HEADER_CONTENT_TYPE, APPLICATION_JSON);
            return testwareDAO.filterAndFindLatest(query);
        }, transformer);

        delete(ROUTE_CONTEXT + ROUTE_TESTWARE + ":id", MediaType.JSON_UTF_8.toString(), (request, response) -> {
            testwareDAO.delete(testwareDAO.findOne("id", request.params("id")));
            response.status(HTTP_STATUS_NO_CONTENT);
            return "";
        });

    }
}
