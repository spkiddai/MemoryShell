package com.spkiddai.memoryserver.Servlet;

import com.spkiddai.memoryserver.Test.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TestServlet {

    @WebServlet(name = "RegisterServletTest", urlPatterns = "/servlet/test")
    public static class RegisterServletTest extends HttpServlet {

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                ServletTest servletTest = new ServletTest();
                servletTest.addServlet(request);
                response.getWriter().println("ServletTest Register Success");
            } catch (Exception e) {
                response.getWriter().write("Error Register ServletTest: " + e.getMessage());
            }
        }
    }

    @WebServlet(name = "DeleteServlet", urlPatterns = "/servlet/delete")
    public static class DeleteServletTest extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                ServletTest servletTest = new ServletTest();
                servletTest.rmServlet(request);
                response.getWriter().println("Servlet Delete Success");
            } catch (Exception e) {
                response.getWriter().write("Error Delete Servlet: " + e.getMessage());
            }
        }
    }

    @WebServlet(name = "RegisterFilterTest", urlPatterns = "/filter/test")
    public static class RegisterFilterTest extends HttpServlet {

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                FilterTest filterTest = new FilterTest();
                filterTest.addFilter(request);
                response.getWriter().println("FilterTest Register Success");
            } catch (Exception e) {
                response.getWriter().write("Error Register FilterTest: " + e.getMessage());
            }
        }
    }

    @WebServlet(name = "DeleteFilter", urlPatterns = "/filter/delete")
    public static class DeleteFilterTest extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                FilterTest filterTest = new FilterTest();
                filterTest.rmFilter(request);
                response.getWriter().println("Filter Delete Success");
            } catch (Exception e) {
                response.getWriter().write("Error Delete Filter: " + e.getMessage());
            }
        }
    }

    @WebServlet(name = "RegisterListenerTest", urlPatterns = "/listener/test")
    public static class RegisterListenerTest extends HttpServlet {

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                ListenerTest listenerTest = new ListenerTest();
                listenerTest.addListener(request);
                response.getWriter().println("ListenerTest Register Success");
            } catch (Exception e) {
                response.getWriter().write("Error Register ListenerTest: " + e.getMessage());
            }
        }
    }

    @WebServlet(name = "DeleteListener", urlPatterns = "/listener/delete")
    public static class DeleteListenerTest extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                ListenerTest listenerTest = new ListenerTest();
                listenerTest.rmListener(request);
                response.getWriter().println("Listener Delete Success");
            } catch (Exception e) {
                response.getWriter().write("Error Delete Listener: " + e.getMessage());
            }
        }
    }

    @WebServlet(name = "RegisterValveTest", urlPatterns = "/valve/test")
    public static class RegisterValveTest extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                ValveTest valveTest = new ValveTest();
                valveTest.addValve(request);
                response.getWriter().println("ValveTest Register Success");
            } catch (Exception e) {
                response.getWriter().write("Error Register ValveTest: " + e.getMessage());
            }
        }
    }

    @WebServlet(name = "DeleteValve", urlPatterns = "/valve/delete")
    public static class DeleteValveTest extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                ValveTest valveTest = new ValveTest();
                valveTest.rmValve(request);
                response.getWriter().println("Valve Delete Success");
            } catch (Exception e) {
                response.getWriter().write("Error Delete Valve: " + e.getMessage());
            }
        }
    }

}
