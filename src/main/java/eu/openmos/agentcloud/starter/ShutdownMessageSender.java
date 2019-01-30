/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.starter;

import eu.openmos.agentcloud.utilities.Constants;
import java.io.*;
import java.net.*;
import org.apache.log4j.Logger;

/**
 * Utility that sends the shutdown message to the platform (message raised from the shutdown.bat file).
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class ShutdownMessageSender {
    private static final Logger logger = Logger.getLogger(ShutdownMessageSender.class);
    
    /**
     * It opens a communication to the socket server listening on the shutdown port
     * and it sends the shutdown command.
     * 
     * @param hostName  the host where the shutdownlistener lives
     * @param portNumber   the port number where the shutdownlistener is listening
     */
    public void send(String hostName, int portNumber)
    {
        
        try (
            Socket kkSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(kkSocket.getInputStream()));
        ) {
                    // out.println("Bye.");
                    out.println(Constants.PLATFORM_SHUTDOWN_MESSAGE);
        } catch (UnknownHostException e) {
            String msg = getClass().getName() + " - don't know about host " + hostName;
            logger.error(msg);
            throw new RuntimeException(msg);
        } catch (IOException e) {
            String msg = getClass().getName() + " - couldn't get I/O for the connection to " + hostName;
            logger.error(msg);
            throw new RuntimeException(msg);
        }
    }
}

