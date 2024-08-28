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
public class ExtendedTestSteps extends BaseTestSteps {

    @Override
    @TestStep(id = "SAMPLE_EXTENDED", description = "Test Step extended")
    public ExtendedTestSteps extend(@Input("argExtended") String arg) {
        return null;
    }

    @TestStep(id = "SAMPLE2", description = "Test Step description 2")
    public void doSomething(@Input("arg2") String arg) {
        // implementation is not required
    }

}
