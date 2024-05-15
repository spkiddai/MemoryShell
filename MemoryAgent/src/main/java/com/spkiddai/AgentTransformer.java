package com.spkiddai;

import javassist.*;
import org.objectweb.asm.*;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class AgentTransformer implements ClassFileTransformer {


    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        className = className.replace("/", ".");
        if ("com.spkiddai.memoryserver.Servlet.HelloServlet".equals(className)) {
            classfileBuffer = modifyBytecode(className, classBeingRedefined);
            return  ASMBytecode(classfileBuffer);
        } else if (className.startsWith("org.apache.catalina.core.StandardContext")) { // 仅对特定包下的类进行修改
            return addListenerAgent(className, classBeingRedefined);
        } else if ("com.sun.tools.attach.VirtualMachine".equals(className)) {
            return  attachLog(className, classBeingRedefined);
        } else if ("sun.instrument.InstrumentationImpl".equals(className)) {
            return  classLog(className, classBeingRedefined);
        }
        return null;
    }

    private static byte[] classLog(String className, Class<?> classBeingRedefined) {
        try {
            ClassPool classPool = ClassPool.getDefault();
            ClassClassPath classPath = new ClassClassPath(classBeingRedefined);
            classPool.insertClassPath(classPath);
            CtClass ctClass = classPool.get(className);
            CtMethod[] methods = ctClass.getDeclaredMethods();
            for (CtMethod method : methods) {
                if (method.getName().equals("retransformClasses")) {
                    method.insertBefore("{ System.out.println(\"Java Agent Inject: retransformClasses\"); }");
                }
                if (method.getName().equals("redefineClasses")) {
                    method.insertBefore("{ System.out.println(\"Java Agent Inject: redefineClasses\"); }");
                }
            }
            byte[] byteCode = ctClass.toBytecode();
            ctClass.detach();
            return byteCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static byte[] attachLog(String className, Class<?> classBeingRedefined) {
        try {
            ClassPool classPool = ClassPool.getDefault();
            ClassClassPath classPath = new ClassClassPath(classBeingRedefined);
            classPool.insertClassPath(classPath);
            CtClass ctClass = classPool.get(className);

            // 监控 attach 方法
            try {
                CtMethod attachMethod = ctClass.getDeclaredMethod("attach",
                        new CtClass[]{classPool.get("java.lang.String")});
                attachMethod.insertBefore("{ System.out.println(\"Java Agent Inject: attach\"); }");
            } catch (NotFoundException e) {
                System.out.println("attach method not found.");
            }
            byte[] byteCode = ctClass.toBytecode();
            ctClass.detach();
            return byteCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static byte[] addListenerAgent(String className, Class<?> classBeingRedefined) {
        try {
            ClassPool classPool = ClassPool.getDefault();
            ClassClassPath classPath = new ClassClassPath(classBeingRedefined);
            classPool.insertClassPath(classPath);
            CtClass ctClass = classPool.get(className);
            CtMethod[] methods = ctClass.getDeclaredMethods();

            for (CtMethod method : methods) {
                if (method.getName().equals("addApplicationEventListener")) {
                    method.insertBefore(
                    "StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();" +
                        "String callingClass = null;" +
                        "for (int i = 0; i < stackTrace.length; i++) {" +
                        "    if (\"org.apache.catalina.core.StandardContext\".equals(stackTrace[i].getClassName()) " +
                        "        && \"addApplicationEventListener\".equals(stackTrace[i].getMethodName())) {" +
                        "        callingClass = stackTrace[i + 1].getClassName();" +
                        "        break;" +
                        "    }" +
                        "}" +
                        "System.out.println(\"Suspicious call to addApplicationEventListener from \" + callingClass);"
                    );
                }
            }
            byte[] byteCode = ctClass.toBytecode();
            ctClass.detach();
            return byteCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用 Javassist 来修改特定类的字节码。这个方法尝试查找名为"doGet"的方法，并修改其实现。
     *
     * @param className           要修改的类的完全限定类名。
     * @param classBeingRedefined 如果是被重定义或重新转换的类，则为非null；否则为null。
     * @return 修改后的字节码或null如果字节码没有被修改。
     */
    private static byte[] modifyBytecode(String className, Class<?> classBeingRedefined) {
        try {
            // 初始化 Javassist 的类池
            ClassPool classPool = ClassPool.getDefault();

            // 添加当前正在被转换或重定义的类的类路径到类池
            ClassClassPath classPath = new ClassClassPath(classBeingRedefined);
            classPool.insertClassPath(classPath);

            // 获取要修改的 CtClass 对象
            CtClass ctClass = classPool.get(className);

            // 获取 ctClass 中所有声明的方法
            CtMethod[] methods = ctClass.getDeclaredMethods();

            for (CtMethod method : methods) {
                // 查找名为"doGet"的方法并在其前面插入代码
                if (method.getName().equals("doGet")) {
                    method.insertBefore("response.getWriter().println(\"JavaAgentTest Registration with Javassist Example\");");
                }
            }

            // 将 CtClass 对象转换为字节码
            byte[] byteCode = ctClass.toBytecode();

            // 释放 CtClass 对象，避免内存泄漏
            ctClass.detach();

            return byteCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 使用ASM库来修改特定类的字节码。
     *
     * @param classfileBuffer 原始的字节码
     * @return 修改后的字节码
     */
    private static byte[] ASMBytecode(byte[] classfileBuffer) {
        try {
            // 使用ASM的ClassReader来读取原始的字节码
            ClassReader classReader = new ClassReader(classfileBuffer);
            // 使用ClassWriter来写入新的字节码，同时自动计算最大堆栈和局部变量表大小
            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
            // 创建一个新的ClassVisitor来修改方法
            ClassVisitor cv = new ClassVisitor(Opcodes.ASM9, classWriter) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                    MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                    // 对"doGet"方法进行特殊处理
                    if (name.equals("doGet")) {
                        return new ResponseWriteAdapter(Opcodes.ASM9, mv);
                    }
                    return mv;
                }
            };
            // 开始访问类
            classReader.accept(cv, 0);
            return classWriter.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 自定义的MethodVisitor，用于修改"doGet"方法的字节码。
     * 此适配器将确保每次调用doGet时都会首先输出"JavaAgentTest Registration Example"。
     */
    static class ResponseWriteAdapter extends MethodVisitor {
        public ResponseWriteAdapter(int api, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
        }

        @Override
        public void visitCode() {
            mv.visitCode();

            // 修改doGet方法的字节码，使其在方法开始时输出JavaAgentTest Registration Example
            mv.visitVarInsn(Opcodes.ALOAD, 2); // 加载HttpServletResponse参数到操作数栈
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "javax/servlet/http/HttpServletResponse", "getWriter", "()Ljava/io/PrintWriter;", true);
            mv.visitLdcInsn("JavaAgentTest Registration with ASM Example"); // 加载常量字符串到操作数栈
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintWriter", "println", "(Ljava/lang/String;)V", false);
            super.visitCode();
        }
    }


}