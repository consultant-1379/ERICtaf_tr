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

import com.ericsson.cifwk.taf.TestResult;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.annotations.Shared;

import java.util.Date;

@Shared
@Operator(context = Context.REST)
public class SpecificOperator implements CommonOperator {

    public void testStep1(@Input("param") String param) {
        // implementation is not required
    }

    public int testStep2(@Input("data1") String param1, @Input("data2") String param2) {
        return 1;
    }

    public TestResult testStep3(int arg1, Date arg2) {
        return new TestResult();
    }

    @Override
    public void globalMethod(String param) {
        // This method SHOULD BE published by scanner as well
    }
}
