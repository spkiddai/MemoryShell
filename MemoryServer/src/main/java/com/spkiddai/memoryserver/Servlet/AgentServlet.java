package com.spkiddai.memoryserver.Servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.spkiddai.memoryserver.Tools.InjectAgent;



public class AgentServlet {

    @WebServlet(name = "AgentServletTest", value = "/agent/test")
    public static class AgentServletTest extends HttpServlet{

        public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                InjectAgent injectAgent = new InjectAgent();
                injectAgent.inject("Test","/app/MemoryAgent.jar");
                // 设置响应内容
                response.getWriter().println("Attach JavaAgent Test Success");
            }catch (Exception e) {
                // 异常处理
                response.getWriter().write("Error Attach JavaAgent Test: " + e.getMessage());
            }
        }

    }

    @WebServlet(name = "AgentServletReflect", value = "/agent/reflect")
    public static class AgentServletReflect extends HttpServlet{

        public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                InjectAgent injectAgent = new InjectAgent();
                injectAgent.reflectInject("Test","/app/MemoryAgent.jar");
                // 设置响应内容
                response.getWriter().println("Attach JavaAgent ReflectTest Success");
            } catch (Exception e) {
                // 异常处理
                response.getWriter().write("Error Attach JavaAgent ReflectTest: " + e.getMessage());
            }
        }
    }



    @WebServlet(name = "AgentServletShell", value = "/agent/shell")
    public static class AgentServletShell extends HttpServlet{

        public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                InjectAgent injectAgent = new InjectAgent();
                injectAgent.reflectInject("Shell","/app/MemoryAgent.jar");
                // 设置响应内容
                response.getWriter().println("Attach JavaAgent Shell Success");
            }catch (Exception e) {
                // 异常处理
                response.getWriter().write("Error Attach JavaAgent Shell: " + e.getMessage());
            }
        }

    }

    @WebServlet(name = "RASPServletRasp", value = "/agent/rasp")
    public static class RASPServletRasp extends HttpServlet{

        public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                InjectAgent injectAgent = new InjectAgent();
                injectAgent.inject("Rasp","/app/MemoryAgent.jar");
                // 设置响应内容
                response.getWriter().println("Attach JavaAgent RASP Success");
            }catch (Exception e) {
                // 异常处理
                response.getWriter().write("Error Attach JavaAgent RASP: " + e.getMessage());
            }
        }
    }



}

