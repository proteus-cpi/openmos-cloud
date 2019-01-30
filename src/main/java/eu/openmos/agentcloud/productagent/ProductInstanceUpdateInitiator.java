package eu.openmos.agentcloud.productagent;

import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.ProductInstance;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author User
 */
public class ProductInstanceUpdateInitiator extends AchieveREInitiator {
    private static final Logger logger = Logger.getLogger(ProductInstanceUpdateInitiator.class);   
    
    public static ACLMessage buildMessage(Agent agent, ProductInstance p)  {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.ONTO_PRODUCT_INSTANCE_UPDATE);
        try {
            msg.setContentObject(p);
        } catch (IOException ex) {
            logger.error(ex);
        }
        DFAgentDescription[] dfad = DFInteraction.SearchInDF(Constants.DF_PRODUCTION_OPTIMIZER, agent);   
        msg.addReceiver(dfad[0].getName());
        return msg;
    }
    
    public ProductInstanceUpdateInitiator(Agent a, ACLMessage msg) {
        super(a, msg);
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        logger.info("Optimizer acknowledged new product instance update message from agent " + myAgent.getLocalName() + ".");
    }
    
}
