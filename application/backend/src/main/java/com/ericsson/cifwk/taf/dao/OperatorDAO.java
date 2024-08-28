package com.ericsson.cifwk.taf.dao;

import com.ericsson.cifwk.taf.model.Operator;
import com.google.common.collect.Lists;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.aggregation.AggregationPipeline;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.MorphiaIterator;
import org.mongodb.morphia.query.Query;

import javax.inject.Inject;
import java.util.List;

import static org.mongodb.morphia.aggregation.Sort.ascending;

public class OperatorDAO extends BasicDAO<Operator, String> {

    private static final String OPERATOR_NAME = "name";

    @Inject
    public OperatorDAO(Datastore datastore) {
        super(datastore);
    }

    public List<Operator> findAll(Query query) {
        AggregationPipeline<Operator, Operator> pipeline = getDs().<Operator, Operator>createAggregation(Operator.class)
                .match(query)
                .sort(ascending(OPERATOR_NAME));

        MorphiaIterator<Operator, Operator> operatorIterator = pipeline.aggregate(Operator.class);
        return Lists.newArrayList(operatorIterator.iterator());
    }

}
