package eu.openmos.agentcloud.agent.initiators;

import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import eu.openmos.model.SubSystemRecipes;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * Notifies the optimizer whenever a workstation needs to update its internal recipes list.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 * @author Pedro Monteiro <pedro.monteiro@uninova.pt>
 * 
 */
public class WorkstationRecipesUpdateInitiator extends AchieveREInitiator {
    /**
    * Object that writes logs into the file.
    */
    private static final Logger logger = Logger.getLogger(WorkstationRecipesUpdateInitiator.class);   
    
    /**
     * Method to create the message that starts this FIPA Request Protocol. The
     * message contains:
     * 
     * the complete cyberphysical agent description object
     * 
     * @param agent - the receiver agent.
     * @param content - Content of the message to be sent.
     * @return Object ACLMessage with the message that starts the Protocol.
     */
    public static ACLMessage buildMessage(Agent agent, SubSystemRecipes subSystemRecipes) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.ONTO_WORKSTATION_RECIPES_UPDATE);
        try {
            msg.setContentObject(subSystemRecipes);
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
    public WorkstationRecipesUpdateInitiator(Agent a, ACLMessage msg) {
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
        logger.info("Optimizer acknowledged new workstation recipes update message from agent " + myAgent.getLocalName() + ".");
    }
    
}
