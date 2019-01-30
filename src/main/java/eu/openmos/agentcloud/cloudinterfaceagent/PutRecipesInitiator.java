/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.cloudinterfaceagent;

import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import eu.openmos.model.Recipe;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
* WP4 Cloud Platform Re-work related code.
* 
* @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
* @author Valerio Gentile <valerio.gentile@we-plus.eu>
*/
public class PutRecipesInitiator extends AchieveREInitiator {
    private static final Logger logger = Logger.getLogger(PutRecipesInitiator.class);

    public PutRecipesInitiator(Agent a, ACLMessage msg) {
        super(a, msg);
    }

    public static ACLMessage buildMessage(Agent agent, Recipe[] recipes) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.ONTO_PUT_RECIPES);
        DFAgentDescription[] dfad = DFInteraction.SearchInDF(Constants.DF_PRODUCTION_OPTIMIZER, agent);   
        msg.addReceiver(dfad[0].getName());
        try {
            //        msg.setContent(content);
            msg.setContentObject(recipes);
        } catch (IOException ex) {
            logger.error(ex);
        }        
        return msg;
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        System.out.println("Put Recipes -- Processing inform from " + inform.getSender().getLocalName());
    }

    @Override
    protected void handleFailure(ACLMessage failure) {
        System.out.println("Put Recipes -- Processing failure from " + failure.getSender().getLocalName());
    }

}
