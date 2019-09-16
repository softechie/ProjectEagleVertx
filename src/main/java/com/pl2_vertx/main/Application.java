package com.pl2_vertx.main;

import com.pl2_vertx.controller.EntryPoint;
import com.pl2_vertx.controller.PostgresEntryPoint;
import io.vertx.core.Launcher;
//IDIOM:Item-0033-One Class per file (same for Enum or Interface)
/**
 * Hello world!
 *
 */
//IDIOM:Item-0065: Upper case naming
public class Application
{
    public static void main( String[] args )
    {

      Launcher.executeCommand("run", PostgresEntryPoint.class.getName());
     // Launcher.executeCommand("run", EntryPoint.class.getName());
    }
}
