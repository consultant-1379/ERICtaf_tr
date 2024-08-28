package com.ericsson.cifwk.taf.service;

import com.ericsson.cifwk.scanner.SuitesScanner;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class SuitesScannerTest {

    File artifactJar;

    SuitesScanner service = new SuitesScanner();

    @Before
    public void setUp() throws URISyntaxException {
        System.out.println(new File("../../testware-scanner/sample-testware/target").getAbsolutePath());

        FileFilter fileFilter = file -> file.getName().contains(".jar");
        File[] file = new File("../../testware-scanner/sample-testware/target").listFiles(fileFilter);
        artifactJar = file[0];
    }

    @Test
    public void testScanSuites() throws IOException {
        List<String> strings = service.scanSuites(artifactJar);

        assertThat(strings.size(), is(3));
        assertThat(strings, containsInAnyOrder("DataDriven_Scenarios.xml", "TAF_UI_SDK.xml", "TransactionWithData.xml"));
    }
}
