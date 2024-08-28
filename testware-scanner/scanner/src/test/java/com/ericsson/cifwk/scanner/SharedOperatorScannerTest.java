package com.ericsson.cifwk.scanner;

import com.ericsson.cifwk.scanner.model.Argument;
import com.ericsson.cifwk.scanner.model.PublicMethod;
import com.ericsson.cifwk.scanner.model.SharedOperator;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.ericsson.cifwk.scanner.ScannerTestUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class SharedOperatorScannerTest {

    private SharedOperator specificOperator;
    private SharedOperator visibilityOperator;
    private SharedOperator baseOperator;
    private SharedOperator extendedOperator;
    private SharedOperator multipleContextOperator;

    @Before
    public void setUp() {
        SharedOperatorScanner scanner = new SharedOperatorScanner();

        List<SharedOperator> sharedOperators = scanner.scan(getTestware());
        Collections.sort(sharedOperators);
        assertEquals(5, sharedOperators.size());

        Iterator<SharedOperator> iterator = sharedOperators.iterator();
        baseOperator = iterator.next();
        extendedOperator = iterator.next();
        multipleContextOperator = iterator.next();
        specificOperator = iterator.next();
        visibilityOperator = iterator.next();
    }

    @Test
    public void specificOperator() {
        assertEquals("com.ericsson.cifwk.taf.operators.SpecificOperator", specificOperator.getName());

        assertGav(specificOperator.getGav());

        assertThat(specificOperator.getContext().size(), is(1));
        assertThat(specificOperator.getContext(), hasItem("REST"));

        List<PublicMethod> publicMethods = specificOperator.getPublicMethods();
        assertEquals(4, publicMethods.size());

        Collections.sort(publicMethods);
        Iterator<PublicMethod> iterator = publicMethods.iterator();
        assertMethod(iterator, "globalMethod", "void", new Argument("param1", "java.lang.String"));
        assertMethod(iterator, "testStep1", "void", new Argument("param", "java.lang.String"));
        assertMethod(iterator, "testStep2", "int", new Argument("data1", "java.lang.String"), new Argument("data2", "java.lang.String"));
        assertMethod(iterator, "testStep3", "com.ericsson.cifwk.taf.TestResult", new Argument("param1", "int"), new Argument("param2", "java.util.Date"));
    }

    @Test
    public void visibilityOperator() {
        List<PublicMethod> publicMethods = visibilityOperator.getPublicMethods();

        assertGav(visibilityOperator.getGav());

        assertEquals(1, publicMethods.size());
        PublicMethod publicMethod = publicMethods.iterator().next();
        assertEquals("makePublic", publicMethod.getName());
    }

    @Test
    public void baseOperator() {
        assertEquals("com.ericsson.cifwk.taf.operators.BaseOperator", baseOperator.getName());

        assertGav(baseOperator.getGav());

        assertThat(baseOperator.getContext().size(), is(1));
        assertThat(baseOperator.getContext(), hasItem("UNKNOWN"));

        List<PublicMethod> publicMethods = baseOperator.getPublicMethods();
        assertEquals(2, publicMethods.size());

        Collections.sort(publicMethods);
        Iterator<PublicMethod> iterator = publicMethods.iterator();
        assertMethod(iterator, "extend", "com.ericsson.cifwk.taf.operators.BaseOperator", new Argument("arg1", "java.lang.String"));
        assertMethod(iterator, "login", "void");
    }

    @Test
    public void extendedOperator() {
        assertEquals("com.ericsson.cifwk.taf.operators.ExtendedOperator", extendedOperator.getName());

        assertGav(extendedOperator.getGav());

        assertThat(extendedOperator.getContext().size(), is(1));
        assertThat(extendedOperator.getContext(), hasItem("REST"));

        List<PublicMethod> publicMethods = extendedOperator.getPublicMethods();
        assertEquals(3, publicMethods.size());
        // TODO: should contain public methods

        Collections.sort(publicMethods);
        Iterator<PublicMethod> iterator = publicMethods.iterator();
        assertMethod(iterator, "doSomething", "void", new Argument("arg2", "java.lang.String"));
        assertMethod(iterator, "extend", "com.ericsson.cifwk.taf.operators.ExtendedOperator", new Argument("argExtended", "java.lang.String"));
        assertMethod(iterator, "publicAction", "void");
    }

    @Test
    public void multipleContextOperator() {
        assertEquals("com.ericsson.cifwk.taf.operators.MultipleContextOperator", multipleContextOperator.getName());

        assertGav(multipleContextOperator.getGav());

        assertThat(multipleContextOperator.getContext().size(), is(2));
        assertThat(multipleContextOperator.getContext(), hasItems("REST", "API"));

        assertThat(multipleContextOperator.getPublicMethods().size(), is(2));
    }

}
