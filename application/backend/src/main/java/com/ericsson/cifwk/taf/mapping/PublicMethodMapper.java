package com.ericsson.cifwk.taf.mapping;

import com.ericsson.cifwk.scanner.model.Argument;
import com.ericsson.cifwk.taf.model.TestStep;

import java.util.UUID;

public class PublicMethodMapper {

    public TestStep map(com.ericsson.cifwk.scanner.model.PublicMethod publicMethod) {
        TestStep testStepEntity = new TestStep();

        testStepEntity.setId(UUID.randomUUID().toString());
        testStepEntity.setName(publicMethod.getName());
        testStepEntity.setReturnType(publicMethod.getReturnType());
        testStepEntity.setComponent(publicMethod.getClassName());

        for (Argument argument : publicMethod.getArguments()) {
            com.ericsson.cifwk.taf.model.Parameter parameterEntity = new com.ericsson.cifwk.taf.model.Parameter();
            parameterEntity.setType(argument.getType());
            parameterEntity.setName(argument.getName());
            testStepEntity.getAttributes().add(parameterEntity);
        }
        return testStepEntity;
    }
}
