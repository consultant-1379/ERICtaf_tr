package com.ericsson.cifwk.taf.service;

import com.ericsson.cifwk.taf.mapping.OperatorMapper;
import com.ericsson.cifwk.taf.mapping.PublicMethodMapper;
import com.ericsson.cifwk.taf.mapping.TestStepMapper;
import com.ericsson.cifwk.taf.model.Operator;
import com.ericsson.cifwk.taf.model.TestStep;
import com.ericsson.cifwk.taf.model.Testware;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.FileFilter;
import java.net.URISyntaxException;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JarFileScannerTest {

    @InjectMocks
    private JarFileScanner service;

    @Mock
    private PublicMethodMapper publicMethodMapper;
    @Mock
    private TestStepMapper testStepMapper;
    @Mock
    private OperatorMapper operatorMapper;

    @Mock
    private Testware testware;
    @Mock
    private TestStep testStep;

    private File artifactJar;

    @Before
    public void setUp() throws URISyntaxException {
        when(testware.getId()).thenReturn("tw-id");

        System.out.println(new File("../../testware-scanner/sample-testware/target").getAbsolutePath());

        FileFilter fileFilter = file -> file.getName().contains(".jar");
        File[] file = new File("../../testware-scanner/sample-testware/target").listFiles(fileFilter);
        artifactJar = file[0];
    }

    @Test
    public void testScanForOperators() {
        List<Operator> operators = service.scanForSharedOperators(artifactJar);
        assertThat(operators, hasSize(2));
    }

    @Test
    public void testScanForTestSteps() {
        List<TestStep> testSteps = service.scanForTestSteps(artifactJar);
        assertThat(testSteps, hasSize(2));
    }
}
