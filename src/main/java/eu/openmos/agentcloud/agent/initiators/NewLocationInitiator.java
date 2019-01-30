/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.agent.initiators;

import eu.openmos.agentcloud.utilities.Constants;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import org.apache.log4j.Logger;

/**
 * Notifies the product when new location has been reached.
 * 
 * Content: Serialized form of a location object.
 * Counterpart: Optimizer Agent
 * Ontology: ONTO_LOCATION.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * 
 */
public class NewLocationInitiator extends AchieveREInitiator {
    /**
    * Object that writes logs into the file.
    */
    private static final Logger logger = Logger.getLogger(NewLocationInitiator.class.getName());  
    
    /**
     * Method to create the message that starts this FIPA Request Protocol. The
     * content contains the new location.
     * 
     * @param receiver - the receiver agent.
     * @param content - Content of the message to be sent.
     * @return Object ACLMessage with the message that starts the Protocol.
     */
    public static ACLMessage buildMessage(Agent receiver, String content) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(receiver.getAID());
        msg.setOntology(Constants.ONTO_LOCATION);
        msg.setContent(content);
        return msg;
    }
 
    /**
     * Default Constructor of the AchieveREInitiator class.
     * 
     * @param a - Agent that initiates the Protocol.
     * @param msg - First message to be sent.
     */
    public NewLocationInitiator(Agent a, ACLMessage msg) {
        super(a, msg);
    }

    /**
     * Handle inform method that receives an inform message if any comes from the
     * Protocol. 
     * .
     * @param inform - The message sent by the counterpart agent.
     */
    @Override
    protected void handleInform(ACLMessage inform) {
        logger.info(inform.getSender().getLocalName() + " acknowledged new location.");
    }
    
}
