package com.spkiddai.memoryserver.Test;

import com.spkiddai.memoryserver.Shell.ValveShell;
import org.apache.catalina.Valve;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.valves.ValveBase;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ValveTest extends ValveBase {

    // Valve的核心方法，用于处理请求
    public void invoke(Request request, Response response) throws IOException, ServletException {
        response.getWriter().println("ValveTest Registration Example");
        this.getNext().invoke(request, response);
    }

    /**
     * 向Tomcat注册Valve
     *
     * @param request 当前的HttpServletRequest请求对象
     */
    public void addValve(HttpServletRequest request) throws Exception {
        ServletContext servletContext = request.getServletContext();
        StandardContext context = getContext(servletContext);

        // 创建ValveTest实例
        Valve valvetest = new ValveTest();

        // 添加Valve
        context.addValve(valvetest);
    }

    /**
     * 从Tomcat中删除指定的Valve。
     *
     * @param request 当前的HttpServletRequest请求对象
     */
    public void rmValve(HttpServletRequest request) throws Exception {
        ServletContext servletContext = request.getServletContext();
        StandardContext context = getContext(servletContext);

        // 获取pipeline和valves
        Method getPipelineMethod = context.getClass().getMethod("getPipeline");
        Object pipeline = getPipelineMethod.invoke(context);
        Method getValvesMethod = pipeline.getClass().getMethod("getValves");
        Object[] valves = (Object[]) getValvesMethod.invoke(pipeline);

        for (Object valve : valves) {
            if ((valve instanceof ValveTest)||(valve instanceof ValveShell)) {

                // 从pipeline中删除指定的Valve
                Method removeValveMethod = pipeline.getClass().getMethod("removeValve", Valve.class);
                removeValveMethod.invoke(pipeline, valve);
            }
        }
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
