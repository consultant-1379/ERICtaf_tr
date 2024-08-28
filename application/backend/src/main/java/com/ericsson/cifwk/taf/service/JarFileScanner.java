package com.ericsson.cifwk.taf.service;

import com.ericsson.cifwk.scanner.SharedOperatorScanner;
import com.ericsson.cifwk.scanner.SharedTestStepsScanner;
import com.ericsson.cifwk.scanner.model.SharedOperator;
import com.ericsson.cifwk.scanner.model.SharedTestSteps;
import com.ericsson.cifwk.taf.mapping.OperatorMapper;
import com.ericsson.cifwk.taf.mapping.TestStepMapper;
import com.ericsson.cifwk.taf.model.Operator;
import com.ericsson.cifwk.taf.model.TestStep;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class JarFileScanner {

    @Inject
    private TestStepMapper testStepMapper;

    @Inject
    private OperatorMapper operatorMapper;

    public List<Operator> scanForSharedOperators(File jar) {
        List<SharedOperator> sharedOperators = new SharedOperatorScanner().scan(jar);

        return sharedOperators.stream()
                .map(sharedOperator -> operatorMapper.map(sharedOperator))
                .collect(Collectors.toList());
    }

    public List<TestStep> scanForTestSteps(File jar) {
        List<TestStep> testSteps = Lists.newArrayList();
        List<SharedTestSteps> sharedTestStepsList = new SharedTestStepsScanner().scan(jar);

        for (SharedTestSteps sharedTestSteps : sharedTestStepsList) {
            testSteps.addAll(sharedTestSteps.getTestSteps().stream()
                    .map(testStep -> testStepMapper.map(testStep))
                    .collect(Collectors.toList()));
        }
        return testSteps;
    }
}
