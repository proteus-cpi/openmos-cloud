/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.cloudinterfaceagent;

import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import eu.openmos.model.Skill;
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
public class CreateNewSkillInitiator extends AchieveREInitiator {
    private static final Logger logger = Logger.getLogger(CreateNewSkillInitiator.class);    

    public CreateNewSkillInitiator(Agent a, ACLMessage msg) {
        super(a, msg);
    }

/*    
    public static ACLMessage buildMessage(Agent agent, String content) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.ONTO_PROCESS_NEW_SKILL_ON_THE_SYSTEM);
        DFAgentDescription[] dfad = DFInteraction.SearchInDF(Constants.DF_PRODUCTION_OPTIMIZER, agent);   
        msg.addReceiver(dfad[0].getName());
        msg.setContent(content);
        return msg;
    }
*/
    public static ACLMessage buildMessage(Agent agent, Skill skill) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.ONTO_PROCESS_NEW_SKILL_ON_THE_SYSTEM);
        DFAgentDescription[] dfad = DFInteraction.SearchInDF(Constants.DF_PRODUCTION_OPTIMIZER, agent);   
        msg.addReceiver(dfad[0].getName());
        try {
            //        msg.setContent(content);
            msg.setContentObject(skill);
        } catch (IOException ex) {
            logger.error(ex);
        }
        return msg;
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        System.out.println("Create New Skill -- Processing inform from " + inform.getSender().getLocalName());
    }

    @Override
    protected void handleFailure(ACLMessage failure) {
        System.out.println("Create New Skill -- Processing failure from " + failure.getSender().getLocalName());
    }

}
