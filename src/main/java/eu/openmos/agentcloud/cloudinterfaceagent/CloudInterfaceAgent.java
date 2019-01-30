/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.cloudinterfaceagent;

import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.cloudinterface.AgentPlatformKillerImpl;
import eu.openmos.agentcloud.cloudinterface.SystemConfiguratorImpl;
import eu.openmos.agentcloud.toolagent.ToolAgent;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import org.apache.log4j.Logger;
import javax.xml.ws.Endpoint;

/**
 * Cloud Interface Agent responsible for receiving external requests to be 
 * satisfied by the Agent Cloud. This includes deploying new agents and orders to
 * the cloud and removing agents from it.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 * @author Pedro Lima Monteiro <pedro.monteiro@uinova.pt>
 * 
 */
public class CloudInterfaceAgent extends ToolAgent {
    
    private String AGENT_SYSTEMCONFIGURATOR_WS_VALUE;
    private String AGENT_AGENTPLATFORMKILLER_WS_VALUE;

    /**
     * Object that writes logs into the file.
     */
    private static final Logger logger = Logger.getLogger(CloudInterfaceAgent.class.getName());
    
    /**
     * Setup method for the Cloud Interface Agent. Registers the agent in the DF.
     * TO DO: Explain endpoints (Valerio)
     */
    @Override
    protected void setup() {
        super.setup();
        
        DFInteraction.RegisterInDF(this, this.getLocalName(), Constants.DF_CLOUD_INTERFACE);

        // Making web-services available to external connections
        logger.info(getClass().getName() + " - Hello, my name is " + this.getName() + " and I just woke up.");

        AGENT_SYSTEMCONFIGURATOR_WS_VALUE = ConfigurationLoader.getMandatoryProperty(Constants.AGENT_SYSTEMCONFIGURATOR_WS_PARAMETER);
        Endpoint.publish(AGENT_SYSTEMCONFIGURATOR_WS_VALUE, new SystemConfiguratorImpl(this));	   
        logger.info(getClass().getName() + " - Published Agent System Configurator wsSystemConfigurator");            

        AGENT_AGENTPLATFORMKILLER_WS_VALUE = ConfigurationLoader.getMandatoryProperty(Constants.AGENT_AGENTPLATFORMKILLER_WS_PARAMETER);
        Endpoint.publish(AGENT_AGENTPLATFORMKILLER_WS_VALUE, new AgentPlatformKillerImpl(this));	   
        logger.info(getClass().getName() + " - Published Agent Platform Killer wsAgentPlatformKiller");            
    }

    /**
     * Takedown method of the Cloud Interface Agent. It deregisters the agent 
     * from the DF.
     */
    @Override
    protected void takeDown() {
        super.takeDown();
    }

}
