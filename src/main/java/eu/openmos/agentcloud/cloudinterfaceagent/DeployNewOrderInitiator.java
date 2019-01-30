/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.cloudinterfaceagent;

import eu.openmos.model.OrderInstance;
import eu.openmos.model.ProductInstance;
import eu.openmos.agentcloud.servicebus.MSBHelper;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 * Initiator that is to be launched whenever the CIA (Cloud Interface Agent) is 
 * notified to deploy a new order. This will start a FIPA Request Protocol that
 * will request the deployment agent to deploy the requested order (product 
 * agents).
 * 
 * Content: Serialized form of an Order object. 
 * Counterpart: Deployment Agent.
 * Ontology: ONTO_DEPLOY_NEW_ORDER.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * 
 */
public class DeployNewOrderInitiator extends AchieveREInitiator {
    /**
     * Object that writes logs into the file.
     */
    private static final Logger logger = Logger.getLogger(DeployNewOrderInitiator.class);
    
    /**
     * Method to create the message that starts this FIPA Request Protocol. The
     * content contains a serialized form of and Order object.
     * 
     * @param receiver - AID of the receiver Agent.
     * @param content - Content of the message to be sent.
     * @return Object ACLMessage with the message that starts the Protocol.
     */
//    public static ACLMessage buildMessage(AID receiver, String content) {
    public static ACLMessage buildMessage(AID receiver, OrderInstance order) {
        logger.debug("DeployNewOrderInitiator - THIS IS AN ORDER: " + order);
        
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.ONTO_DEPLOY_NEW_ORDER);
        msg.addReceiver(receiver);
        try {
            //        msg.setContent(content);
            msg.setContentObject(order);
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
    public DeployNewOrderInitiator(Agent a, ACLMessage msg) {
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
        logger.info("New order deployed successfully.");

        // call service made available by the msb.  
        logger.debug("Call service to inform the MSB.");
//        String orderId = inform.getContent();

//        Order order = Order.fromString(inform.getContent());
        OrderInstance order;
        try {
            order = (OrderInstance)inform.getContentObject();
            List<String> detailIds = new LinkedList<>();
            for (ProductInstance pd : order.getProductInstances())
                detailIds.add(pd.getUniqueId());
            MSBHelper.confirmOrderCreated(order.getUniqueId(), detailIds);
        } catch (UnreadableException ex) {
            logger.error(ex);
        }
    }

    @Override
    protected void handleFailure(ACLMessage failure) {
        logger.debug("New order was not deployed: " + failure.getContent());
        
        // call service made available by the msb.  
        logger.debug("Call service to inform the MSB.");
        String orderId = failure.getContent();
        MSBHelper.notifyOrderNotCreated(orderId);
    }
    
    
    
}
