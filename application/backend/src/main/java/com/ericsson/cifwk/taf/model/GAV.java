package com.ericsson.cifwk.taf.model;

import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "testwareArtifact")
public class GAV {

    @XmlElement
    private String groupId;
    @XmlElement
    private String artifactId;
    @XmlElement
    private String version;
    @XmlElement
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date dateCreated;
    @XmlElement
    private String testPom;

    public GAV() {
    }

    public GAV(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getTestPom() {
        return testPom;
    }

    public void setTestPom(String testPom) {
        this.testPom = testPom;
    }

    public boolean isValid() {
        return groupId != null &&
                artifactId != null &&
                version != null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GAV gav = (GAV) o;

        return new EqualsBuilder()
                .append(artifactId, gav.artifactId)
                .append(groupId, gav.groupId)
                .append(version, gav.version)
                .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(groupId)
                .append(artifactId)
                .append(version).build();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("groupId", groupId)
                .add("artifactId", artifactId)
                .add("version", version)
                .toString();
    }

    private static class DateAdapter extends XmlAdapter<String, Date> {

        private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public String marshal(Date v) throws Exception {
            return dateFormat.format(v);
        }

        @Override
        public Date unmarshal(String v) throws Exception {
            return dateFormat.parse(v);
        }
    }
}
