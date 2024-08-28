package com.ericsson.cifwk.taf.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "latest-testware")
public class ArtifactItems {
    @XmlElement(name = "testwareArtifact")
    private List<GAV> artifacts;

    public List<GAV> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<GAV> artifacts) {
        this.artifacts = artifacts;
    }
}
