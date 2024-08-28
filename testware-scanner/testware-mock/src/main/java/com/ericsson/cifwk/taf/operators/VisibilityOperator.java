/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.taf.operators;

import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.annotations.Shared;

@Shared
@Operator
public class VisibilityOperator {

    public void makePublic() {
        // implementation is not required
    }

    protected void makeProtected() {
        // implementation is not required
    }

    void makeDefault() {
        // implementation is not required
    }

    private void makePrivate() { //NOSONAR
        // implementation is not required
    }

}
