package eu.openmos.agentcloud.agent.initiators;

import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import eu.openmos.model.ExecutionTable;
import eu.openmos.model.SubSystem;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * Notifies the optimizer whenever a workstation needs to update its internal execution table.
 * 
 * Content: message coming from the MSB via socket.
 * Counterpart: Optimizer Agent
 * Ontology: ONTO_EXECUTION_TABLE_UPDATE.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 * @author Pedro Monteiro <pedro.monteiro@uninova.pt>
 * 
 */
public class ExecutionTableUpdateInitiator extends AchieveREInitiator {
    /**
    * Object that writes logs into the file.
    */
    private static final Logger logger = Logger.getLogger(ExecutionTableUpdateInitiator.class);   
    
    /**
     * Method to create the message that starts this FIPA Request Protocol. The
     * message contains:
     * 
     * the complete execution table object
     * 
     * @param agent - the receiver agent.
     * @param content - Content of the message to be sent.
     * @return Object ACLMessage with the message that starts the Protocol.
     */
    public static ACLMessage buildMessage(Agent agent, ExecutionTable executionTable) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.ONTO_UPDATE_EXECUTION_TABLE);
        try {
            msg.setContentObject(executionTable);
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
    
    /**
     * Default Constructor of the AchieveREInitiator class.
     * 
     * @param a - Agent that initiates the Protocol.
     * @param msg - First message to be sent.
     */
    public ExecutionTableUpdateInitiator(Agent a, ACLMessage msg) {
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
        logger.info("Optimizer acknowledged new execution table update message from agent " + myAgent.getLocalName() + ".");

//        // call service made available by the msb
//        logger.debug("Call service to inform the MSB.");
//        String equipmentId = inform.getContent();
//        MSBHelper.confirmAgentRemoved(equipmentId);
        
    }
    
}
