package com.smartcampus;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

//replaces MainApp for Tomcat deployment
@ApplicationPath("/api/v1")
public class AppConfig extends Application {
}