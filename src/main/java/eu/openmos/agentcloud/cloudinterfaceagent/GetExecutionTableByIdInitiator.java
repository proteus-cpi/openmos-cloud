package eu.openmos.agentcloud.cloudinterfaceagent;

import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import org.apache.log4j.Logger;

/**
* WP4 Cloud Platform Re-work related code.
* 
* @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
* @author Valerio Gentile <valerio.gentile@we-plus.eu>
*/
public class GetExecutionTableByIdInitiator extends AchieveREInitiator {
    private static final Logger logger = Logger.getLogger(GetExecutionTableByIdInitiator.class);

    public GetExecutionTableByIdInitiator(Agent a, ACLMessage msg) {
        super(a, msg);
    }

    public static ACLMessage buildMessage(Agent agent) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.ONTO_GET_EXECUTION_TABLE_BY_ID);
        DFAgentDescription[] dfad = DFInteraction.SearchInDF(Constants.DF_PRODUCTION_OPTIMIZER, agent);   
        msg.addReceiver(dfad[0].getName());
        return msg;
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        System.out.println("Get executionTableById -- Processing inform from " + inform.getSender().getLocalName());
    }

    @Override
    protected void handleFailure(ACLMessage failure) {
        System.out.println("Get executionTableById -- Processing failure from " + failure.getSender().getLocalName());
    }

}
