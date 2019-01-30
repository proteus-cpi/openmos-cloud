package eu.openmos.agentcloud.cloudinterfaceagent;

import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import eu.openmos.model.SubSystemChangeStage;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
* @author Valerio Gentile <valerio.gentile@we-plus.eu>
*/
public class NewSubSystemStageChangeInitiator extends AchieveREInitiator {
    private static Logger logger = Logger.getLogger(NewSubSystemStageChangeInitiator.class.getName());

    public NewSubSystemStageChangeInitiator(Agent a, ACLMessage msg) {
        super(a, msg);
    }

    public static ACLMessage buildMessage(Agent agent, SubSystemChangeStage subSystemChangeState) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.ONTO_NEW_SUBSYSTEM_STAGE_CHANGE);
        DFAgentDescription[] dfad = DFInteraction.SearchInDF(Constants.DF_PRODUCTION_OPTIMIZER, agent);   
        msg.addReceiver(dfad[0].getName());
        try {
            msg.setContentObject(subSystemChangeState);
        } catch (IOException ex) {
            logger.error(ex);
        }
        return msg;
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        System.out.println("New subsystem status change -- Processing inform from " + inform.getSender().getLocalName());
    }

    @Override
    protected void handleFailure(ACLMessage failure) {
        System.out.println("New subsystem status change  -- Processing failure from " + failure.getSender().getLocalName());
    }

}
