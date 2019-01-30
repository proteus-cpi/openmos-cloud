/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.deploymentagent;

import eu.openmos.model.OrderInstance;
import eu.openmos.model.ProductInstance;
import eu.openmos.agentcloud.productagent.ProductAgent;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author pedro
 */
public class UpdateOrderResponder extends AchieveREResponder {
    /**
    * Object that writes logs into the file.
    */
    private static final Logger logger = Logger.getLogger(UpdateOrderResponder.class);

    public UpdateOrderResponder(Agent a, MessageTemplate mt) {
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
        OrderInstance order;
        try {
            //            order = Order.fromString(request.getContent());
            order = (OrderInstance)request.getContentObject();
        } catch (UnreadableException ex) {
            throw new RefuseException(ex.getLocalizedMessage());
        }
        
        ACLMessage inform = request.createReply();
        inform.setPerformative(ACLMessage.INFORM);
        for (ProductInstance p : order.getProductInstances()) {
            boolean exists = false;
            List<String> existences = new LinkedList<>();
            logger.debug(getClass().getName() + " - " + this.myAgent.getLocalName() + " - " + p.getName());
            
            DFAgentDescription[] productAgentsList = DFInteraction.SearchInDF(Constants.DF_PRODUCT, this.myAgent);
            for(DFAgentDescription agentDesc : productAgentsList) {
                exists = ((ServiceDescription) agentDesc.getAllServices().next()).getName().equals(p.getUniqueId());
                if(exists) break;
            }
            if(!exists) {
                // VaG - 06/11/2017
                // Added constructor with product instance parameter
                // ProductAgent pa = new ProductAgent();
                ProductAgent pa = new ProductAgent(p);
                try {
                    AgentController controller = this.myAgent.getContainerController().acceptNewAgent(p.getUniqueId(), pa);
                    controller.start();                    
                } catch (StaleProxyException ex) {
                    inform.setPerformative(ACLMessage.FAILURE);
                    logger.error("The agent you are trying to create is outdated. Please, try again.");
                    break;
                }
            }
        }
        if(inform.getPerformative() == ACLMessage.INFORM)
        {
            // TODO check request.getContent() is WRONG! 
            myAgent.addBehaviour(new UpdatedOrderInitiator(myAgent, UpdatedOrderInitiator.buildMessage(myAgent, request.getContent())));
        }
        return inform;
    }
    
}
