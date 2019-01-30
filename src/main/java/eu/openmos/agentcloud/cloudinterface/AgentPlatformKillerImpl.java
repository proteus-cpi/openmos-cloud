/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.cloudinterface;

import eu.openmos.agentcloud.utilities.ServiceCallStatus;
import eu.openmos.agentcloud.cloudinterfaceagent.RemoveAgentInitiator;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import java.util.ResourceBundle;
import javax.jws.WebService;
import org.apache.log4j.Logger;

/**
 * Agent Platform Killer, an internal service for graceful shutdown of the agents before the Jade platform is closed.
 * This concrete web-service class implements shutdown method.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@WebService(endpointInterface = "eu.openmos.agentcloud.cloudinterface.AgentPlatformKiller", serviceName = "AgentPlatformKiller")
public class AgentPlatformKillerImpl implements AgentPlatformKiller {

    private static final Logger logger = Logger.getLogger(AgentPlatformKillerImpl.class.getName());
    private Agent cloudInterfaceAgent = null;
    private static final String CLOUDINTERFACEAGENT_NULL = "agentcloud.CI.systemconfigurator.cloudinterfaceagent.null";

    /**
     * Default constructor.
     */
    public AgentPlatformKillerImpl() {}
    
    /**
     * Constructor with reference to the cloud interface agent.
     * 
     * @param cloudInterfaceAgent  The existing cloud interface agent
     */
    public AgentPlatformKillerImpl(Agent cloudInterfaceAgent) {
        this.cloudInterfaceAgent = cloudInterfaceAgent;
    }
    
    /**
     * Kills resource transport, product agents, production optimizer agent, deployment agent, 
     * platform starter agent and cloud interface agent.
     * 
     * @return  0 for OK, -1 for KO
     */
    @Override
    public int shutdown() {
        logger.info(getClass().getName() + " - shutdown - starting shutdown procedure...");
        // MessageTrackerWSClient.track("shutdown", "agentplatformkiller", "starting shutdown procedure...");

        if (this.cloudInterfaceAgent == null) {
            return traceAndReturnError(CLOUDINTERFACEAGENT_NULL);
        }

        // resource agents
        DFAgentDescription[] dfResources = DFInteraction.SearchInDF(Constants.DF_RESOURCE, this.cloudInterfaceAgent);
        logger.debug(getClass().getName() + " - shutdown - remove resource agent -> resource agents found to remove: [" + dfResources.length + "]");
        for (DFAgentDescription ag : dfResources) {
            sendRemovalMessage(this.cloudInterfaceAgent,ag);
        }

        // transport agents
        DFAgentDescription[] dfTransports = DFInteraction.SearchInDF(Constants.DF_TRANSPORT, this.cloudInterfaceAgent);
        logger.debug(getClass().getName() + " - shutdown - remove transport agent -> transport agents found to remove: [" + dfTransports.length + "]");
        for (DFAgentDescription ag : dfTransports) {
            sendRemovalMessage(this.cloudInterfaceAgent,ag);
        }

        // product agents
        DFAgentDescription[] dfProducts = DFInteraction.SearchInDF(Constants.DF_RESOURCE, this.cloudInterfaceAgent);
        logger.debug(getClass().getName() + " - shutdown - remove product agent -> product agents found to remove: [" + dfProducts.length + "]");
        for (DFAgentDescription ag : dfProducts) {
            sendRemovalMessage(this.cloudInterfaceAgent,ag);
        }

        // production optimizer agent
        DFAgentDescription[] dfProductionOptimizer = DFInteraction.SearchInDF(Constants.DF_PRODUCTION_OPTIMIZER, this.cloudInterfaceAgent);
        logger.debug(getClass().getName() + " - shutdown - remove production optimizer agent -> production optimizer agents found to remove: [" + dfProductionOptimizer.length + "]");
        if (dfProductionOptimizer.length != 1)
        {
            // TODO something strange
        }
        sendRemovalMessage(this.cloudInterfaceAgent,dfProductionOptimizer[0]);

        // deployment agent
        DFAgentDescription[] dfDeployment = DFInteraction.SearchInDF(Constants.DF_DEPLOYMENT, this.cloudInterfaceAgent);
        logger.debug(getClass().getName() + " - shutdown - remove deployment agent -> deployment agents found to remove: [" + dfDeployment.length + "]");
        if (dfDeployment.length != 1)
        {
            // TODO something strange
        }
        sendRemovalMessage(this.cloudInterfaceAgent,dfDeployment[0]);

        // cloudinterface agent
        DFAgentDescription[] dfCloudInterface = DFInteraction.SearchInDF(Constants.DF_CLOUD_INTERFACE, this.cloudInterfaceAgent);
        logger.debug(getClass().getName() + " - shutdown - remove cloud interface agent -> cloud interface agents found to remove: [" + dfCloudInterface.length + "]");
        if (dfCloudInterface.length != 1)
        {
            // TODO something strange
        }
        sendRemovalMessage(this.cloudInterfaceAgent,dfCloudInterface[0]);

        // platform starter agent
        DFAgentDescription[] dfPlatformStarter = DFInteraction.SearchInDF(Constants.DF_PLATFORMSTARTER, this.cloudInterfaceAgent);
        logger.debug(getClass().getName() + " - shutdown - remove platform starter agent -> platform starter agents found to remove: [" + dfPlatformStarter.length + "]");
        if (dfPlatformStarter.length != 1)
        {
            // TODO something strange
        }
        sendRemovalMessage(this.cloudInterfaceAgent,dfPlatformStarter[0]);
       
        return traceAndReturnSuccess(ServiceCallStatus.REMOVEAGENT_CALL_SUCCESS);
        
    }
    
    /**
     * Logs the operation and returns -1 for KO.
     * 
     * @param msgCode   The error message code to be found into resource bundle messages.
     * @return   -1 for KO
     */
    private int traceAndReturnError(String msgCode) {
        String msg = getMessage(msgCode);
        logger.error(getClass().getName() + " - shutdown - " + msg);
        // MessageTrackerWSClient.track("shutdown", "agentplatformkiller", msg);
        return -1;
    }

    /**
     * Logs the operation and returns 0 for OK.
     * 
     * @param msgCode   The message code to be found into resource bundle messages.
     * @return   0 for KO
     */
    private int traceAndReturnSuccess(String msgCode) {
        String msg = getMessage(msgCode);
        logger.info(getClass().getName() + " - shutdown - " + msg);
        // MessageTrackerWSClient.track("shutdown", "agentplatformkiller", msg);
        return 0;
    }
    
    /**
     * Looks for the message code into resource bundle messages.
     * 
     * @param msgCode   The message code to be found into resource bundle messages.
     * @return    The localized message.
     */
    private String getMessage(String msgCode) {
        ResourceBundle messages = ResourceBundle.getBundle("resultsmessages");
        String message = messages.getString(msgCode);
        return message;
    }    

    /**
     * Send a message to an agent asking for suicide.
     * 
     * @param cloudInterfaceAgent   Reference to the cloud interface agent
     * @param ag    Reference to the agent to be removed.
     */
    private void sendRemovalMessage(Agent cloudInterfaceAgent, DFAgentDescription ag) {
        logger.debug(getClass().getName() + " - sendRemovalMessage - current agent to be removed: [" + ag.getName() + "]");

        logger.debug(getClass().getName() + " - sendRemovalMessage - creating removal message...");
//        ACLMessage removalMessage = RemoveAgentInitiator.buildMessage(ag.getName(), "");
        ACLMessage removalMessage = RemoveAgentInitiator.buildMessage(ag.getName(), ag.getName().getLocalName());
        logger.debug(getClass().getName() + " - sendRemovalMessage -> removal message created: " + removalMessage.toString());

        // behaviour instantiation and adding to the agent        
        cloudInterfaceAgent.addBehaviour(new RemoveAgentInitiator(cloudInterfaceAgent, removalMessage));
        logger.debug(getClass().getName() + " - sendRemovalMessage -> removal behaviour added");
    }
}
