package com.spkiddai.memoryserver.Tools;

import com.sun.tools.attach.VirtualMachine;
import sun.misc.Unsafe;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class InjectAgent {

    public void reflectInject(String arg, String agentFilePath) {
        try {
            // 确保agent文件存在
            String javaAgent = new File(agentFilePath).getCanonicalPath();

            this.enableAttachSelf();

            // 加载VirtualMachine类
            Class<?> VirtualMachineCls = ClassLoader.getSystemClassLoader().loadClass("com.sun.tools.attach.VirtualMachine");

            // 获取当前JVM的进程ID
            String pid = getCurrentPID(); // 你需要实现这个方法来获取当前JVM的PID

            // 反射调用VirtualMachine的attach方法
            Method attachMethod = VirtualMachineCls.getMethod("attach", String.class);
            Object vm = attachMethod.invoke(null, pid);

            // 反射调用VirtualMachine的loadAgent方法
            Method loadAgentMethod = VirtualMachineCls.getMethod("loadAgent", String.class, String.class);
            loadAgentMethod.invoke(vm, javaAgent, arg);

            // 反射调用VirtualMachine的detach方法
            Method detachMethod = VirtualMachineCls.getMethod("detach");
            detachMethod.invoke(vm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getCurrentPID() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        return pid;
    }

    private void enableAttachSelf() {
        try {
            Unsafe unsafe = null;

            try {
                Field field = Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                unsafe = (Unsafe)field.get((Object)null);
            } catch (Exception var6) {
                throw new AssertionError(var6);
            }

            Class cls = Class.forName("sun.tools.attach.HotSpotVirtualMachine");
            Field field = cls.getDeclaredField("ALLOW_ATTACH_SELF");
            long fieldAddress = unsafe.staticFieldOffset(field);
            unsafe.putBoolean(cls, fieldAddress, true);
        } catch (Throwable var7) {
        }
    }

    public void inject(String arg ,String agentFilePath){
        try {
            String javaAgent = new File(agentFilePath).getCanonicalPath();

            this.enableAttachSelf();

            String pid = getCurrentPID();

            VirtualMachine vm = VirtualMachine.attach(pid);
            // 在此处执行所需的操作，例如加载一个代理
            vm.loadAgent(javaAgent, arg);
            // 断开连接
            vm.detach();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
