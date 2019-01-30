/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.agent.initiators;

import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import eu.openmos.model.UnexpectedProductData;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import java.io.IOException;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 * Notifies the optimizer whenever a product goes into the wrong workstation.
 * 
 * Content: message coming from the MSB via socket.
 * Counterpart: Optimizer Agent
 * Ontology: ONTO_UNEXPECTED_PRODUCT.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 * @author Pedro Monteiro <pedro.monteiro@uninova.pt>
 * 
 */
public class UnexpectedProductInitiator extends AchieveREInitiator {
    /**
    * Object that writes logs into the file.
    */
    private static final Logger logger = Logger.getLogger(UnexpectedProductInitiator.class);   
    
    /**
     * Method to create the message that starts this FIPA Request Protocol. The
     * message contains:
     * 
     * workstation id
     * expected product id,
     * actually received product id,
     * message timestamp
     * 
     * @param agent - the receiver agent.
     * @param content - Content of the message to be sent.
     * @return Object ACLMessage with the message that starts the Protocol.
     */
//    public static ACLMessage buildMessage(Agent agent, String content) {
//        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
//        msg.setOntology(Constants.ONTO_UNEXPECTED_PRODUCT);
//        msg.setContent(content);
//        DFAgentDescription[] dfad = DFInteraction.SearchInDF(Constants.DF_PRODUCTION_OPTIMIZER, agent);   
//        msg.addReceiver(dfad[0].getName());
//        return msg;
//    }
    public static ACLMessage buildMessage(Agent agent, UnexpectedProductData unexpectedProductData) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.ONTO_UNEXPECTED_PRODUCT);
        try {
            msg.setContentObject(unexpectedProductData);
        } catch (IOException ex) {
            logger.error(ex);
        }
        DFAgentDescription[] dfad = DFInteraction.SearchInDF(Constants.DF_PRODUCTION_OPTIMIZER, agent);   
        msg.addReceiver(dfad[0].getName());
        return msg;
    }
    
    /**
     * Default Constructor of the AchieveREInitiator class.
     * 
     * @param a - Agent that initiates the Protocol.
     * @param msg - First message to be sent.
     */
    public UnexpectedProductInitiator(Agent a, ACLMessage msg) {
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
        logger.info("Optimizer acknowledged new unexpected product message from agent " + myAgent.getLocalName() + ".");
    }
    
}
