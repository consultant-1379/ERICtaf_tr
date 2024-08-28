package com.ericsson.cifwk.scanner;

import com.ericsson.cifwk.scanner.model.Argument;
import com.ericsson.cifwk.scanner.model.SharedTestSteps;
import com.ericsson.cifwk.scanner.model.TestStep;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.ericsson.cifwk.scanner.ScannerTestUtils.*;
import static org.junit.Assert.*;

public class SharedTestStepsScannerTest {

    private SharedTestSteps baseTestSteps;
    private SharedTestSteps extendedTestSteps;
    private SharedTestSteps simpleTestSteps;
    private SharedTestSteps specificTestSteps;
    private SharedTestSteps visibilityTestSteps;

    @Before
    public void setUp() {
        SharedTestStepsScanner scanner = new SharedTestStepsScanner();

        List<SharedTestSteps> sharedTestStepsList = scanner.scan(getTestware());
        Collections.sort(sharedTestStepsList);
        assertEquals(5, sharedTestStepsList.size());

        Iterator<SharedTestSteps> iterator = sharedTestStepsList.iterator();
        baseTestSteps = iterator.next();
        extendedTestSteps = iterator.next();
        simpleTestSteps = iterator.next();
        specificTestSteps = iterator.next();
        visibilityTestSteps = iterator.next();
    }

    @Test
    public void baseTestSteps() {
        assertEquals("com.ericsson.cifwk.taf.teststeps.BaseTestSteps", baseTestSteps.getName());

        assertGav(baseTestSteps.getGav());

        List<TestStep> testSteps = baseTestSteps.getTestSteps();
        assertEquals(2, testSteps.size());

        Collections.sort(testSteps);
        Iterator<TestStep> iterator = testSteps.iterator();
        Argument argument = new Argument("arg1", "java.lang.String");
        assertTestStep(iterator, "SAMPLE1", "extend", "Test Step description 1", "com.ericsson.cifwk.taf.teststeps.BaseTestSteps", argument);
        assertTestStep(iterator, "LOGIN", "login", "Login description", "void");
    }

    @Test
    public void extendedTestSteps() {
        assertEquals("com.ericsson.cifwk.taf.teststeps.ExtendedTestSteps", extendedTestSteps.getName());

        assertGav(extendedTestSteps.getGav());

        List<TestStep> testSteps = extendedTestSteps.getTestSteps();
        assertEquals(2, testSteps.size());

        Collections.sort(testSteps);
        Iterator<TestStep> iterator = testSteps.iterator();
        Argument sample2Arg = new Argument("arg2", "java.lang.String");
        assertTestStep(iterator, "SAMPLE2", "doSomething", "Test Step description 2", "void", sample2Arg);
        Argument extendedArg = new Argument("argExtended", "java.lang.String");
        assertTestStep(iterator, "SAMPLE_EXTENDED", "extend", "Test Step extended", "com.ericsson.cifwk.taf.teststeps.ExtendedTestSteps", extendedArg);
    }

    @Test
    public void simpleTestSteps() {
        assertEquals("com.ericsson.cifwk.taf.teststeps.SimpleTestSteps", simpleTestSteps.getName());

        assertGav(simpleTestSteps.getGav());

        List<TestStep> testSteps = simpleTestSteps.getTestSteps();
        assertEquals(2, testSteps.size());

        Collections.sort(testSteps);
        Iterator<TestStep> iterator = testSteps.iterator();
        assertTestStep(iterator, "WITH_NAMED_PARAM", "withInputParam", "With named param", "java.lang.String", new Argument("param", "java.lang.String"));
        assertTestStep(iterator, "WITH_SIMPLE_PARAM", "withSimpleParam", "With simple param", "java.lang.String", new Argument("param1", "java.lang.String"));
    }

    @Test
    public void specificTestSteps() {
        assertEquals("com.ericsson.cifwk.taf.teststeps.SpecificTestSteps", specificTestSteps.getName());

        assertGav(specificTestSteps.getGav());

        List<TestStep> testSteps = specificTestSteps.getTestSteps();
        assertEquals(3, testSteps.size());

        Collections.sort(testSteps);
        Iterator<TestStep> iterator = testSteps.iterator();
        assertTestStep(iterator, "1", "void", new Argument("param", "java.lang.String"));
        assertTestStep(iterator, "2", "int", new Argument("data1", "java.lang.String"), new Argument("data2", "java.lang.String"));
        assertTestStep(iterator, "3", "com.ericsson.cifwk.taf.TestResult", new Argument("param1", "int"), new Argument("param2", "java.util.Date"));
    }

    @Test
    public void visibilityTestSteps() {
        assertEquals("com.ericsson.cifwk.taf.teststeps.VisibilityTestSteps", visibilityTestSteps.getName());

        assertGav(visibilityTestSteps.getGav());

        List<TestStep> testSteps = visibilityTestSteps.getTestSteps();
        assertEquals(1, testSteps.size());

        Iterator<TestStep> iterator = testSteps.iterator();
        assertTestStep(iterator, "SAMPLE1", "makePublic", "Test Step description 1", "void");
    }

}
