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
public class PublicMethodMapperTest {

    PublicMethodMapper mapper;

    @Mock
    com.ericsson.cifwk.scanner.model.PublicMethod publicMethod;

    @Mock
    Argument argument;

    @Before
    public void setUp() {
        mapper = new PublicMethodMapper();

        when(publicMethod.getName()).thenReturn("TestStepName");
        when(publicMethod.getClassName()).thenReturn("SharedOperator");
        when(publicMethod.getReturnType()).thenReturn("String");
        when(publicMethod.getArguments()).thenReturn(Lists.newArrayList(argument));

        when(argument.getType()).thenReturn("ArgType");
        when(argument.getName()).thenReturn("ArgName");
    }

    @Test
    public void testMap() {
        TestStep mappedStep = mapper.map(publicMethod);

        assertThat(mappedStep.getId(), notNullValue());
        assertThat(mappedStep.getName(), is(publicMethod.getName()));
        assertThat(mappedStep.getComponent(), is(publicMethod.getClassName()));
        assertThat(mappedStep.getReturnType(), is(publicMethod.getReturnType()));

        List<Parameter> attributes = mappedStep.getAttributes();
        assertThat(attributes.size(), is(1));
        assertThat(attributes.get(0).getType(), is(argument.getType()));
        assertThat(attributes.get(0).getName(), is(argument.getName()));
    }
}
