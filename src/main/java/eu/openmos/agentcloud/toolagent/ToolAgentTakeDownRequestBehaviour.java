/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.toolagent;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import org.apache.log4j.Logger;

/**
 * JADE behaviour that takes care of the removal of a tool agent.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class ToolAgentTakeDownRequestBehaviour extends AchieveREResponder {

    private static final Logger logger = Logger.getLogger(ToolAgentTakeDownRequestBehaviour.class);    
    private final long deletionDelay; 
    
    /**
     * Constructor to inizialize the object.
     * 
     * @param a    the tool agent itself
     * @param mt   the message template matching the message type and the ontology
     * @param deletionDelay   time after which the agent should suicide (in milliseconds)
     */
    public ToolAgentTakeDownRequestBehaviour(Agent a, MessageTemplate mt, long deletionDelay) {
        super(a, mt);
        this.deletionDelay = deletionDelay;
    }
    
    /**
     * It handles the request and kills the agent.
     * 
     * @param request message of type request that asks for the suicide of the agent 
     * @return the agree message 
     * @throws NotUnderstoodException
     * @throws RefuseException 
     */
    @Override
    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
        logger.debug(myAgent.getLocalName() + " - " + request.toString());
        
        ACLMessage agree = request.createReply();
        agree.setPerformative(ACLMessage.AGREE);
        agree.setContent("");                
        
        myAgent.doDelete();
        
        return agree;
        
    }        
}
