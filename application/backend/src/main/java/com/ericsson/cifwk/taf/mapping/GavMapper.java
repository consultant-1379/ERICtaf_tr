package com.ericsson.cifwk.taf.mapping;

import com.ericsson.cifwk.scanner.model.Gav;
import com.ericsson.cifwk.taf.model.GavInfo;

public class GavMapper {

    public GavInfo map(Gav gav) {
        return new GavInfo(gav.getGroupId(), gav.getArtifactId(), gav.getVersion());
    }

}
