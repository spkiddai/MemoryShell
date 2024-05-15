package com.spkiddai;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.LoadCompleted;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import org.kohsuke.MetaInfServices;
import javax.annotation.Resource;
import java.lang.instrument.ClassDefinition;

@MetaInfServices(Module.class)
@Information(id = "rasp-check", version = "0.0.1", author = "spkiddai")
public class RaspModule implements Module, LoadCompleted {
    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    @Override
    public void loadCompleted() {
        new EventWatchBuilder(moduleEventWatcher)
                .onClass("com.sun.tools.attach.VirtualMachine")
                .includeBootstrap()
                .onBehavior("attach")
                .withParameterTypes(String.class)
                .onWatch(new AdviceListener() {

                    @Override
                    protected void before(Advice advice) throws Throwable {
                        System.out.println("VirtualMachine Attach PID");
                        printStackTrace();
                    }
                });
        new EventWatchBuilder(moduleEventWatcher)
                .onClass("sun.instrument.InstrumentationImpl")
                .includeBootstrap()
                .onBehavior("redefineClasses")
                .withParameterTypes(ClassDefinition[].class)
                .onWatch(new AdviceListener() {
                    @Override
                    protected void before(Advice advice) throws Throwable {
                        System.out.println("冰蝎内存马");
                        printStackTrace();
                    }
                });
        new EventWatchBuilder(moduleEventWatcher)
                .onClass("sun.instrument.InstrumentationImpl")
                .includeBootstrap()
                .onBehavior("retransformClasses")
                .withParameterTypes(Class[].class)
                .onWatch(new AdviceListener() {
                    @Override
                    protected void before(Advice advice) throws Throwable {
                        System.out.println("JavaAgent内存马");
                        printStackTrace();
                    }
                });
    }

    private void printStackTrace() {
        System.out.println("-------------------");
        System.out.println("堆栈信息");
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTraceElements) {
            System.out.println(element.toString());
        }
        System.out.println("-------------------");
    }
}