/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.starter;

// import eu.openmos.agentdataservices.Test1DAOImpl;
import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.cloudinterfaceagent.CloudInterfaceAgent;
import eu.openmos.agentcloud.deploymentagent.DeploymentAgent;
import eu.openmos.agentcloud.optimizer.productionoptimizeragent.ProductionOptimizerAgent;
// import eu.openmos.datacloudservices.MessageTrackerImpl;
import eu.openmos.agentcloud.optimizer.PluggableOptimizerInterface;
import eu.openmos.agentcloud.toolagent.ToolAgent;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import javax.xml.ws.Endpoint;
import org.apache.log4j.Logger;

/**
 * The first agent created, the agent that starts other tool agents. 
 * Dolores, the first host created.
 * @see http://westworld.wikia.com/wiki/Dolores_Abernathy
 * 
 * @author Luis Ribeiro <luis.riberio@liu.se>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class PlatformLauncherAgent extends ToolAgent {
    private static final Logger logger = Logger.getLogger(PlatformLauncherAgent.class);

    /**
     * Setup method for the Platform Launcher Agent. 
     * It registers the agent in the DF, 
     * loads the suggested optimizer from the classpath (according with the configuration file),
     * starts the production optimizer agent (passing the optimizer implementation), 
     * the deployment agent, the cloud interface agent.
     */
    @Override
    protected void setup() {
        super.setup();
                
        String PRODUCTION_OPTIMIZER_ALGORITHM_VALUE;
        // String PRODUCTION_OPTIMIZER_ALGORITHM_PARAMETER_VALUE;
        String AGENT_DATA_WS_VALUE;
        String DATA_SERVICES_WS_VALUE;

        DFInteraction.RegisterInDF(this, Constants.PLATFORM_STARTER_SERVICE_NAME, Constants.DF_PLATFORMSTARTER);
        
        try {

            PRODUCTION_OPTIMIZER_ALGORITHM_VALUE = ConfigurationLoader.getMandatoryProperty(Constants.PRODUCTION_OPTIMIZER_ALGORITHM_NAME);
            logger.info("Default production optimizer algorithm name = " + PRODUCTION_OPTIMIZER_ALGORITHM_VALUE);
            // OptimizationAlgorithm oa = instanciateOptimizationAlgorithm();
            PluggableOptimizerInterface oa = instanciateOptimizationAlgorithm();
            AgentController productionOptimizerAgentController= getContainerController().acceptNewAgent(Constants.PRODUCTION_OPTIMIZER_AGENT_NAME, new ProductionOptimizerAgent(PRODUCTION_OPTIMIZER_ALGORITHM_VALUE, oa));
            productionOptimizerAgentController.start();

            AgentController deploymentAgentController = getContainerController().acceptNewAgent(Constants.DEPLOYMENT_AGENT_NAME, new DeploymentAgent());
            deploymentAgentController.start();

            AgentController cloudInterfaceAgentController = getContainerController().acceptNewAgent(Constants.CLOUD_INTERFACE_AGENT_NAME, new CloudInterfaceAgent());
            cloudInterfaceAgentController.start();

            // Web-services setup
            //
            // VaG - 22/12/2016
            // I don't start these services for now.
            //
//            AGENT_DATA_WS_VALUE = ConfigurationLoader.getMandatoryProperty(Constants.AGENT_DATA_WS_PARAMETER);
//            Endpoint.publish(AGENT_DATA_WS_VALUE, new Test1DAOImpl());
//            logger.info("Published AgentData webservice wsAgentDataTest1DAO");
//
//            DATA_SERVICES_WS_VALUE = ConfigurationLoader.getMandatoryProperty(Constants.DATA_SERVICES_WS_PARAMETER);
//            Endpoint.publish(DATA_SERVICES_WS_VALUE, new MessageTrackerImpl());
//            logger.info("Published DataCloud webservice MessageTrackerImpl");

        } catch (StaleProxyException ex) {
            logger.error(ex);
        }

    }

//    private OptimizationAlgorithm instanciateOptimizationAlgorithm() {
//        String PRODUCTION_OPTIMIZER_ALGORITHM_CLASS_VALUE;
//    
//        PRODUCTION_OPTIMIZER_ALGORITHM_CLASS_VALUE = ConfigurationLoader.getMandatoryProperty(Constants.PRODUCTION_OPTIMIZER_ALGORITHM_CLASS_NAME);
//        logger.info("Default production optimizer algorithm class = " + PRODUCTION_OPTIMIZER_ALGORITHM_CLASS_VALUE);
//
//        OptimizationAlgorithm oa = null;
//
//        // optimizer class
//        Class prodOptAlg;
//        try {
//            prodOptAlg = Class.forName(PRODUCTION_OPTIMIZER_ALGORITHM_CLASS_VALUE);
//
//            try {
//                // VaG - 21/12/2016
//                // oa = (OptimizationAlgorithm) prodOptAlg.newInstance();
//                oa = (PluggableOptimizerInterface) prodOptAlg.newInstance();
//            } catch (IllegalAccessException iae) {
//                logger.error(iae.getMessage());
//                throw new RuntimeException(iae);
//            } catch (InstantiationException ie) {
//                logger.error(ie.getMessage());
//                throw new RuntimeException(ie);
//            }
//        } catch (ClassNotFoundException cnfe) {
//            logger.error(cnfe.getMessage());
//            logger.error("The OpenMOS platform can not run without optimization algorithm. Aborting...");
//            // throw new RuntimeException(cnfe);
//            System.exit(-1);
//        }
//
//        return oa;
//    }
//
    private PluggableOptimizerInterface instanciateOptimizationAlgorithm() {
        String PRODUCTION_OPTIMIZER_ALGORITHM_CLASS_VALUE;
    
        PRODUCTION_OPTIMIZER_ALGORITHM_CLASS_VALUE = ConfigurationLoader.getMandatoryProperty(Constants.PRODUCTION_OPTIMIZER_ALGORITHM_CLASS_NAME);
        logger.info("Default production optimizer algorithm class = " + PRODUCTION_OPTIMIZER_ALGORITHM_CLASS_VALUE);

        // OptimizationAlgorithm oa = null;
        PluggableOptimizerInterface oa = null;

        // optimizer class
        Class prodOptAlg;
        try {
            prodOptAlg = Class.forName(PRODUCTION_OPTIMIZER_ALGORITHM_CLASS_VALUE);

            try {
                // VaG - 21/12/2016
                // oa = (OptimizationAlgorithm) prodOptAlg.newInstance();
                oa = (PluggableOptimizerInterface) prodOptAlg.newInstance();
            } catch (IllegalAccessException iae) {
                logger.error(iae.getMessage());
                throw new RuntimeException(iae);
            } catch (InstantiationException ie) {
                logger.error(ie.getMessage());
                throw new RuntimeException(ie);
            }
        } catch (ClassNotFoundException cnfe) {
            logger.error(cnfe.getMessage());
            logger.error("The OpenMOS platform can not run without optimization algorithm. Aborting...");
            // throw new RuntimeException(cnfe);
            System.exit(-1);
        }

        return oa;
    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }
    
    
}
