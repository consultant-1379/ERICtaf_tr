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

import com.ericsson.cifwk.taf.dao.TestwareDAO;
import com.ericsson.cifwk.taf.model.Testware;
import com.github.fakemongo.Fongo;
import com.github.fakemongo.junit.FongoRule;
import com.google.common.base.Joiner;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.ericsson.cifwk.taf.TestRegistryApplication.ROUTE_CONTEXT;
import static com.ericsson.cifwk.taf.routes.TestwareRoutes.*;
import static com.jayway.restassured.RestAssured.*;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class TestwareRestApiTest {

    private static final String OWNER = "?owner~ejhnhng";
    private static final String OWNER_GROUP_ID = "?owner~ejhnhng&groupId=com.ericsson.cifwk";
    private static final String ARTIFACT_ID_SUITES_COMPONENT = "?artifactId=tm-tests&suites=suite4.xml&component~Rest";
    private static final String VERSION_DESCRIPTION = "?version=1.1.5&description~Creates a new";
    private static final String NO_RESULT = "?owner=ejhnhng&gitLocation=git.com";
    private static final String OWNER_ARTIFACT_NOT_EQUAL = "?owner!=egergle&owner!=ejhnhng&artifactId!=tm-tests";

    private static Gson gson;

    @ClassRule
    public static FongoRule fongoRule = new FongoRule();
    private static TestBackend backend;

    @BeforeClass
    public static void init() {
        System.setProperty("env", "devTest");

        Fongo fongo = fongoRule.getFongo();
        MongoClient mongoClient = fongo.getMongo();
        backend = new TestBackend(mongoClient);
        backend.init();

        gson = backend.getInjector().getInstance(Gson.class);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 5678;
    }

    @Before
    public void clearDB() {
        TestwareDAO testwareDAO = backend.getInjector().getInstance(TestwareDAO.class);
        testwareDAO.deleteByQuery(testwareDAO.createQuery());
    }

    @Test
    public void testFormat() {
        Date date = gson.fromJson("\"Thu Oct 30 10:12:30 EET 2014\"", Date.class);
        GregorianCalendar calendar = new GregorianCalendar(Locale.UK);
        calendar.setTime(date);
        assertEquals(30, calendar.get(Calendar.DATE));
        assertEquals(10, calendar.get(Calendar.MONTH) + 1);
        assertEquals(2014, calendar.get(Calendar.YEAR));
        // hour can vary in different time zone. Jenkins has another time zone.
//        assertEquals(10, calendar.get(Calendar.HOUR));
        assertEquals(12, calendar.get(Calendar.MINUTE));
        assertEquals(30, calendar.get(Calendar.SECOND));
    }

    @Test
    public void shouldCheckTestware_Create() {
        String created = createTestware();

        Response response = when().get(created);
        response.then()
                .body("id", notNullValue())
                .body("artifactId", equalTo("art"))
                .statusCode(200);
    }

    @Test
    public void shouldCheckTestware_Get() {
        get(ROUTE_CONTEXT + ROUTE_TESTWARE).then().statusCode(200);
    }

    @Test
    public void shouldCheckTestwareList_Get() throws Exception {
        populateTestwareWithEmbeddedTestStepsList();

        Response response = get(ROUTE_CONTEXT + ROUTE_TESTWARE);
        response.then()
                .body("size()", equalTo(15))
                .statusCode(200);
    }

    @Test
    public void shouldCheckTestwareList_SearchSingleTestwares() throws Exception {
        populateTestwareWithEmbeddedTestStepsList();

        Response response = get(ROUTE_CONTEXT + ROUTE_TESTWARE + "?search=com.ericsson.cifwk:tm-tests:1.0.1");
        response.then()
                .body("size()", equalTo(1))
                .statusCode(200);
    }

    @Test
    public void shouldCheckTestwareList_SearchManyTestwares() throws Exception {
        populateTestwareWithEmbeddedTestStepsList();

        Response response = get(ROUTE_CONTEXT + ROUTE_TESTWARE + "?search=com.ericsson.cifwk:tm-tests:1.0.1,com.ericsson.cifwk:tm-tests:1.0.2");
        response.then()
                .body("size()", equalTo(2))
                .statusCode(200);
    }

    @Test
    public void shouldCheckTestwareList_SearchManyTestwaresWithWrongGAV() throws Exception {
        populateTestwareWithEmbeddedTestStepsList();

        Response response = get(ROUTE_CONTEXT + ROUTE_TESTWARE + "?search=com.ericsson.cifwk:tm-tests:1.0.1,com.ericsson.cifwk:1.0.2,com.ericsson.cifwk:tm-tests:1.0.2");
        response.then()
                .body("size()", equalTo(2))
                .statusCode(200);
    }

    @Test
    public void shouldSearchByOwner() throws Exception {
        populateTestwareWithEmbeddedTestStepsList();
        verifySearchQuery(OWNER, 2, new String[]{"id_5", "id_6"});
    }

    @Test
    public void shouldSearchByOwnerAndGroupId() throws Exception {
        populateTestwareWithEmbeddedTestStepsList();
        verifySearchQuery(OWNER_GROUP_ID, 2, new String[]{"id_5", "id_6"});
    }

    @Test
    public void shouldSearchByArtifactIdSuitesAndComponent() throws Exception {
        populateTestwareWithEmbeddedTestStepsList();
        verifySearchQuery(ARTIFACT_ID_SUITES_COMPONENT, 1, new String[]{"id_5"});
    }

    @Test
    public void shouldSearchByVersionAndDescription() throws Exception {
        populateTestwareWithEmbeddedTestStepsList();
        verifySearchQuery(VERSION_DESCRIPTION, 1, new String[]{"id_7"});
    }

    @Test
    public void shouldReturnNoResult() throws Exception {
        populateTestwareWithEmbeddedTestStepsList();
        verifySearchQuery(NO_RESULT, 0, new String[]{});
    }

    @Test
    public void shouldSearchByOwnerAndArtifactId() throws Exception {
        populateTestwareWithEmbeddedTestStepsList();
        verifySearchQuery(OWNER_ARTIFACT_NOT_EQUAL, 2, new String[]{"id_7", "id_8"});
    }

    @Test
    public void shouldSearchAnyFieldFor() throws Exception{
        populateTestwareWithEmbeddedTestStepsList();

        // owners
        verifySearchQuery("?eariid1", 3, new String[]{"id_5", "id_7", "id_8"});

        //gitLocation
        verifySearchQuery("?ERICtaf_tr", 3, new String[]{"id_5", "id_7", "id_8"});

        // testStep context
        verifySearchQuery("?API", 3, new String[]{"id_5", "id_7", "id_8"});
    }

    private void verifySearchQuery(String searchQuery, int expectedSize, String[] expectedItems) {
        Response response = get(ROUTE_CONTEXT + ROUTE_TESTWARE + SEARCH_QUERY + searchQuery);
        JsonPath jsonPath = new JsonPath(response.asString());
        List<String> ids = jsonPath.get("id");
        assertThat(ids, hasSize(expectedSize));
        assertThat(ids, hasItems(expectedItems));
    }

    private void populateTestwareWithEmbeddedTestStepsList() throws IOException, URISyntaxException {
        Testware[] testware = gson.fromJson(getJsonString("data/embedded-test-steps.json").replaceAll("_id", "id"), Testware[].class);
        TestwareDAO testwareDAO = backend.getInjector().getInstance(TestwareDAO.class);
        for (Testware testwareObj : testware) {
            testwareDAO.save(testwareObj);
        }
    }

    private String getJsonString(String resourceName) throws IOException, URISyntaxException {
        URL resource = Resources.getResource(resourceName);
        List<String> strings = Files.readAllLines(Paths.get(resource.toURI()));
        return Joiner.on("").join(strings);
    }

    private String createTestware() {
        return given()
                .body("{'groupId':'g.r.o.u.p', 'artifactId':'art', 'version':'1.2.3'}")
                .contentType("application/json")
                .when()
                .post(ROUTE_CONTEXT + ROUTE_TESTWARE)
                .then()
                .statusCode(201)
                .header("Content-Location", notNullValue())
                .extract()
                .header("Content-Location");
    }

}
