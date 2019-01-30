package eu.openmos.agentcloud.cloudinterfaceagent;

import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import eu.openmos.model.ProductInstance;
import eu.openmos.model.StartedProductInfo;
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
public class StartedProductInitiator extends AchieveREInitiator {
    private static Logger logger = Logger.getLogger(StartedProductInitiator.class.getName());

    public StartedProductInitiator(Agent a, ACLMessage msg) {
        super(a, msg);
    }

//    public static ACLMessage buildMessage(Agent agent, String content) {
    public static ACLMessage buildMessage(Agent agent, ProductInstance productInstance)
    {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.ONTO_STARTED_PRODUCT);
        DFAgentDescription[] dfad = DFInteraction.SearchInDF(Constants.DF_PRODUCTION_OPTIMIZER, agent);   
        msg.addReceiver(dfad[0].getName());
        try {
            msg.setContentObject(productInstance);
        } catch (IOException ex) {
            logger.error(ex);
        }
        return msg;
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        System.out.println("Started Product -- Processing inform from " + inform.getSender().getLocalName());
    }

    @Override
    protected void handleFailure(ACLMessage failure) {
        System.out.println("Started Product -- Processing failure from " + failure.getSender().getLocalName());
    }

}
