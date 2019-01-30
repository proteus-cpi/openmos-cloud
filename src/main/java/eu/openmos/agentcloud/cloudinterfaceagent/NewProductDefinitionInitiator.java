package eu.openmos.agentcloud.cloudinterfaceagent;

import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import eu.openmos.model.Product;
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
public class NewProductDefinitionInitiator extends AchieveREInitiator {
    private static Logger logger = Logger.getLogger(NewProductDefinitionInitiator.class.getName());

    public NewProductDefinitionInitiator(Agent a, ACLMessage msg) {
        super(a, msg);
    }

    public static ACLMessage buildMessage(Agent agent, Product product) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.ONTO_NEW_PRODUCT_DEFINITION);
        DFAgentDescription[] dfad = DFInteraction.SearchInDF(Constants.DF_PRODUCTION_OPTIMIZER, agent);   
        msg.addReceiver(dfad[0].getName());
        try {
            msg.setContentObject(product);
        } catch (IOException ex) {
            logger.error(ex);
        }
        return msg;
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        System.out.println("New product definition -- Processing inform from " + inform.getSender().getLocalName());
    }

    @Override
    protected void handleFailure(ACLMessage failure) {
        System.out.println("New product definition  -- Processing failure from " + failure.getSender().getLocalName());
    }

}
