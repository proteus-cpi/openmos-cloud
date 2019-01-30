/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.deploymentagent;

import eu.openmos.agentcloud.cloudinterfaceagent.DeployNewOrderInitiator;
import eu.openmos.agentcloud.productagent.ProductAgent;
import eu.openmos.model.OrderInstance;
import eu.openmos.model.ProductInstance;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 * Deploys new product agents in the system, according to the order that comes 
 * from the Cloud Interface Agent.
 * 
 * Content: Serialized form of an Order object. 
 * Counterpart: Cloud Interface Agent
 * Ontology: ONTO_DEPLOY_NEW_ORDER.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * 
 */
public class DeployNewOrderResponder extends AchieveREResponder {
    /**
    * Object that writes logs into the file.
    */
    private static final Logger logger = Logger.getLogger(DeployNewOrderResponder.class);
    
    /**
     * Default AchieveREResponder constructor.
     * 
     * @param a - the agent that will hold this behaviour.
     * @param mt - the template for the messages to which this behaviour will
     * listen to.
     */
    public DeployNewOrderResponder(Agent a, MessageTemplate mt) {
        super(a, mt);
    }

    /**
     * Method that handles the request message that will come from the counterpart.
     * It deploys as many product agents as needed by the order.
     * 
     * @param request - message that triggers the protocol.
     * @return Agree or Failure message.
     * @throws NotUnderstoodException - default exception
     * @throws RefuseException - default exception
     */
    @Override
    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
        logger.debug("DeployNewOrderResponder: THIS IS AN ORDER: [" + request.getContent());
        OrderInstance order;
        try {
            //            order = Order.fromString(request.getContent());
            order = (OrderInstance)request.getContentObject();
        } catch (UnreadableException ex) {
            throw new RefuseException(ex.getLocalizedMessage());
        }
            
        ACLMessage inform = request.createReply();
        try {
            //
//        inform.setContent(order.getUniqueId());
////////////////////        inform.setContent(request.getContent() /* order.toString() */);
            inform.setContentObject(order /* order.toString() */);
        } catch (IOException ex) {
            throw new RefuseException(ex.getLocalizedMessage());
        }
        
        inform.setPerformative(ACLMessage.INFORM);
        
        // VaG - 26/10/2017
        // No product agents creation.
/*        
        for (ProductInstance p : order.getProductInstances()) {
            logger.debug(getClass().getName() + " - " + this.myAgent.getLocalName() + " - " + p.getUniqueId());
            ProductAgent pa = new ProductAgent();
            try {
                AgentController controller = this.myAgent.getContainerController().acceptNewAgent(p.getUniqueId(), pa);
                controller.start();                    
            } catch (StaleProxyException ex) {
                inform.setPerformative(ACLMessage.FAILURE);
                inform.setContent(order.getUniqueId());
                logger.error("The agent you are trying to create is outdated. Please, try again.");
                break;
            }
        }
*/
        if(inform.getPerformative() == ACLMessage.INFORM)
//            myAgent.addBehaviour(new NewlyDeployedOrderInitiator(myAgent, NewlyDeployedOrderInitiator.buildMessage(myAgent, request.getContent())));
            myAgent.addBehaviour(new NewlyDeployedOrderInitiator(myAgent, NewlyDeployedOrderInitiator.buildMessage(myAgent, order)));
        return inform;
    }

}
