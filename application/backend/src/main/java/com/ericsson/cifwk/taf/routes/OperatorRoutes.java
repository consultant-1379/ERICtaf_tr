package com.ericsson.cifwk.taf.routes;

import com.ericsson.cifwk.taf.JsonTransformer;
import com.ericsson.cifwk.taf.dao.OperatorDAO;
import com.ericsson.cifwk.taf.model.Operator;
import com.ericsson.cifwk.taf.query.OperatorQueryBuilder;
import com.google.common.net.MediaType;
import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;

import javax.inject.Inject;

import static com.ericsson.cifwk.taf.TestRegistryApplication.ROUTE_CONTEXT;
import static com.ericsson.cifwk.taf.routes.Headers.*;
import static spark.Spark.*;

public class OperatorRoutes {

    public static final String ROUTE_OPERATOR = "/operators/";

    public static final String SEARCH_QUERY = "search/query";

    public static final String SEARCH_ANY = "search/any";

    @Inject
    private OperatorDAO operatorDAO;
    @Inject
    OperatorQueryBuilder queryBuilder;

    @Inject
    private Gson gson;

    public void configure() { //NOSONAR
        JsonTransformer transformer = new JsonTransformer();
        get(ROUTE_CONTEXT + ROUTE_OPERATOR, MediaType.JSON_UTF_8.toString(), (request, response) -> {
            String artifacts = request.queryParams("search");
            if (artifacts == null) {
                return operatorDAO.find().asList();
            }

            Query query = operatorDAO.createQuery();
            queryBuilder.addFilters(query, request.queryString());
            response.header(HEADER_CONTENT_TYPE, APPLICATION_JSON);
            return operatorDAO.findAll(query);
        }, transformer);

        post(ROUTE_CONTEXT + ROUTE_OPERATOR, MediaType.JSON_UTF_8.toString(), (request, response) -> {
            Operator resource = gson.fromJson(request.body(), Operator.class);
            resource.setId(ObjectId.get().toString());
            operatorDAO.save(resource);
            response.status(HTTP_STATUS_CREATED);
            response.header(HEADER_CONTENT_LOCATION, "/registry/api/operators/" + resource.getId());
            return "";
        });

        get(ROUTE_CONTEXT + ROUTE_OPERATOR + ":id", MediaType.JSON_UTF_8.toString(), (request, response) -> {
            response.header(HEADER_CONTENT_TYPE, APPLICATION_JSON);
            return operatorDAO.findOne("id", request.params("id"));
        }, transformer);

        put(ROUTE_CONTEXT + ROUTE_OPERATOR + ":id", MediaType.JSON_UTF_8.toString(), (request, response) -> {
            Operator resource = gson.fromJson(request.body(), Operator.class);
            resource.setId(request.params("id"));
            operatorDAO.save(resource);
            response.status(HTTP_STATUS_NO_CONTENT);
            return "";
        });

        get(ROUTE_CONTEXT + ROUTE_OPERATOR + SEARCH_QUERY, MediaType.JSON_UTF_8.toString(), (request, response) -> {
            Query query = operatorDAO.createQuery();
            queryBuilder.addFilters(query, request.queryString());
            response.header(HEADER_CONTENT_TYPE, APPLICATION_JSON);
            return operatorDAO.findAll(query);
        }, transformer);

        get(ROUTE_CONTEXT + ROUTE_OPERATOR + SEARCH_ANY, MediaType.JSON_UTF_8.toString(), (request, response) -> {
            Query<Operator> query = operatorDAO.createQuery();
            queryBuilder.addAnyOperatorFieldLikeFilter(query, request.queryString());
            response.header(HEADER_CONTENT_TYPE, APPLICATION_JSON);
            return operatorDAO.findAll(query);
        }, transformer);

        delete(ROUTE_CONTEXT + ROUTE_OPERATOR + ":id", MediaType.JSON_UTF_8.toString(), (request, response) -> {
            operatorDAO.delete(operatorDAO.findOne("id", request.params("id")));
            response.status(HTTP_STATUS_NO_CONTENT);
            return "";
        });
    }

}
