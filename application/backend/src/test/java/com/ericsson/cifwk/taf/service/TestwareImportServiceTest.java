package com.ericsson.cifwk.taf.service;

import com.ericsson.cifwk.taf.dao.DataImportDAO;
import com.ericsson.cifwk.taf.dao.OperatorDAO;
import com.ericsson.cifwk.taf.dao.TestwareDAO;
import com.ericsson.cifwk.taf.mapping.TestStepMapper;
import com.ericsson.cifwk.taf.mapping.TestwareMapper;
import com.ericsson.cifwk.taf.model.DataImport;
import com.ericsson.cifwk.taf.model.Operator;
import com.ericsson.cifwk.taf.model.TestStep;
import com.ericsson.cifwk.taf.model.Testware;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;


@RunWith(MockitoJUnitRunner.class)
public class TestwareImportServiceTest {

    @Spy
    TestStepMapper testStepMapper = new TestStepMapper();

    @Spy
    TestwareMapper testwareMapper = new TestwareMapper();

    @InjectMocks
    TestwareImportService testwareService;

    @Mock
    Testware validTestware;

    @Mock
    Testware invalidTestware;

    @Mock
    DataImportDAO dataImportDAO;

    @Mock
    TestwareDAO testwareDAO;

    @Mock
    OperatorDAO operatorDAO;

    @Mock
    TestStep testStep;

    @Mock
    Operator operator;

    @Before
    public void setUp() throws URISyntaxException, IOException {
        when(validTestware.getGroupId()).thenReturn("com.ericsson.cifwk");
        when(validTestware.getArtifactId()).thenReturn("tr-sample-testware");
        when(validTestware.getVersion()).thenReturn("1.0.10");

        List<TestStep> testSteps = Lists.newArrayList(testStep);
        when(validTestware.getTestSteps()).thenReturn(testSteps);
    }

    @Test
    public void testImportTestwareData() {
        List<Operator> operators = Lists.newArrayList(operator);
        testwareService.importTestwareData(validTestware, operators);

        verify(testwareDAO).save(validTestware);

        ArgumentCaptor<DataImport> importCaptor = ArgumentCaptor.forClass(DataImport.class);

        verify(dataImportDAO, times(1)).save(importCaptor.capture());

        DataImport dataImport = importCaptor.getValue();
        assertThat(dataImport.getId(), notNullValue());
        assertThat(dataImport.getTimestamp(), notNullValue());
        assertThat(dataImport.getSource(), notNullValue());
        assertThat(dataImport.getUser(), notNullValue());

        assertThat(dataImport.getTestware(), is(validTestware));
        assertThat(dataImport.getOperators().size(), is(1));
        assertThat(dataImport.getOperators().get(0), is(operator));
    }
}
