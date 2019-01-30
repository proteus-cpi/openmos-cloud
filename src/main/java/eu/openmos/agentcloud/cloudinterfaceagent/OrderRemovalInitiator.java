/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.cloudinterfaceagent;

import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

/**
* WP4 Cloud Platform Re-work related code.
* 
* @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
* @author Valerio Gentile <valerio.gentile@we-plus.eu>
*/
public class OrderRemovalInitiator extends AchieveREInitiator {

    public OrderRemovalInitiator(Agent a, ACLMessage msg) {
        super(a, msg);
    }

    public static ACLMessage buildMessage(Agent agent, String content) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.ONTO_ORDER_REMOVAL);
        DFAgentDescription[] dfad = DFInteraction.SearchInDF(Constants.DF_PRODUCTION_OPTIMIZER, agent);   
        msg.addReceiver(dfad[0].getName());
        msg.setContent(content);
        return msg;
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        System.out.println("Order Removal -- Processing inform from " + inform.getSender().getLocalName());
    }

    @Override
    protected void handleFailure(ACLMessage failure) {
        System.out.println("Order Removal -- Processing failure from " + failure.getSender().getLocalName());
    }

}
