package com.ericsson.cifwk.taf.query;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class QueryStringParserTest {

    private static final String QUERY = "?groupId=com.ericsson.taf&version!=0.0.1&artifactId~someJar";

    private static final String NONSENSE_QUERY = "?tabitha/4&nonsense*rubbish&key1=jee";

    private static final String EMPTY_QUERY = "";

    private QueryStringParser queryStringParser = new QueryStringParser();

    @Test
    public void shouldParseQueryString() {
        List<SearchCriterion> criteria = queryStringParser.parse(QUERY);
        assertThat(criteria.size(), equalTo(3));

        assertThat(criteria.get(0).getField(), equalTo("groupId"));
        assertThat(criteria.get(0).getOperator(), equalTo(QueryOperator.EQUAL.syntax()));
        assertThat(criteria.get(0).getValue(), equalTo("com.ericsson.taf"));

        assertThat(criteria.get(1).getField(), equalTo("version"));
        assertThat(criteria.get(1).getOperator(), equalTo(QueryOperator.NOT_EQUAL.syntax()));
        assertThat(criteria.get(1).getValue(), equalTo("0.0.1"));

        assertThat(criteria.get(2).getField(), equalTo("artifactId"));
        assertThat(criteria.get(2).getOperator(), equalTo(QueryOperator.CONTAINS.syntax()));
        assertThat(criteria.get(2).getValue(), equalTo("someJar"));
    }

    @Test
    public void shouldIgnoreIncorrectOperators() {
        List<SearchCriterion> criteria = queryStringParser.parse(NONSENSE_QUERY);
        assertThat(criteria.size(), equalTo(1));
        assertThat(criteria.get(0).getField(), equalTo("key1"));
        assertThat(criteria.get(0).getOperator(), equalTo(QueryOperator.EQUAL.syntax()));
        assertThat(criteria.get(0).getValue(), equalTo("jee"));
    }

    @Test
    public void shouldNotCreateSearchCriterionForEmptyString() {
        List<SearchCriterion> criteria = queryStringParser.parse(EMPTY_QUERY);
        assertThat(criteria.size(), equalTo(0));
    }
}
