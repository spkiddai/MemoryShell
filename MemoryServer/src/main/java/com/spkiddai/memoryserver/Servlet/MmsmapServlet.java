package com.spkiddai.memoryserver.Servlet;

import com.spkiddai.memoryserver.Msmap.TomcatListener;
import com.spkiddai.memoryserver.Msmap.TomcatServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MmsmapServlet {

    @WebServlet(name = "MsmapServlet", value = "/msmap/servlet")
    public static class MsmapServlet extends HttpServlet {

        public void doGet(HttpServletRequest request, HttpServletResponse response) {
            TomcatServlet tomcatServlet = new TomcatServlet();
            tomcatServlet.Register();
        }
    }

    @WebServlet(name = "MsmapListener", value = "/msmap/listener")
    public static class MsmapListener extends HttpServlet {

        public void doGet(HttpServletRequest request, HttpServletResponse response) {
            TomcatListener tomcatListener = new TomcatListener();
            tomcatListener.Register();
        }
    }
}



