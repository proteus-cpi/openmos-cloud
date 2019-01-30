/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.productagent;

import eu.openmos.model.RawProductData;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import java.io.IOException;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 * Notifies the optimizer of the changes occurred in a product agent.
 * 
 * Content: Serialized form of an Agent Data object.
 * Counterpart: Optimizer Agent
 * Ontology: ONTO_UPDATE_CPA_PERFORMANCE_DATA.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * 2/12/2016
 * 
 */
public class NotifyOptimizerOfUpdatedData extends AchieveREInitiator{    
    /**
    * Object that writes logs into the file.
    */
    private static final Logger logger = Logger.getLogger(NotifyOptimizerOfUpdatedData.class.getName());
        
    /**
     * Default Constructor of the AchieveREInitiator class.
     * 
     * @param a - Agent that initiates the Protocol.
     * @param msg - First message to be sent.
     */
    public NotifyOptimizerOfUpdatedData(Agent a, ACLMessage msg) {
        super(a, msg);
    }
    
    /**
     * Method to create the message that starts this FIPA Request Protocol. The
     * content contains the agent data to be sent to the optimizer.
     * 
     * @param agent - the receiver agent.
     * @param content - Content of the message to be sent.
     * @return Object ACLMessage with the message that starts the Protocol.
     */
//    public static ACLMessage buildMessage(Agent agent, String content) {        
//        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
//        msg.setOntology(Constants.ONTO_UPDATE_CPA_PERFORMANCE_DATA);
//        msg.setContent(content);
//        DFAgentDescription[] dfad = DFInteraction.SearchInDF(Constants.DF_PRODUCTION_OPTIMIZER, agent);   
//        msg.addReceiver(dfad[0].getName());
//        return msg;
//    }
    public static ACLMessage buildMessage(Agent agent, RawProductData rawProductData) {        
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.ONTO_UPDATE_CPA_PERFORMANCE_DATA);
        try {
            msg.setContentObject(rawProductData);
        } catch (IOException ex) {
            logger.error(ex);
        }
        DFAgentDescription[] dfad = DFInteraction.SearchInDF(Constants.DF_PRODUCTION_OPTIMIZER, agent);   
        msg.addReceiver(dfad[0].getName());
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
        logger.info("Optimizer acknowledged new data from product " + myAgent.getLocalName() + ".");
    }
    
}
