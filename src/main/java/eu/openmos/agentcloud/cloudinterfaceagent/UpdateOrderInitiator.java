/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.cloudinterfaceagent;

import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.OrderInstance;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author pedro
 */
public class UpdateOrderInitiator extends AchieveREInitiator {
    /**
     * Object that writes logs into the file.
     */
    private static final Logger logger = Logger.getLogger(UpdateOrderInitiator.class);
    
    public UpdateOrderInitiator(Agent a, ACLMessage msg) {
        super(a, msg);
    }
    
    /**
     * Method to create the message that starts this FIPA Request Protocol. The
     * content contains a serialized form of and Order object.
     * 
     * @param receiver - AID of the receiver Agent.
     * @param content - Content of the message to be sent.
     * @return Object ACLMessage with the message that starts the Protocol.
     */
/*    
    public static ACLMessage buildMessage(AID receiver, String content) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.ONTO_UPDATE_ORDER);
        msg.addReceiver(receiver);
        msg.setContent(content);
        return msg;
    }
*/    
    public static ACLMessage buildMessage(AID receiver, OrderInstance order) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.ONTO_UPDATE_ORDER);
        msg.addReceiver(receiver);
        try {
            //        msg.setContent(content);
            msg.setContentObject(order);
        } catch (IOException ex) {
            logger.error(ex);
        }
        return msg;
    }
    
    /**
     * Handle inform method that receives an inform message if any comes from the
     * Protocol. 
     * .
     * @param inform - The message sent by the counterpart agent.
     */
    @Override
    protected void handleInform(ACLMessage inform) {
        logger.info("Order updated successfully.");
    }
    
}
