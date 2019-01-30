/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.optimizer.databasegateway;

import eu.openmos.agentcloud.config.ConfigurationLoader;
// import static eu.openmos.shells.openmosagent.utilities.Constants.*;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;

/**
 *
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 */
public class MongoDB {
    protected final Runtime myRuntime;
    protected Process myProcess;
    protected boolean running;
    protected Host myHost;    
    private static final String MONGODB_EXEC_PATH_NAME = "openmos.db.mongodb.exec.path";
    private static final String MONGODB_DATA_PATH_NAME = "openmos.db.mongodb.data.path";
    private static final Logger logger = Logger.getLogger(MongoDB.class.getName());    
    
    public MongoDB() {
        logger.debug("Starting MongoDB.");
        this.myRuntime = Runtime.getRuntime();       
                
        try {
            myHost = new Host(Inet4Address.getLocalHost().getHostAddress() + ":27017");
        } catch (UnknownHostException ex) {
            logger.error(ex);
        }  
        
        logger.debug("MongoDB ready.");
    }
    
    public MongoDB(int port) {
        logger.debug("Starting MongoDB.");
        this.myRuntime = Runtime.getRuntime();       
                
        try {
            myHost = new Host(Inet4Address.getLocalHost().getHostAddress() + ":" + port);
        } catch (UnknownHostException ex) {
            logger.error(ex);
        }   
        
        logger.debug("MongoDB ready.");
    }
    
    public void shutdown() {
        logger.debug("Shutting down MongoDB.");
        myProcess.destroy();
        changeRunningState();
        logger.debug("MongoDB shutdown.");
    }

    public void launch() {        
        String MONGODB_EXEC_PATH_VALUE = ConfigurationLoader.getProperty(MONGODB_EXEC_PATH_NAME);
        if (MONGODB_EXEC_PATH_VALUE == null) {
            String msg = "Missing configuration for " + MONGODB_EXEC_PATH_NAME;
            logger.error(msg);
            throw new RuntimeException(msg);
        }
        logger.info("Default mongodb exec path = " + MONGODB_EXEC_PATH_VALUE);

        String MONGODB_DATA_PATH_VALUE = ConfigurationLoader.getProperty(MONGODB_DATA_PATH_NAME);
        if (MONGODB_DATA_PATH_VALUE == null) {
            String msg = "Missing configuration for " + MONGODB_DATA_PATH_NAME;
            logger.error(msg);
            throw new RuntimeException(msg);
        }
        logger.info("Default mongodb data path = " + MONGODB_DATA_PATH_VALUE);
        
        try {
            String[] command = new String[] {
                MONGODB_EXEC_PATH_VALUE,
                "--dbpath",
                MONGODB_DATA_PATH_VALUE,
                ((myHost.getPort() != 27017) ? ("--port " + myHost.getPort()) : "")
            };
            myProcess = myRuntime.exec(command);
            changeRunningState();
        } catch (IOException ex) {
            logger.error(ex);
        }  
        logger.debug("MongoDB running on host " + myHost.toString() + ".");
    }

    public boolean getRunningState() {
        return running;
    }

    public void changeRunningState() {
        this.running = !this.running;
    }

    public Host getMyHost() {
        return myHost;
    }
}
