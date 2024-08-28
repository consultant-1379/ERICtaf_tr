/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.taf.dao;

import com.ericsson.cifwk.taf.model.Testware;
import com.google.common.collect.Lists;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.aggregation.AggregationPipeline;
import org.mongodb.morphia.aggregation.Group;
import org.mongodb.morphia.aggregation.Projection;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.MorphiaIterator;
import org.mongodb.morphia.query.Query;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import static org.mongodb.morphia.aggregation.Group.*;
import static org.mongodb.morphia.aggregation.Projection.projection;
import static org.mongodb.morphia.aggregation.Sort.ascending;

public class TestwareDAO extends BasicDAO<Testware, String> {

    private static final String TESTWARE_GROUP_ID = "groupId";
    private static final String TESTWARE_PUBLISHED_AT = "publishedAt";
    private static final String TESTWARE_ARTIFACT_ID = "artifactId";
    private static final String TESTWARE_VERSION = "version";
    private static final String TESTWARE_JAVA_DOC_LOCATION = "javaDocLocation";
    private static final String TESTWARE_GIT_LOCATION = "gitLocation";
    private static final String TESTWARE_POM_LOCATION = "pomLocation";
    private static final String TESTWARE_OWNERS = "owners";
    private static final String TESTWARE_DESCRIPTION = "description";
    private static final String TESTWARE_IDS = "ids";
    private static final String TESTWARE_VERSIONS = "versions";
    private static final String TESTWARE_TAF_VERSION = "tafVersion";
    private static final String TESTWARE_TEST_STEPS_COUNT = "testStepsCount";
    private static final String TEMPORARY_ID = "id";
    private static final String TESTWARE_ID = "_id";

    @Inject
    public TestwareDAO(Datastore datastore) {
        super(datastore);
    }

    public List<Testware> filterAndFindLatest(Query query) {
        AggregationPipeline<Testware, Testware> pipeline = getDs().<Testware, Testware>createAggregation(Testware.class)
                .match(query)
                .sort(ascending(TESTWARE_PUBLISHED_AT))
                .group(
                        id(grouping(TESTWARE_GROUP_ID), grouping(TESTWARE_ARTIFACT_ID)),
                        groups()
                )
                .project(projections())
                .sort(ascending(TESTWARE_GROUP_ID), ascending(TESTWARE_ARTIFACT_ID));

        MorphiaIterator<Testware, Testware> testwareIterator = pipeline.aggregate(Testware.class);
        List<Testware> testwareList = Lists.newArrayList(testwareIterator.iterator());
        return testwareList.stream()
                .filter(tw -> tw.getTestStepsCount() > 0)
                .collect(Collectors.toList());
    }

    public boolean exists(Testware testware) {
        Testware savedTestware = createQuery()
                .filter("groupId", testware.getGroupId())
                .filter("artifactId", testware.getArtifactId())
                .filter("version", testware.getVersion())
                .get();

        return savedTestware != null;
    }

    private Group[] groups() {
        Group[] groups = {
                grouping(TEMPORARY_ID, last(TESTWARE_ID)),
                grouping(TESTWARE_ARTIFACT_ID, last(TESTWARE_ARTIFACT_ID)),
                grouping(TESTWARE_GROUP_ID, last(TESTWARE_GROUP_ID)),
                grouping(TESTWARE_VERSION, last(TESTWARE_VERSION)),
                grouping(TESTWARE_JAVA_DOC_LOCATION, last(TESTWARE_JAVA_DOC_LOCATION)),
                grouping(TESTWARE_GIT_LOCATION, last(TESTWARE_GIT_LOCATION)),
                grouping(TESTWARE_POM_LOCATION, last(TESTWARE_POM_LOCATION)),
                grouping(TESTWARE_OWNERS, last(TESTWARE_OWNERS)),
                grouping(TESTWARE_PUBLISHED_AT, last(TESTWARE_PUBLISHED_AT)),
                grouping(TESTWARE_DESCRIPTION, last(TESTWARE_DESCRIPTION)),
                grouping(TESTWARE_TAF_VERSION, last(TESTWARE_TAF_VERSION)),
                grouping(TESTWARE_IDS, push(TESTWARE_ID)),
                grouping(TESTWARE_VERSIONS, push(TESTWARE_VERSION)),
                grouping(TESTWARE_TEST_STEPS_COUNT, last(TESTWARE_TEST_STEPS_COUNT))
        };
        return groups;
    }

    private Projection[] projections() {
        Projection[] projections = {
                projection(TESTWARE_ID, TEMPORARY_ID),
                projection(TESTWARE_ARTIFACT_ID),
                projection(TESTWARE_GROUP_ID),
                projection(TESTWARE_VERSION),
                projection(TESTWARE_JAVA_DOC_LOCATION),
                projection(TESTWARE_GIT_LOCATION),
                projection(TESTWARE_POM_LOCATION),
                projection(TESTWARE_OWNERS),
                projection(TESTWARE_PUBLISHED_AT),
                projection(TESTWARE_DESCRIPTION),
                projection(TESTWARE_IDS),
                projection(TESTWARE_VERSIONS),
                projection(TESTWARE_TAF_VERSION),
                projection(TESTWARE_TEST_STEPS_COUNT)
        };
        return projections;
    }
}
