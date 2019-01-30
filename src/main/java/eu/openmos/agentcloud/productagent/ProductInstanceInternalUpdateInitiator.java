package eu.openmos.agentcloud.productagent;

import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.ProductInstance;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class ProductInstanceInternalUpdateInitiator extends AchieveREInitiator {
    private static final Logger logger = Logger.getLogger(ProductInstanceInternalUpdateInitiator.class);   
    
    public static ACLMessage buildMessage(AID agentAID, ProductInstance p) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.ONTO_PRODUCT_INSTANCE_INTERNAL_UPDATE);
        try {
            msg.setContentObject(p);
        } catch (IOException ex) {
            logger.error(ex);
        }
        msg.addReceiver(agentAID);
        return msg;
    }
    
    public ProductInstanceInternalUpdateInitiator(Agent a, ACLMessage msg) {
        super(a, msg);
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        logger.info("Product agent acknowledged new product instance update message from agent " + myAgent.getLocalName() + ".");
    }
    
}
