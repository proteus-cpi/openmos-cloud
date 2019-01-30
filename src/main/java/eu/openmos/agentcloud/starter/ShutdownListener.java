/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.starter;

import eu.openmos.agentcloud.utilities.Constants;
import java.net.*;
import java.io.*;
import org.apache.log4j.Logger;

/**
 * Java thread launched by the bootstrap class,
 * it waits on the shutdown designated port for the shutdown command (from the shutdown.bat file)
 * and it stops the system.
 * @see eu.openmos.agentcloud.utilities.Constants.PLATFORM_SHUTDOWN_PORT_PARAMETER

 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class ShutdownListener implements Runnable {
    int portNumber = -1;
    OpenMosRuntime runtime = null;
    private static final Logger logger = Logger.getLogger(ShutdownListener.class);
    
    /**
     * Constructor.
     * 
     * @param portNumber  The port number where this thread is listening for shutdown command.
     * @param runtime   Reference to the openmos runtime object
     */
    public ShutdownListener(int portNumber, OpenMosRuntime runtime)
    {
        this.portNumber = portNumber;
        this.runtime = runtime;
        if (portNumber <= 0)
        {
            String msg = getClass().getName() + " - port number must be a valid port number";
            logger.error(msg);
            throw new RuntimeException(msg);
        }
        if (runtime == null)
        {
            String msg = getClass().getName() + " - openmos runtime reference cannot be null";
            logger.error(msg);
            throw new RuntimeException(msg);
        }
    }

    /**
     * Main thread method,
     * it creates the server socket and waits for the shutdown command.
     * As soon as it receives the shutdown command, it stops the platform gracefully.
     */
    @Override
    public void run() {
        try ( 
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        ) {
        
            String inputLine, outputLine;
            
            while ((inputLine = in.readLine()) != null) {
                logger.debug("inputLine received = [" + inputLine + "]");
                // if (inputLine.equals("Bye."))
                if (inputLine.equals(Constants.PLATFORM_SHUTDOWN_MESSAGE))
                    break;
            }
            
            runtime.stop();

        } catch (IOException e) {
            logger.error("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            logger.error(e.getMessage());
        }
    }
}

