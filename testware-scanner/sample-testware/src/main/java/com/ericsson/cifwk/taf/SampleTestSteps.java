package com.ericsson.cifwk.taf;

import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Shared;
import com.ericsson.cifwk.taf.annotations.TestStep;

@Shared
public class SampleTestSteps {

    @TestStep(id = "sample-test-step-id", description = "Sample test step")
    public void testAction(@Input("param") String param) {
        // no implementation required
    }

    @TestStep(id = "another-sample-test-step-id", description = "Sample test step with two parameters")
    public void anotherTestAction(@Input("firstParameter") String firstParam, @Input("secondParameter") Integer secondParam) {
        // no implementation required
    }

    public void anyPublicMethod(String param) {
        // no implementation required
    }

    private void anyPrivateMethod(String param) {
        // no implementation required
    }
}
