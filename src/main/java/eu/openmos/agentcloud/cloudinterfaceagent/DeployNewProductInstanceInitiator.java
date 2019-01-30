package eu.openmos.agentcloud.cloudinterfaceagent;

import eu.openmos.model.ProductInstance;
import eu.openmos.agentcloud.servicebus.MSBHelper;
import eu.openmos.agentcloud.utilities.Constants;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;
import java.io.IOException;
import org.apache.log4j.Logger;

public class DeployNewProductInstanceInitiator extends AchieveREInitiator {
    /**
     * Object that writes logs into the file.
     */
    private static final Logger logger = Logger.getLogger(DeployNewProductInstanceInitiator.class);
    
    /**
     * Method to create the message that starts this FIPA Request Protocol. The
     * content contains a serialized form of and Order object.
     * 
     * @param receiver - AID of the receiver Agent.
     * @param content - Content of the message to be sent.
     * @return Object ACLMessage with the message that starts the Protocol.
     */
//    public static ACLMessage buildMessage(AID receiver, String content) {
    public static ACLMessage buildMessage(AID receiver, ProductInstance productInstance) {
        logger.debug("DeployNewProductInstanceInitiator - THIS IS A PRODUCT INSTANCE: " + productInstance);
        
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.ONTO_DEPLOY_NEW_PRODUCT_INSTANCE);
        msg.addReceiver(receiver);
        try {
            msg.setContentObject(productInstance);
        } catch (IOException ex) {
            logger.error(ex);
        }
        return msg;
    }
    
    /**
     * Default Constructor of the AchieveREInitiator class.
     * 
     * @param a - Agent that initiates the Protocol.
     * @param msg - First message to be sent.
     */
    public DeployNewProductInstanceInitiator(Agent a, ACLMessage msg) {
        super(a, msg);
    }
    
    /**
     * Handle inform method that receives an inform message if any comes from the
     * Protocol. Notifies the Manufacturing Service Bus that the new order has 
     * been deployed.
     * .
     * @param inform - The message sent by the counterpart agent.
     */
    @Override
    protected void handleInform(ACLMessage inform) {
        logger.info("New product instance deployed successfully.");

        // call service made available by the msb.  
        logger.debug("Call service to inform the MSB.");
//        String orderId = inform.getContent();

//        Order order = Order.fromString(inform.getContent());
        ProductInstance productInstance;
        try {
            productInstance = (ProductInstance)inform.getContentObject();
            MSBHelper.confirmProductInstanceCreated(productInstance);
        } catch (UnreadableException ex) {
            logger.error(ex);
        }
    }

    @Override
    protected void handleFailure(ACLMessage failure) {
        logger.debug("New product instance was not deployed: " + failure.getContent());
        
        // call service made available by the msb.  
        logger.debug("Call service to inform the MSB.");
        ProductInstance productInstance = null;
        try {
            productInstance = (ProductInstance)failure.getContentObject();
        } catch (UnreadableException ex) {
            logger.error(ex);
        }
        MSBHelper.notifyProductInstanceNotCreated(productInstance);
    }
    
    
    
}
