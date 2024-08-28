package com.ericsson.cifwk.scanner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class SuitesScanner {

    public List<String> scanSuites(File jar) throws IOException {
        List<String> suites = new ArrayList<>();

        if (jar != null) {
            try (JarFile jarFile = new JarFile(jar)) {
                Enumeration<JarEntry> entries = jarFile.entries();

                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();

                    String name = jarEntry.getName();
                    if (name.contains("suites/") && name.contains(".xml")) {
                        String suiteName = name.replace("suites/", "");
                        suites.add(suiteName);
                    }
                }
            }
        }
        return suites;
    }
}
