package com.pl2_vertx.main;

import com.pl2_vertx.controller.EntryPoint;
import io.vertx.core.Launcher;

/**
 * Hello world!
 *
 */
public class Application
{
    public static void main( String[] args )
    {

      Launcher.executeCommand("run", EntryPoint.class.getName());


    }
}
