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

import com.github.fakemongo.Fongo;
import com.google.common.base.Joiner;
import com.google.common.io.Resources;
import com.mongodb.BasicDBList;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ApplicationDevStart {

    public static void main(String[] args) throws Exception {
        System.setProperty("env", "local");

        Fongo fongo = new Fongo("test");
        MongoClient mongoClient = fongo.getMongo();
        TestBackend backend = new TestBackend(mongoClient, true);
        backend.init();

        DBObject[] testware = getDbObjects("data/embedded-test-steps.json");
        DBObject[] operators = getDbObjects("data/operators.json");

        DB db = mongoClient.getDB("test-steps");
        db.getCollection("testware").insert(testware);
        db.getCollection("operators").insert(operators);
    }

    private static DBObject[] getDbObjects(String resourceName) throws IOException, URISyntaxException {
        URL resource = Resources.getResource(resourceName);
        List<String> strings = Files.readAllLines(Paths.get(resource.toURI()));
        BasicDBList parsed = (BasicDBList) JSON.parse(Joiner.on("\n").join(strings));
        return parsed.toArray(new DBObject[parsed.size()]);
    }

}
