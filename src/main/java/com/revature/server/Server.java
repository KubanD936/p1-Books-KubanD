package com.revature.server;

import com.revature.Servlet.BooksServlet;
import com.revature.Servlet.DefaultServlet;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
    Logger logger = LoggerFactory.getLogger(Server.class);
    Tomcat server;

    // Run server
    public Server() {
        server = new Tomcat();
        server.getConnector();
        server.addContext("", null);
        //new DatabaseController().BuildDatabase();
        // Default and Books Servlets
        server.addServlet("", "defaultServlet", new DefaultServlet()).addMapping("/*");
        server.addServlet("", "booksServlet", new BooksServlet()).addMapping("/books");
    }


    public void run() {
        try {
            server.start();
        } catch (LifecycleException e) {
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }
}
