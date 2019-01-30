/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.agent.responders;

import eu.openmos.agentcloud.agent.initiators.ExecutionTableUpdateInitiator;
import eu.openmos.agentcloud.cloudinterfaceagent.StartExecutionTableUpdateInitiator;
import eu.openmos.agentcloud.cyberphysicalagent.CyberPhysicalAgent;
import eu.openmos.model.ExecutionTable;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;
import jade.proto.AchieveREResponder;
import org.apache.log4j.Logger;

/**
 *
 * @author valerio.gentile
 */
public class CPAExecutionTableUpdateResponder extends AchieveREResponder {
   private static final org.apache.log4j.Logger logger = Logger.getLogger(CPAExecutionTableUpdateResponder.class);

    public CPAExecutionTableUpdateResponder(Agent a, MessageTemplate mt) {
        super(a, mt);
    }

    @Override
    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
        ExecutionTable et = null;
        try {
            et = (ExecutionTable) request.getContentObject();

            logger.debug("et before update: " + ((CyberPhysicalAgent) myAgent).getCpad().getExecutionTable().toJSON2());

            ((CyberPhysicalAgent) myAgent).getCpad().setExecutionTable(et);

            logger.debug("et after update: " + ((CyberPhysicalAgent) myAgent).getCpad().getExecutionTable().toJSON2());
        } catch (UnreadableException ex) {
            logger.error("CPAExecutionTableUpdateResponder: " + ex);
        }
        
        myAgent.addBehaviour(new ExecutionTableUpdateInitiator(myAgent, ExecutionTableUpdateInitiator.buildMessage(myAgent, et)));
        
        ACLMessage reply = request.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        return reply;
    }
    
}
