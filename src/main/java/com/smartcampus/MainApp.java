package com.smartcampus;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.net.URI;

public class MainApp {

    public static final String BASE_URI = "http://localhost:8080/api/v1/";

    public static void main(String[] args) {

        ResourceConfig config = new ResourceConfig()
                .packages("com.smartcampus");

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create(BASE_URI), config
        );

        System.out.println("Server running at " + BASE_URI);
    }
}