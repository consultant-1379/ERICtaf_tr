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

import com.ericsson.cifwk.taf.model.DataImport;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import javax.inject.Inject;

public class DataImportDAO extends BasicDAO<DataImport, String> {

    @Inject
    public DataImportDAO(Datastore datastore) {
        super(datastore);
    }

}
