package eu.openmos.agentcloud.deploymentagent;

import eu.openmos.agentcloud.cloudinterfaceagent.StartedProductInitiator;
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
import org.apache.log4j.Logger;

/**
 * Deploys new product agents in the system, according to the order that comes 
 * from the Cloud Interface Agent.
 * 
 * Content: Serialized form of an Order object. 
 * Counterpart: Cloud Interface Agent
 * Ontology: ONTO_DEPLOY_NEW_PRODUCT_INSTANCE.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * 
 */
public class DeployNewProductInstanceResponder extends AchieveREResponder {
    /**
    * Object that writes logs into the file.
    */
    private static final Logger logger = Logger.getLogger(DeployNewProductInstanceResponder.class);
    
    /**
     * Default AchieveREResponder constructor.
     * 
     * @param a - the agent that will hold this behaviour.
     * @param mt - the template for the messages to which this behaviour will
     * listen to.
     */
    public DeployNewProductInstanceResponder(Agent a, MessageTemplate mt) {
        super(a, mt);
    }

    /**
     * Method that handles the request message that will come from the counterpart.
     * It deploys one product agent at a time.
     * 
     * @param request - message that triggers the protocol.
     * @return Agree or Failure message.
     * @throws NotUnderstoodException - default exception
     * @throws RefuseException - default exception
     */
    @Override
    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
        logger.debug("DeployNewProductInstanceResponder: start");
        ProductInstance p = null;
        try {
            p = (ProductInstance)request.getContentObject();
        } catch (UnreadableException ex) {
            throw new RefuseException(ex.getLocalizedMessage());
        }
            
        ACLMessage inform = request.createReply();
        try {
            inform.setContentObject(p);
        } catch (IOException ex) {
            throw new RefuseException(ex.getLocalizedMessage());
        }
        
        inform.setPerformative(ACLMessage.INFORM);
        
        // VaG - 26/10/2017
        // Product agent creation.
        logger.debug(getClass().getName() + " - " + this.myAgent.getLocalName() + " - " + p.getUniqueId());
        // ProductAgent pa = new ProductAgent();
        // TODO we miss the productinstance object into the agent memory
        // VaG - 06/11/2017
        // Added constructor with product instance parameter
        ProductAgent pa = new ProductAgent(p);
        
        try {
            AgentController controller = this.myAgent.getContainerController().acceptNewAgent(p.getUniqueId(), pa);
            controller.start();                    
        } catch (StaleProxyException ex) {
            inform.setPerformative(ACLMessage.FAILURE);
            inform.setContent(p.getUniqueId());
            logger.error("The agent you are trying to create is outdated. Please, try again.");
        }

        if(inform.getPerformative() == ACLMessage.INFORM)
            myAgent.addBehaviour(new StartedProductInitiator(myAgent, StartedProductInitiator.buildMessage(myAgent, p)));
        return inform;
    }

}
