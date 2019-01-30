/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.starter;

import eu.openmos.agentcloud.ws.agentplatformkiller.wsimport.AgentPlatformKiller;
import eu.openmos.agentcloud.ws.agentplatformkiller.wsimport.AgentPlatformKiller_Service;
import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.servicebus.MSBHelper;
import eu.openmos.agentcloud.services.rest.StartRS;
import eu.openmos.agentcloud.utilities.Constants;
import jade.core.Profile;
import jade.core.Runtime;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import org.apache.log4j.Logger;

/**
 * Main openmos runtime. 
 * The start() method instanciates the jade container, loads eventually the graphical console and creates the platform launcher agent (the first agent). 
 * The stop() method calls the web-service that kills all the agents and then destroys the main jade container.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class OpenMosRuntime {
    private static final Logger logger = Logger.getLogger(Starter.class);
    private ContainerController cc;

    /**
     * It calls the web-service that kills all the agents and then destroys the main jade container.
     */
    protected void stop() {
        String PLATFORM_HOST_VALUE;
        String PLATFORM_PORT_VALUE;
        logger.info(getClass().getName() + " - stopping the OpenMOS agent platform...");

//        PLATFORM_HOST_VALUE = ConfigurationLoader.getProperty(Constants.PLATFORM_HOST_PARAMETER);
//        PLATFORM_PORT_VALUE = ConfigurationLoader.getProperty(Constants.PLATFORM_PORT_PARAMETER);

        AgentPlatformKiller_Service agentPlatformKillerService = new AgentPlatformKiller_Service();
        logger.debug(getClass().getName() + " - service created");
	AgentPlatformKiller agentPlatformKiller = agentPlatformKillerService.getAgentPlatformKillerImplPort();
        logger.debug(getClass().getName() + " - service port created");
        
        agentPlatformKiller.shutdown();
        logger.debug(getClass().getName() + " - shutdown service method called");

        if (cc != null) {
            try {
                cc.kill();
            } catch (Exception e) {
                String msg = getClass().getName() + " - OpenMOS platform stopping error";
                logger.error(msg, e);
                throw new RuntimeException(e);
            }
        }
        
        logger.info(getClass().getName() + " - OpenMOS agent platform stopped. Exiting with return code 0");
        logger.debug(getClass().getName() + " - exiting with return code 0");
        System.exit(0);
    }
    
    /**
     * It instanciates the jade container, loads eventually the graphical console and creates the platform launcher agent (the first agent). 
     */
    protected void start()
    {
        String PLATFORM_HOST_VALUE;
        String PLATFORM_PORT_VALUE;
        String PLATFORM_NAME_VALUE;
        String PLATFORM_RELEASE_VALUE;
        String PLATFORM_GUI_VALUE;
        ConfigurationLoader configurationLoader;

        logger.info(getClass().getName() + " - starting the system...");
        logger.info(getClass().getName() + " -  loading configuration...");
        
        PLATFORM_HOST_VALUE = ConfigurationLoader.getMandatoryProperty(Constants.PLATFORM_HOST_PARAMETER);
        PLATFORM_PORT_VALUE = ConfigurationLoader.getMandatoryProperty(Constants.PLATFORM_PORT_PARAMETER);
        PLATFORM_NAME_VALUE = ConfigurationLoader.getMandatoryProperty(Constants.PLATFORM_NAME_PARAMETER);
        PLATFORM_RELEASE_VALUE = ConfigurationLoader.getMandatoryProperty(Constants.PLATFORM_RELEASE_PARAMETER);

        PLATFORM_GUI_VALUE = ConfigurationLoader.getDefaultedProperty(Constants.PLATFORM_GUI_PARAMETER, "false");

        logger.info(getClass().getName() + " - OpenMOS agent plaform release detected " + PLATFORM_RELEASE_VALUE);
        logger.info(getClass().getName() + " - configuration loaded...");

        Runtime runtime = Runtime.instance();
        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, PLATFORM_HOST_VALUE);
        p.setParameter(Profile.MAIN_PORT, PLATFORM_PORT_VALUE);
        
        cc = runtime.createMainContainer(p);        
        if (cc == null) {
            String msg = "OpenMOS platform could not create jade container. Exiting";
            logger.error(msg);
            throw new RuntimeException(msg);            
        }
        try {
            // optional start of the graphical console
            //
            if ("true".equalsIgnoreCase(PLATFORM_GUI_VALUE))
            {
                AgentController rma = cc.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
                rma.start();
            }

            AgentController ac = cc.createNewAgent(PLATFORM_NAME_VALUE,
                    Constants.PLATFORM_LAUNCHER_AGENT_CLASS, null);
            ac.start();
            
            // start of rest services
            StartRS.start();
            
            // msb notification cloud is active
            MSBHelper.confirmCloudActive();
            
            logger.info(getClass().getName() + " - OpenMOS platform started");               
        } catch (Exception e) {
            String msg = getClass().getName() + " - OpenMOS platform starting error";
            logger.error(msg, e);
            throw new RuntimeException(e);
        }
    }
    
}
