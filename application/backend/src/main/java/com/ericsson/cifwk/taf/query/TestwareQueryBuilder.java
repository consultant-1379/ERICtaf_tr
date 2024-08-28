package com.ericsson.cifwk.taf.query;

import com.ericsson.cifwk.taf.model.Testware;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.mongodb.morphia.query.Criteria;
import org.mongodb.morphia.query.Query;

import java.util.Arrays;
import java.util.List;

public class TestwareQueryBuilder extends QueryBuilder {

    public void addAnyTestwareFieldLikeFilter(Query<Testware> query, String searchTerm) {
        if (Strings.isNullOrEmpty(searchTerm)) {
            return;
        }
        String decodedSearchTerm = decodeQuery(searchTerm);
        List<Criteria> criteriaList = Lists.newArrayList();
        for (TestwareQueryField qf : TestwareQueryField.values()) {
            Criteria criteria = query.criteria(qf.actualField()).containsIgnoreCase(decodedSearchTerm);
            criteriaList.add(criteria);
        }
        Criteria[] criteriaArray = criteriaList.toArray(new Criteria[criteriaList.size()]);
        query.or(criteriaArray);
    }

    @Override
    protected String convertQueryFieldToActualField(String queryField) {
        return Arrays.stream(TestwareQueryField.values())
                .filter(f -> f.queryStringField().equalsIgnoreCase(queryField))
                .findFirst().get()
                .actualField();
    }

}
