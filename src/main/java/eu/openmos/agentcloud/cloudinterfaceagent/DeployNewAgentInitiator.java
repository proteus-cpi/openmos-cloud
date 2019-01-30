/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.cloudinterfaceagent;

import eu.openmos.agentcloud.servicebus.MSBHelper;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.SubSystem;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * Initiator that is to be launched whenever the CIA (Cloud Interface Agent) is 
 * notified to deploy a new agent. This will start a FIPA Request Protocol that
 * will request the deployment agent to deploy the requested agent.
 * 
 * Content: Serialized form of a CyberPhysicalAgentDescription object. 
 * Counterpart: Deployment Agent
 * Ontology: DEPLOY_NEW_AGENT.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * 
 */
public class DeployNewAgentInitiator extends AchieveREInitiator {
    /**
     * Object that writes logs into the file.
     */
    private static final Logger logger = Logger.getLogger(DeployNewAgentInitiator.class.getName());    
    
    /**
     * Method to create the message that starts this FIPA Request Protocol. The
     * content contains the description of the agent to be deployed.
     * 
     * @param receiver - AID of the receiver Agent.
     * @param content - Content of the message to be sent.
     * @return Object ACLMessage with the message that starts the Protocol.
     */
    public static ACLMessage buildMessage(AID receiver, String content) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.DEPLOY_NEW_AGENT);
        msg.addReceiver(receiver);
        logger.debug("buildMessage content length = " + content.length());
        msg.setContent(content);
        return msg;
    }
    
    public static ACLMessage buildMessage(AID receiver, SubSystem subSystem) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.DEPLOY_NEW_AGENT);
        msg.addReceiver(receiver);
        // logger.debug("buildMessage content length = " + content.length());
        try
        {
        msg.setContentObject(subSystem);
        }
        catch (IOException e)
        {
            logger.error(e);
        }
        return msg;
    }
    
    /**
     * Default Constructor of the AchieveREInitiator class.
     * 
     * @param a - Agent that initiates the Protocol.
     * @param msg - First message to be sent.
     */
    public DeployNewAgentInitiator(Agent a, ACLMessage msg) {
        super(a, msg);
    }

    /**
     * Handle agree method that receives an agree message if any comes from the
     * Protocol. Debug purposes only.
     * 
     * @param agree - The message sent by the counterpart agent.
     */
    @Override
    protected void handleAgree(ACLMessage agree) {
        logger.debug("Agent being deployed..");
    }  
    
    /**
     * Handle inform method that receives an inform message if any comes from the
     * Protocol. Notifies the Manufacturing Service Bus that the new agent has 
     * been deployed
     * .
     * @param inform - The message sent by the counterpart agent.
     */
    @Override
    protected void handleInform(ACLMessage inform) {        
        logger.debug("Agent deployed succesfully..");

        // call service made available by the msb.  
        logger.debug("Call service to inform the MSB.");
        String equipmentId = inform.getContent();
        MSBHelper.confirmAgentCreated(equipmentId);
    }

    /**
     * Handle failure method that receives a failure message if any comes from
     * the Protocol. Notifies the Manufacturing Service Bus that the new agent  
     * has not been deployed
     * .
     * @param inform - The message sent by the counterpart agent.
     */
    @Override
    protected void handleFailure(ACLMessage failure) {
        logger.debug("Agent was not deployed: " + failure.getContent());
        
        // call service made available by the msb.  
        logger.debug("Call service to inform the MSB.");
        String equipmentId = failure.getContent();
        MSBHelper.notifyAgentNotCreated(equipmentId);
    }
    
}
