package com.ericsson.cifwk.scanner;

import com.ericsson.cifwk.scanner.model.Argument;
import com.ericsson.cifwk.scanner.model.Gav;
import com.ericsson.cifwk.scanner.model.PublicMethod;
import com.ericsson.cifwk.scanner.model.TestStep;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ScannerTestUtils {

    protected static File getTestware() {
        File targetFolder = new File("../testware-mock/target/");
        return targetFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("tr-testware-mock");
            }
        })[0];
    }

    protected static void assertGav(Gav gav) {
        assertNotNull(gav);

        assertThat(gav.getGroupId(), is("com.ericsson.cifwk"));
        assertThat(gav.getArtifactId(), is("tr-testware-mock"));
        // TODO: Use for local testing
//        assertThat(gav.getVersion(), containsString("-SNAPSHOT"));
    }

    protected static void assertMethod(Iterator<PublicMethod> iterator, String name, String returnType, Argument... arguments) {
        PublicMethod publicMethod = iterator.next();

        assertEquals(name, publicMethod.getName());
        assertEquals(returnType, publicMethod.getReturnType());
        assertArguments(publicMethod, arguments);
    }

    protected static void assertTestStep(Iterator<TestStep> iterator, String actionOrder, String returnType, Argument... arguments) {
        String id = "SAMPLE" + actionOrder;
        String description = "Test Step description " + actionOrder;
        String name = "testStep" + actionOrder;
        assertTestStep(iterator, id, name, description, returnType, arguments);
    }

    protected static void assertTestStep(Iterator<TestStep> iterator, String id, String name, String description, String returnType, Argument... arguments) {
        TestStep testStep = iterator.next();
        assertEquals(id, testStep.getId());
        assertEquals(description, testStep.getDescription());
        assertEquals(name, testStep.getName());
        assertEquals(returnType, testStep.getReturnType());
        assertArguments(testStep, arguments);
    }

    private static void assertArguments(PublicMethod publicMethod, Argument[] arguments) {
        List<Argument> actualArguments = publicMethod.getArguments();
        assertEquals(arguments.length, actualArguments.size());
        for (int i = 0; i < arguments.length; i++) {
            assertArgument(arguments[i], actualArguments.get(i));
        }
    }

    private static void assertArgument(Argument expectedArgument, Argument actualArgument) {
        assertEquals(expectedArgument.getName(), actualArgument.getName());
        assertEquals(expectedArgument.getType(), actualArgument.getType());
    }
}
