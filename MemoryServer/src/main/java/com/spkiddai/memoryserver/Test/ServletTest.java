package com.spkiddai.memoryserver.Test;

import com.spkiddai.memoryserver.Shell.ServletShell;
import org.apache.catalina.Container;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class ServletTest implements Servlet {

    String serveltpattern = "/servlet/index";

    // 初始化方法
    public void init(ServletConfig servletConfig) throws ServletException {
    }

    // 获取ServletConfig配置信息
    public ServletConfig getServletConfig() {
        return null;
    }

    // 提供服务的核心方法
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        servletResponse.getWriter().println("ServletTest Registration Example");
    }

    // 获取Servlet信息
    public String getServletInfo() {
        return null;
    }

    // 销毁方法
    public void destroy() {
    }

    /**
     * 向Tomcat注册Servlet
     *
     * @param request HttpServletRequest请求对象
     */
    public void addServlet(HttpServletRequest request) throws Exception {
        ServletContext servletContext = request.getServletContext();

        if (servletContext.getServletRegistration(this.getClass().getSimpleName()) == null) {
            StandardContext context = getContext(servletContext);

            // 创建ServletTest实例
            Servlet servletClass = new ServletTest();

            // 创建Wrapper，设置属性
            Wrapper wrapper = context.createWrapper();
            wrapper.setName(this.getClass().getSimpleName());
            wrapper.setLoadOnStartup(1);
            wrapper.setServlet(servletClass);
            wrapper.setServletClass(this.toString());

            //添加Wrapper和映射
            context.addChild(wrapper);
            context.addServletMappingDecoded(serveltpattern, this.getClass().getSimpleName());
        }
    }

    /**
     * 从Tomcat中删除指定的Servlet。
     *
     * @param request 当前的HttpServletRequest请求对象
     */
    public void rmServlet(HttpServletRequest request) throws Exception {
        ServletContext servletContext = request.getServletContext();
        StandardContext context = getContext(servletContext);

        // 获取StandardContext子容器
        Method findChildrenMethod = context.getClass().getMethod("findChildren");
        Object[] children = (Object[]) findChildrenMethod.invoke(context);
        for (Object child : children) {
            if (child instanceof Wrapper) {
                Wrapper wrapper = (Wrapper) child;
                Servlet servletInstance = wrapper.getServlet();
                if ((servletInstance instanceof ServletTest) || (servletInstance instanceof ServletShell)) {
                    String[] mappings = wrapper.findMappings();
                    if (mappings.length > 0) {
                        String patternFromWrapper = mappings[0];

                        // 删除Servlet
                        Method removeChildMethod = context.getClass().getMethod("removeChild", Container.class);
                        removeChildMethod.invoke(context, child);

                        // 删除Servlet URL映射
                        Method removeMappingMethod = context.getClass().getMethod("removeServletMapping", String.class);
                        removeMappingMethod.invoke(context, patternFromWrapper);
                    }
                }
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
