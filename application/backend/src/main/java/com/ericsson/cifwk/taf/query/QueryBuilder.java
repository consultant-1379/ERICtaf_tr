package com.ericsson.cifwk.taf.query;

import com.google.common.base.Strings;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Optional;

public abstract class QueryBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryBuilder.class);

    @Inject
    private QueryStringParser parser;

    public void addFilters(Query query, String queryString) {
        if (Strings.isNullOrEmpty(queryString)) {
            return;
        }
        String decodedQuery = decodeQuery(queryString);
        List<SearchCriterion> criteria = parser.parse(decodedQuery);
        criteria.forEach(criterion -> addAppropriateFilter(query, criterion));
    }

    private void addAppropriateFilter(Query query, SearchCriterion searchCriterion) {
        String actualField = convertQueryFieldToActualField(searchCriterion.getField());
        String operator = searchCriterion.getOperator();
        String value = searchCriterion.getValue();

        if (operator.equals(QueryOperator.EQUAL.syntax())) {
            query.criteria(actualField).equal(value);
        } else if (operator.equals(QueryOperator.NOT_EQUAL.syntax())) {
            query.criteria(actualField).notEqual(value);
        } else if (operator.equals(QueryOperator.CONTAINS.syntax())) {
            query.criteria(actualField).containsIgnoreCase(value);
        }
    }

    protected abstract String convertQueryFieldToActualField(String queryField);

    protected String decodeQuery(String encodedString) {
        Optional<String> decoded = Optional.empty();
        try {
            decoded = Optional.of(URLDecoder.decode(encodedString, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Could not decode criterion value");
        }
        return decoded.get();
    }

}
