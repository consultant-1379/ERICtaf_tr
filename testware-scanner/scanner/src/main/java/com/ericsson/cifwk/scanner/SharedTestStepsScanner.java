package com.ericsson.cifwk.scanner;

import com.ericsson.cifwk.scanner.model.Argument;
import com.ericsson.cifwk.scanner.model.Gav;
import com.ericsson.cifwk.scanner.model.SharedTestSteps;
import com.ericsson.cifwk.scanner.model.TestStep;
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

public class SharedTestStepsScanner {

    public static final String OPERATOR_DESCRIPTOR = "Lcom/ericsson/cifwk/taf/annotations/Operator;";

    public static final String SHARED_DESCRIPTOR = "Lcom/ericsson/cifwk/taf/annotations/Shared;";

    public static final String TEST_STEP_DESCRIPTOR = "Lcom/ericsson/cifwk/taf/annotations/TestStep;";

    private static final int ASM_4 = Opcodes.ASM4;

    private List<SharedTestSteps> sharedTestSteps = new ArrayList<>();

    public List<SharedTestSteps> scan(File jarFile) {
        ScannerHelper.visitAllClassFiles(jarFile, new ScannerHelper.Consumer<InputStream>() {
            @Override
            public void accept(InputStream inputStream) {
                ClassReader classReader;
                try {
                    classReader = new ClassReader(inputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                TestStepVisitor testStepVisitor = new TestStepVisitor(sharedTestSteps);
                classReader.accept(testStepVisitor, 0);
            }

            @Override
            public void accept(Gav gav) {
                for (SharedTestSteps sharedTestStep : sharedTestSteps) {
                    sharedTestStep.setGav(gav);
                }
            }
        });
        return sharedTestSteps;
    }

    private static class TestStepVisitor extends ClassVisitor {

        private SharedTestSteps sharedTestSteps;

        private List<SharedTestSteps> sharedTestStepsList;

        private boolean isOperator = false;
        private boolean isShared = false;

        public TestStepVisitor(List<SharedTestSteps> sharedTestStepsList) {
            super(ASM_4);
            this.sharedTestStepsList = sharedTestStepsList;
            sharedTestSteps = new SharedTestSteps();
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            sharedTestSteps.setName(normalizeClassName(name));
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            if (SHARED_DESCRIPTOR.equals(desc)) {
                isShared = true;
            }
            if (OPERATOR_DESCRIPTOR.equals(desc)) {
                isOperator = true;
            }
            return null;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            return new AnnotationScannerMethodVisitor(access, name, desc, sharedTestSteps);
        }

        @Override
        public void visitEnd() {
            if (!isOperator && isShared) {
                sharedTestStepsList.add(sharedTestSteps);
            }
        }

        private String normalizeClassName(String className) {
            return className.replaceAll("/", ".");
        }

    }

    private static class AnnotationScannerMethodVisitor extends MethodVisitor {

        private final SharedTestSteps sharedTestSteps;
        private final TestStep testStep;
        private final String methodDescriptor;
        private final AtomicBoolean isTestStepAnnotated;
        private final AtomicBoolean isTestStepPublic;
        private final AtomicBoolean isNotSuper;

        public AnnotationScannerMethodVisitor(int access, String name, String desc, SharedTestSteps sharedTestSteps) {
            super(ASM_4);
            this.sharedTestSteps = sharedTestSteps;
            testStep = new TestStep(name, sharedTestSteps.getName());
            methodDescriptor = desc;
            isTestStepAnnotated = new AtomicBoolean(false);
            isTestStepPublic = new AtomicBoolean(Modifier.isPublic(access));
            isNotSuper = new AtomicBoolean(!Modifier.isVolatile(access));
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            if (TEST_STEP_DESCRIPTOR.equals(desc)) {
                isTestStepAnnotated.set(true);
                return new AnnotationVisitor(ASM_4) {
                    @Override
                    public void visit(String name, Object value) {
                        if ("id".equals(name)) {
                            testStep.setId(value.toString());
                        }
                        if ("description".equals(name)) {
                            testStep.setDescription(value.toString());
                        }
                    }
                };
            }
            return super.visitAnnotation(desc, visible);
        }

        @Override
        public AnnotationVisitor visitParameterAnnotation(int orderNr, String descriptor, boolean visible) {
            final Argument argument = new Argument();
            testStep.addArgument(argument);
            return new AnnotationVisitor(ASM_4) {
                @Override
                public void visit(String name, Object value) {
                    argument.setName(value.toString());
                }
            };
        }

        @Override
        public void visitEnd() {
            if (isTestStepAnnotated.get() && isTestStepPublic.get() && isNotSuper.get()) {
                applyMethodParameters(testStep, methodDescriptor);
                sharedTestSteps.addTestStep(testStep);
            }
        }

    }

}
