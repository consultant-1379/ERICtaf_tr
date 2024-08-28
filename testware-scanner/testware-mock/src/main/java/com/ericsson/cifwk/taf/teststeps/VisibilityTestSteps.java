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

import com.ericsson.cifwk.taf.annotations.Shared;
import com.ericsson.cifwk.taf.annotations.TestStep;

@Shared
public class VisibilityTestSteps {

    @TestStep(id = "SAMPLE1", description = "Test Step description 1")
    public void makePublic() {
        // implementation is not required
    }

    @TestStep(id = "SAMPLE1", description = "Test Step description 1")
    protected void makeProtected() {
        // implementation is not required
    }

    @TestStep(id = "SAMPLE1", description = "Test Step description 1")
    void makeDefault() {
        // implementation is not required
    }

    @TestStep(id = "SAMPLE1", description = "Test Step description 1")
    private void makePrivate() { //NOSONAR
        // implementation is not required
    }

}
