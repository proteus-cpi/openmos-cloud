/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.cloudinterfaceagent;

import eu.openmos.agentcloud.servicebus.MSBHelper;
import eu.openmos.agentcloud.utilities.Constants;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import org.apache.log4j.Logger;

/**
 *
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 * 
 * Copied from DeployNewAgentInitiator
 * 
 * Initiator that is to be launched whenever the CIA (Cloud Interface Agent) is 
 * notified to remove an agent. This will start a FIPA Request Protocol that
 * will request the agent itself to suicide.
 * 
 */
public class RemoveAgentInitiator extends AchieveREInitiator 
{
   private static final Logger logger = Logger.getLogger(RemoveAgentInitiator.class);
    
    public static ACLMessage buildMessage(AID receiver, String content) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.REMOVE_AGENT);
        msg.addReceiver(receiver);
        msg.setContent(content);
        logger.debug(new Object() { }.getClass().getEnclosingClass().getName() + " - " + msg.toString());        
        return msg;
    }
    
    public RemoveAgentInitiator(Agent a, ACLMessage msg) {
        super(a, msg);
    }

    @Override
    protected void handleAgree(ACLMessage agree) {
        logger.info(getClass().getName() + " - Agent being removed...");
    }  

    @Override
    protected void handleRefuse(ACLMessage refuse) {
        logger.info(getClass().getName() + " - Agent refused to remove itself.");
    }  
    
    @Override
    protected void handleInform(ACLMessage inform) {        
        logger.info(getClass().getName() + " - Agent removed succesfully..");

        // call service made available by the msb
        logger.debug("Call service to inform the MSB.");
        String equipmentId = inform.getContent();
        MSBHelper.confirmAgentRemoved(equipmentId);
    }

    @Override
    protected void handleFailure(ACLMessage failure) {
        logger.info(getClass().getName() + " - Agent was not removed.");

        // call service made available by the msb
        logger.debug("Call service to inform the MSB.");
        String equipmentId = failure.getContent();
        MSBHelper.notifyAgentNotRemoved(equipmentId);
    }
    
}
