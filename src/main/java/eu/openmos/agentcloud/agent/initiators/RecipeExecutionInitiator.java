/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.agent.initiators;

import eu.openmos.model.RecipeExecutionData;
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
 * Notifies the optimizer whenever some recipe execution data come from the MSB.
 * 
 * Content: message coming from the MSB via socket.
 * Counterpart: Optimizer Agent
 * Ontology: ONTO_RECIPE_EXECUTION.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 * @author Pedro Monteiro <pedro.monteiro@uninova.pt>
 * 
 */
public class RecipeExecutionInitiator extends AchieveREInitiator {
    /**
    * Object that writes logs into the file.
    */
    private static final Logger logger = Logger.getLogger(RecipeExecutionInitiator.class);   
    
    /**
     * Method to create the message that starts this FIPA Request Protocol. The
     * message contains:
     * 
    private String productId;
    private String recipeId;
    private List<String> kpiIds;
    private List<String> parameterIds;
    private Date registeredTimestamp;
     * 
     * @param agent - the receiver agent.
     * @param content - Content of the message to be sent.
     * @return Object ACLMessage with the message that starts the Protocol.
     */
    public static ACLMessage buildMessage(Agent agent, RecipeExecutionData recipeExecutionData) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.ONTO_RECIPE_EXECUTION);
        try {
            msg.setContentObject(recipeExecutionData);
        } catch (IOException ex) {
            logger.error(ex);
        }
        DFAgentDescription[] dfad = DFInteraction.SearchInDF(Constants.DF_PRODUCTION_OPTIMIZER, agent);   
        if (dfad == null || dfad.length != 1)
        {
            logger.error("Optimizer agent not found, did it crash? Exiting...");
            throw new RuntimeException("Optimizer agent not found, did it crash? Exiting...");
        }
        msg.addReceiver(dfad[0].getName());
        return msg;
    }
//    public static ACLMessage buildMessage(Agent agent, String content) {
//        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
//        msg.setOntology(Constants.ONTO_RECIPE_EXECUTION);
//        msg.setContent(content);
//        DFAgentDescription[] dfad = DFInteraction.SearchInDF(Constants.DF_PRODUCTION_OPTIMIZER, agent);   
//        msg.addReceiver(dfad[0].getName());
//        return msg;
//    }
    
    /**
     * Default Constructor of the AchieveREInitiator class.
     * 
     * @param a - Agent that initiates the Protocol.
     * @param msg - First message to be sent.
     */
    public RecipeExecutionInitiator(Agent a, ACLMessage msg) {
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
        logger.info("Optimizer acknowledged new recipe execution data message from agent " + myAgent.getLocalName() + ".");
    }
    
}
