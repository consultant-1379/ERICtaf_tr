package com.ericsson.cifwk.taf.mapping;

import com.ericsson.cifwk.taf.model.TestStep;

public class TestStepMapper extends PublicMethodMapper {

    public TestStep map(com.ericsson.cifwk.scanner.model.TestStep testStep) {
        TestStep testStepEntity = super.map(testStep);
        testStepEntity.setDescription(testStep.getDescription());
        return testStepEntity;
    }
}
