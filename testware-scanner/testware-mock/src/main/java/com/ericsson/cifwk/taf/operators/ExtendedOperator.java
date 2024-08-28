/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.taf.operators;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.annotations.Shared;
import com.ericsson.cifwk.taf.annotations.TestStep;

@Shared
@Operator(context = Context.REST)
public class ExtendedOperator extends BaseOperator {

    @Override
    @TestStep(id = "SAMPLE_EXTENDED", description = "Test Step extended")
    public ExtendedOperator extend(@Input("argExtended") String arg) {
        return null;
    }

    @TestStep(id = "SAMPLE2", description = "Test Step description 2")
    public void doSomething(@Input("arg2") String arg) {
        // implementation is not required
    }

    public void publicAction() {
        // This method SHOULD BE published by scanner as well
    }

}
