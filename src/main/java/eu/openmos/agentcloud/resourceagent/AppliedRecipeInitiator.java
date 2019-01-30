/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.resourceagent;

import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import eu.openmos.model.RecipeExecutionData;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import jade.util.leap.Iterator;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 * Notifies the product when the recipe has been applied.
 * 
 * Content: Serialized form of a recipe object.
 * Counterpart: Optimizer Agent
 * Ontology: ONTO_APPLIED_RECIPE.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * 
 */
public class AppliedRecipeInitiator extends AchieveREInitiator {
    /**
    * Object that writes logs into the file.
    */
    private static final Logger logger = Logger.getLogger(AppliedRecipeInitiator.class.getName());
    /**
    * Message stored in case there is the need to retry.
    */
    private static ACLMessage msg;
    
    /**
     * Method to create the message that starts this FIPA Request Protocol. The
     * content contains the order to be deployed.
     * 
     * @param agent - the receiver agent.
     * @param content - Content of the message to be sent.
     * @return Object ACLMessage with the message that starts the Protocol.
     */
//    public static ACLMessage buildMessage(Agent agent, String content, String productAgentName) {
//        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
//        msg.setOntology(Constants.ONTO_APPLIED_RECIPE);
//        msg.setContent(content);
//        DFAgentDescription[] dfad = DFInteraction.SearchInDF(Constants.DF_PRODUCT, agent);
//        Arrays.asList(dfad).forEach((agentDesc) -> {
//            if(((ServiceDescription) agentDesc.getAllServices().next()).getName().equals(productAgentName)) msg.addReceiver(agentDesc.getName());
//        });
//        return msg;
//    }
    public static ACLMessage buildMessage(Agent agent, RecipeExecutionData recipeExecutionData, String productAgentName) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.ONTO_APPLIED_RECIPE);
        try {
            msg.setContentObject(recipeExecutionData);
        } catch (IOException ex) {
            logger.error(ex);
        }
        DFAgentDescription[] dfad = DFInteraction.SearchInDF(Constants.DF_PRODUCT, agent);
        Arrays.asList(dfad).forEach((agentDesc) -> {
            if(((ServiceDescription) agentDesc.getAllServices().next()).getName().equals(productAgentName)) msg.addReceiver(agentDesc.getName());
        });
        return msg;
    }
 
    /**
     * Default Constructor of the AchieveREInitiator class.
     * 
     * @param a - Agent that initiates the Protocol.
     * @param msg - First message to be sent.
     */
    public AppliedRecipeInitiator(Agent a, ACLMessage msg) {
        super(a, msg);
        this.msg = msg;
    }

    /**
     * Handle inform method that receives an inform message if any comes from the
     * Protocol. 
     * .
     * @param inform - The message sent by the counterpart agent.
     */
    @Override
    protected void handleInform(ACLMessage inform) {
        logger.info("Product " + inform.getSender().getLocalName() + " updated its skill requirements and will notify the optimizer.");
    }
 
    /**
     * Handle failure that retries the Protocol if it fails.
     * .
     * @param failure - The message sent by the counterpart agent.
     */
    @Override
    protected void handleFailure(ACLMessage failure) {
        logger.info("Product " + failure.getSender().getLocalName() + " failed to update its skill requirements. Retrying..");
        myAgent.addBehaviour(new AppliedRecipeInitiator(myAgent, this.msg));
    }
    
}
