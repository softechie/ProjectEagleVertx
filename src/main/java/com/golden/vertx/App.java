package com.golden.vertx;

import controller.EmployeeController;
import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

      Launcher.executeCommand("run", EmployeeController.class.getName());


    }
}
