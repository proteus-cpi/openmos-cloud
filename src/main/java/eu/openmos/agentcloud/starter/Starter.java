/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.starter;

import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.utilities.Constants;
import java.util.Arrays;
import org.apache.log4j.Logger;

/**
 * The entry point of the platform. 
 * It accepts one parameter with accepted values:
 * - "start", to start the system
 * - "stop", to stop the system
 * If called without parameters, it displays the usage string and exits.
 * 
 * @author Luis Ribeiro <luis.ribeiro@liu.se>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class Starter {
    private static OpenMosRuntime runtime;      
    
    private static final Logger logger = Logger.getLogger(Starter.class);

    /**
     * The entry point of the platform. 
     * It accepts one parameter with accepted values:
     * - "start", to start the system
     * - "stop", to stop the system
     * If called without parameters, it displays the usage string and exits.
     * 
     * @param args [start|stop]
     */
    public static final void main(String[] args) 
    {
        logger.debug(new Object() { }.getClass().getEnclosingClass().getName() + " - " + args.length + " - " + Arrays.toString(args));
        
        if (args.length != 1)
        {
            showUsage();
            System.exit(0);
        }
        
        if (args.length == 1)
        {
            if (args[0].equalsIgnoreCase("stop"))
            {
                stopTheSystem();
            } else if (args[0].equalsIgnoreCase("start"))
            {
                startTheSystem();
            }
        }        
    }

    /**
     * Sends the shutdown message to the shutdown listening process.
     */
    private static void stopTheSystem() {
        String PLATFORM_HOST_VALUE;
        String PLATFORM_SHUTDOWN_PORT_VALUE;
        
        // create the socket client to send "Bye." to the socket server 
        // then exit
        //
        PLATFORM_HOST_VALUE = ConfigurationLoader.getMandatoryProperty(Constants.PLATFORM_HOST_PARAMETER);
        PLATFORM_SHUTDOWN_PORT_VALUE = ConfigurationLoader.getMandatoryProperty(Constants.PLATFORM_SHUTDOWN_PORT_PARAMETER);
        // TODO check if the port is a number
        int shutdownPort = new Integer(PLATFORM_SHUTDOWN_PORT_VALUE).intValue();

        ShutdownMessageSender sms = new ShutdownMessageSender();
        // sms.send("192.168.15.1", 9998);
        sms.send(PLATFORM_HOST_VALUE, shutdownPort);
    }

    /**
     * Starts the openmos runtime and the shutdown listening process.
     */
    private static void startTheSystem() {
        String PLATFORM_SHUTDOWN_PORT_VALUE;

        PLATFORM_SHUTDOWN_PORT_VALUE = ConfigurationLoader.getMandatoryProperty(Constants.PLATFORM_SHUTDOWN_PORT_PARAMETER);
        // TODO check if the port is a number
        int shutdownPort = new Integer(PLATFORM_SHUTDOWN_PORT_VALUE).intValue();

        runtime = new OpenMosRuntime();
        runtime.start();

        // Thread t = new Thread(new ShutdownListener(9998, runtime));
        Thread t = new Thread(new ShutdownListener(shutdownPort, runtime));
        t.start();        
        while (t.isAlive()) {}
        return;                        
    }
    
    private static void showUsage() {
        StringBuffer usage = new StringBuffer();
        
        usage.append("Usage: Starter [start|stop]");
        System.out.println(usage);
    }
}
