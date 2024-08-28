/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.taf.teststeps;

import com.ericsson.cifwk.taf.TestResult;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Shared;
import com.ericsson.cifwk.taf.annotations.TestStep;

import java.util.Date;

@Shared
public class SpecificTestSteps implements CommonTestSteps {

    @TestStep(id = "SAMPLE1", description = "Test Step description 1")
    public void testStep1(@Input("param") String param) {
        // implementation is not required
    }

    @TestStep(id = "SAMPLE2", description = "Test Step description 2")
    public int testStep2(@Input("data1") String param1, @Input("data2") String param2) {
        return 1;
    }

    @TestStep(id = "SAMPLE3", description = "Test Step description 3")
    public TestResult testStep3(int arg1, Date arg2) {
        return new TestResult();
    }

    @Override
    public void globalAction(String param) {
        // This method SHOULD NOT BE published by scanner for TestSteps
    }
}
