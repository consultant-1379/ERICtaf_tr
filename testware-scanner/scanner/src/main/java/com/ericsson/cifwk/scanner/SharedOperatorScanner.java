/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.scanner;

import com.ericsson.cifwk.scanner.model.Argument;
import com.ericsson.cifwk.scanner.model.Gav;
import com.ericsson.cifwk.scanner.model.PublicMethod;
import com.ericsson.cifwk.scanner.model.SharedOperator;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.ericsson.cifwk.scanner.ScannerHelper.applyMethodParameters;

public class SharedOperatorScanner {

    public static final String OPERATOR_DESCRIPTOR = "Lcom/ericsson/cifwk/taf/annotations/Operator;";

    public static final String SHARED_DESCRIPTOR = "Lcom/ericsson/cifwk/taf/annotations/Shared;";

    private static final int ASM_4 = Opcodes.ASM4;

    private List<SharedOperator> sharedOperators = new ArrayList<>();

    public List<SharedOperator> scan(File jarFile) {
        ScannerHelper.visitAllClassFiles(jarFile, new ScannerHelper.Consumer<InputStream>() {
            @Override
            public void accept(InputStream inputStream) {
                ClassReader classReader;
                try {
                    classReader = new ClassReader(inputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                PublicMethodVisitor publicMethodVisitor = new PublicMethodVisitor(sharedOperators);
                classReader.accept(publicMethodVisitor, 0);
            }

            @Override
            public void accept(Gav gav) {
                for (SharedOperator sharedOperator : sharedOperators) {
                    sharedOperator.setGav(gav);
                }
            }
        });
        return sharedOperators;
    }

    private static class PublicMethodVisitor extends ClassVisitor {

        private SharedOperator operator;

        private List<SharedOperator> operators;

        private boolean isOperator = false;
        private boolean isShared = false;

        public PublicMethodVisitor(List<SharedOperator> operators) {
            super(ASM_4);
            this.operators = operators;
            operator = new SharedOperator();
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            operator.setName(normalizeClassName(name));
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            if (SHARED_DESCRIPTOR.equals(desc)) {
                isShared = true;
            }

            if (OPERATOR_DESCRIPTOR.equals(desc)) {
                isOperator = true;
                return new AnnotationVisitor(ASM_4) {
                    @Override
                    public AnnotationVisitor visitArray(String name) {
                        return new AnnotationVisitor(ASM_4) {
                            @Override
                            public void visit(String name, Object value) {
                                operator.addContext(value.toString());
                            }
                        };
                    }
                };
            }
            return null;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if (isConstructor(name)) {
                return null;
            }
            return new AnnotationScannerMethodVisitor(access, name, desc, operator);
        }

        private boolean isConstructor(String name) {
            return "<init>".equals(name);
        }

        @Override
        public void visitEnd() {
            if (isOperator && isShared) {
                operators.add(operator);
            }
        }

        private String normalizeClassName(String className) {
            return className.replaceAll("/", ".");
        }

    }

    private static class AnnotationScannerMethodVisitor extends MethodVisitor {

        private final SharedOperator sharedOperator;
        private final PublicMethod publicMethod;
        private final String methodDescriptor;
        private final AtomicBoolean isMethodPublic;
        private final AtomicBoolean isNotSuper;

        public AnnotationScannerMethodVisitor(int access, String name, String desc, SharedOperator sharedOperator) {
            super(ASM_4);
            this.sharedOperator = sharedOperator;
            publicMethod = new PublicMethod(name, sharedOperator.getName());
            methodDescriptor = desc;
            isMethodPublic = new AtomicBoolean(Modifier.isPublic(access));
            isNotSuper = new AtomicBoolean(!Modifier.isVolatile(access));
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            return super.visitAnnotation(desc, visible);
        }

        @Override
        public AnnotationVisitor visitParameterAnnotation(int orderNr, String descriptor, boolean visible) {
            final Argument argument = new Argument();
            publicMethod.addArgument(argument);
            return new AnnotationVisitor(ASM_4) {
                @Override
                public void visit(String name, Object value) {
                    argument.setName(value.toString());
                }
            };
        }

        @Override
        public void visitEnd() {
            if (isMethodPublic.get() && isNotSuper.get()) {
                applyMethodParameters(publicMethod, methodDescriptor);
                sharedOperator.addPublicMethod(publicMethod);
            }
        }

    }

}
