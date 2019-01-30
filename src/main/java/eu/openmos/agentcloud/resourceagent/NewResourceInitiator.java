/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.resourceagent;

import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import eu.openmos.model.SubSystem;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * Notifies the optimizer of a new resource agent in the system.
 * 
 * Content: Serialized form of a CyberPhysicalAgentDescription object.
 * Counterpart: Optimizer Agent
 * Ontology: ONTO_REGISTER_CPA_IN_OPTIMIZER.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Andre Rocha <andre.rocha@uninova.pt>
 */
public class NewResourceInitiator extends AchieveREInitiator {
    /**
    * Object that writes logs into the file.
    */
    private static final Logger logger = Logger.getLogger(NewResourceInitiator.class);    
    /**
    * Message stored in case there is the need to retry.
    */
    private ACLMessage msg;
    
    /**
     * Method to create the message that starts this FIPA Request Protocol. The
     * content contains the order to be deployed.
     * 
     * @param agent - the receiver agent.
     * @param content - Content of the message to be sent.
     * @return Object ACLMessage with the message that starts the Protocol.
     */
/*    
    public static ACLMessage BuildMessage(Agent agent, String content){
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.ONTO_REGISTER_CPA_IN_OPTIMIZER);        
        DFAgentDescription[] dfad = DFInteraction.SearchInDF(Constants.DF_PRODUCTION_OPTIMIZER, agent);   
        msg.addReceiver(dfad[0].getName());
        msg.setContent(content);
        return msg;
    }
*/    
    public static ACLMessage buildMessage(Agent agent, SubSystem subSystem){
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.ONTO_REGISTER_CPA_IN_OPTIMIZER);        
        DFAgentDescription[] dfad = DFInteraction.SearchInDF(Constants.DF_PRODUCTION_OPTIMIZER, agent);   
        msg.addReceiver(dfad[0].getName());
        
        // msg.setContentObject(content);
        try {
            msg.setContentObject(subSystem);
        } catch (IOException ex) {
            logger.error(ex);
        }
        
        return msg;
    }
 
    /**
     * Default Constructor of the AchieveREInitiator class.
     * 
     * @param a - Agent that initiates the Protocol.
     * @param msg - First message to be sent.
     */
    public NewResourceInitiator(Agent a, ACLMessage msg) {
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
        logger.info("Optimizer acknowledged the existence of resource " + myAgent.getLocalName() + ".");
    }
 
    /**
     * Handle failure that retries the Protocol if it fails.
     * .
     * @param failure - The message sent by the counterpart agent.
     */
    @Override
    protected void handleFailure(ACLMessage failure) {
        logger.warn("Optimizer failed to acknowledged the existence of resource " + myAgent.getLocalName() + ". Retrying..");
        ((ResourceAgent) myAgent).addBehaviour(new NewResourceInitiator(myAgent, this.msg));
    }
    
}
