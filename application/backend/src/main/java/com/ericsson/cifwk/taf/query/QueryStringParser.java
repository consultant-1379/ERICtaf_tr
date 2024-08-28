package com.ericsson.cifwk.taf.query;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryStringParser {

    private static final Pattern QUERY_RE = Pattern.compile(
            String.format("(?<field>(?:\\w+?\\.)*\\w+)(?<operator>%1$s)(?<value>(?:(?!%1$s).)*(?:.(?=&|$)))",
                    Joiner.on("|").join(QueryOperator.values())));

    public List<SearchCriterion> parse(String query) {
        List<SearchCriterion> criteria = Lists.newArrayList();
        Matcher matcher = QUERY_RE.matcher(query);
        while (matcher.find()) {
            String fieldName = matcher.group("field");
            String operatorName = matcher.group("operator");
            String value = matcher.group("value");
            criteria.add(new SearchCriterion(fieldName, operatorName, value));
        }
        return criteria;
    }
}
