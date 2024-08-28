package com.ericsson.cifwk.scanner;

import com.ericsson.cifwk.scanner.model.Argument;
import com.ericsson.cifwk.scanner.model.Gav;
import com.ericsson.cifwk.scanner.model.PublicMethod;
import org.objectweb.asm.Type;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ScannerHelper {

    protected static void visitAllClassFiles(File jarFile, Consumer<InputStream> consumer) {
        try {
            scanJarArchive(jarFile, consumer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void scanJarArchive(File jarFile, Consumer<InputStream> consumer) throws IOException {
        if (!jarFile.getName().toLowerCase().endsWith(".jar")) {
            throw new RuntimeException("It is possible to scan Jar files only  : " + jarFile);
        }
        Gav gav = null;
        try (ZipFile zipFile = new ZipFile(jarFile.getAbsolutePath())) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                if (zipEntry.getName().endsWith(".class") && !zipEntry.isDirectory()) {
                    try (InputStream inputStream = zipFile.getInputStream(zipEntry)) {
                        consumer.accept(inputStream);
                    }
                } else if(zipEntry.getName().endsWith("pom.xml") && zipEntry.getName().startsWith("META-INF")) {
                    try (InputStream inputStream = zipFile.getInputStream(zipEntry)) {
                        gav = GavExtractor.get(inputStream);
                    }
                }
            }
        }
        // Should be called in the end
        consumer.accept(gav);
    }

    protected static void applyMethodParameters(PublicMethod publicMethod, String methodDescriptor) {
        Type[] argumentTypes = Type.getArgumentTypes(methodDescriptor);
        Type returnType = Type.getReturnType(methodDescriptor);
        publicMethod.setReturnType(returnType.getClassName());

        List<Argument> arguments = publicMethod.getArguments();
        if (argumentTypes.length != arguments.size()) {

            // If any argument was not annotated as @Input then ignore annotations completely
            arguments.clear();
            for (int i = 0; i < argumentTypes.length; i++) {
                arguments.add(new Argument("param" + (i + 1), argumentTypes[i].getClassName()));
            }

            return;
        }

        for (int i = 0; i < argumentTypes.length; i++) {
            Type type = argumentTypes[i];
            Argument argument = arguments.get(i);
            argument.setType(type.getClassName());
        }
    }

    protected interface Consumer<T> {
        void accept(T t);
        void accept(Gav gav);
    }

}
