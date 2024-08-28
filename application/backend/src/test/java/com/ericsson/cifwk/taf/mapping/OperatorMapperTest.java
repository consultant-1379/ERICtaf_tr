package com.ericsson.cifwk.taf.mapping;

import com.ericsson.cifwk.scanner.model.Gav;
import com.ericsson.cifwk.scanner.model.SharedOperator;
import com.ericsson.cifwk.taf.model.GavInfo;
import com.ericsson.cifwk.taf.model.Operator;
import com.ericsson.cifwk.taf.model.TestStep;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OperatorMapperTest {

    @InjectMocks
    OperatorMapper mapper;

    @Mock
    GavMapper gavMapper;
    @Mock
    PublicMethodMapper publicMethodMapper;
    @Mock
    SharedOperator sharedOperator;
    @Mock
    com.ericsson.cifwk.scanner.model.PublicMethod publicMethod;
    @Mock
    TestStep testStepEntity;
    @Mock
    Gav gav;
    @Mock
    GavInfo gavInfo;

    private String[] contextItems = new String[]{"UI", "REST"};

    @Before
    public void setUp() {
        when(sharedOperator.getName()).thenReturn("OperatorName");
        when(sharedOperator.getContext()).thenReturn(Sets.newHashSet(contextItems));
        when(sharedOperator.getPublicMethods()).thenReturn(Lists.newArrayList(publicMethod));
        when(sharedOperator.getGav()).thenReturn(gav);

        when(publicMethodMapper.map(publicMethod)).thenReturn(testStepEntity);
        when(gavMapper.map(gav)).thenReturn(gavInfo);
    }

    @Test
    public void testMap() {
        Operator result = mapper.map(sharedOperator);

        assertThat(result.getName(), is(sharedOperator.getName()));
        assertThat(result.getContext(), hasItems(contextItems));
        assertThat(result.getPublicMethods(), hasSize(1));
        assertThat(result.getPublicMethods(), hasItems(testStepEntity));
    }

}
