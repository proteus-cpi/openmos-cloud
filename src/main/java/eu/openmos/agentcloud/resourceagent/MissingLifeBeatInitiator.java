/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.resourceagent;

import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import org.apache.log4j.Logger;

/**
 * Notifies the optimizer whenever a missing heart beat is detected.
 * 
 * Content: Empty.
 * Counterpart: Optimizer Agent
 * Ontology: ONTO_MISSED_LB.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * 
 */
public class MissingLifeBeatInitiator extends AchieveREInitiator {
    /**
    * Object that writes logs into the file.
    */
    private static final Logger logger = Logger.getLogger(MissingLifeBeatInitiator.class);   
    
    /**
     * Method to create the message that starts this FIPA Request Protocol. The
     * content contains the order to be deployed.
     * 
     * @param agent - the receiver agent.
     * @return Object ACLMessage with the message that starts the Protocol.
     */
    public static ACLMessage buildMessage(Agent agent) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setContent("Missed Life Beat.");
        msg.setOntology(Constants.ONTO_MISSED_LB);
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
    public MissingLifeBeatInitiator(Agent a, ACLMessage msg) {
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
        logger.info("Optimizer acknowledged new missing life beat from resource " + myAgent.getLocalName() + ".");
    }
    
}
