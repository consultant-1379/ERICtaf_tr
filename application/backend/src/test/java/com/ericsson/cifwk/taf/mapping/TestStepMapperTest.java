package com.ericsson.cifwk.taf.mapping;

import com.ericsson.cifwk.scanner.model.Argument;
import com.ericsson.cifwk.taf.model.Parameter;
import com.ericsson.cifwk.taf.model.TestStep;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestStepMapperTest {

    TestStepMapper mapper;

    @Mock
    com.ericsson.cifwk.scanner.model.TestStep testStep;

    @Mock
    Argument argument;

    @Before
    public void setUp() {
        mapper = new TestStepMapper();

        when(testStep.getName()).thenReturn("TestStepName");
        when(testStep.getClassName()).thenReturn("SharedTestSteps");
        when(testStep.getDescription()).thenReturn("Description");
        when(testStep.getReturnType()).thenReturn("String");

        when(testStep.getArguments()).thenReturn(Lists.newArrayList(argument));
        when(argument.getType()).thenReturn("ArgType");
        when(argument.getName()).thenReturn("ArgName");
    }

    @Test
    public void testMap() {
        TestStep mappedStep = mapper.map(testStep);

        assertThat(mappedStep.getId(), notNullValue());
        assertThat(mappedStep.getName(), is(testStep.getName()));
        assertThat(mappedStep.getComponent(), is(testStep.getClassName()));
        assertThat(mappedStep.getDescription(), is(testStep.getDescription()));
        assertThat(mappedStep.getReturnType(), is(testStep.getReturnType()));

        List<Parameter> attributes = mappedStep.getAttributes();
        assertThat(attributes.size(), is(1));
        assertThat(attributes.get(0).getType(), is(argument.getType()));
        assertThat(attributes.get(0).getName(), is(argument.getName()));
    }
}
