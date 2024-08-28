package com.ericsson.cifwk.scanner;

import com.ericsson.cifwk.scanner.model.Gav;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.InputStream;

public class GavExtractor {

    public static Gav get(InputStream pomXmlStream) {
        PomXml pomXml = JAXB.unmarshal(pomXmlStream, PomXml.class);
        String groupId = pomXml.groupId == null ? pomXml.parent.groupId : pomXml.groupId;
        String artifactId = pomXml.artifactId;
        String version = pomXml.version == null ? pomXml.parent.version : pomXml.version;
        return new Gav(groupId, artifactId, version);
    }

    @XmlRootElement
    private static class PomXml {

        @XmlElement
        private String groupId;

        @XmlElement
        private String artifactId;

        @XmlElement
        private String version;

        @XmlElement
        private ParentPomXml parent;

        private static class ParentPomXml {

            @XmlElement
            private String groupId;

            @XmlElement
            private String artifactId;

            @XmlElement
            private String version;

        }

    }

}
