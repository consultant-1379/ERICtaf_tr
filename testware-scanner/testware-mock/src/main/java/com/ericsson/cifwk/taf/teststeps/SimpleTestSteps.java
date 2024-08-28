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

import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Shared;
import com.ericsson.cifwk.taf.annotations.TestStep;

@Shared
public class SimpleTestSteps {

    @TestStep(id = "WITH_NAMED_PARAM", description = "With named param")
    public String withInputParam(@Input("param") String inputParam) {
        // implementation is not required
        return "";
    }

    @TestStep(id = "WITH_SIMPLE_PARAM", description = "With simple param")
    public String withSimpleParam(String simpleParam) {
        // implementation is not required
        return "";
    }

    public void nonTestStep() {
        // This method SHOULD NOT BE published by scanner
    }

}
