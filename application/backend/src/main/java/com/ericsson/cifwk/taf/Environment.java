/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.taf;

import com.google.common.base.Throwables;
import com.google.common.io.Resources;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

@Singleton
public class Environment {

    private static final Logger LOGGER = LoggerFactory.getLogger(Environment.class);

    private static final String DEFAULT_ENV = "dev";

    public static final String SPARK_PORT = "spark.port";
    public static final String SPARK_JETTY = "spark.jetty";
    public static final String MONGODB_HOST = "mongodb.host";
    public static final String MONGODB_PORT = "mongodb.port";
    public static final String MONGODB_DBNAME = "mongodb.dbname";

    private final String env;

    static {
        System.setProperty("jsse.enableSNIExtension", "false");
    }

    public Environment() {
        String envProperty = System.getenv("env");
        env = envProperty != null ? envProperty : System.getProperty("env", DEFAULT_ENV);
        LOGGER.info("Environment set to " + env);
    }

    public String getName() {
        return env;
    }

    public Properties toProperties() {
        Properties properties = new Properties();
        properties.put("env", env);
        properties.putAll(loadProperties("env/default.properties"));
        properties.putAll(loadProperties("env/" + env + ".properties"));
        return properties;
    }

    private Properties loadProperties(String name) {
        Properties properties = new Properties();
        try {
            URI uri = Resources.getResource(name).toURI();
            FileInputStream stream = new FileInputStream(new File(uri));
            properties.load(stream);
        } catch (IOException | URISyntaxException e) {
            throw Throwables.propagate(e);
        }
        return properties;
    }

}
