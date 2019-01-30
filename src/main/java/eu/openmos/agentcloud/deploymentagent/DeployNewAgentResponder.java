package eu.openmos.agentcloud.deploymentagent;

import eu.openmos.agentcloud.agent.initiators.WorkstationInternalUpdateInitiator;
import eu.openmos.agentcloud.agent.initiators.WorkstationUpdateInitiator;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import eu.openmos.agentcloud.cyberphysicalagent.CyberPhysicalAgent;
import eu.openmos.agentcloud.productagent.ProductAgent;
import eu.openmos.agentcloud.resourceagent.ResourceAgent;
import eu.openmos.agentcloud.transportagent.TransportAgent;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.SubSystem;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import org.apache.log4j.Logger;

/**
 * FIPA Protocol responder that is triggered when the Cloud Interface Agent asks
 * to deploy a new agent. It deserealizes the content of the message and then
 * launches a new agent according to its description.
 * 
 * Content: Serialized form of a CyberPhysicalAgentDescription object. 
 * Counterpart: Cloud Interface Agent
 * Ontology: DEPLOY_NEW_AGENT.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Luis Ribeiro <luis.ribeiro@liu.se>
 *
 */
public class DeployNewAgentResponder extends AchieveREResponder {
    /**
    * Object that writes logs into the file.
    */
    private static final Logger logger = Logger.getLogger(DeployNewAgentResponder.class);

    /**
     * Default AchieveREResponder constructor.
     * 
     * @param a - the agent that will hold this behaviour.
     * @param mt - the template for the messages to which this behaviour will
     * listen to.
     */
    public DeployNewAgentResponder(Agent a, MessageTemplate mt) {
        super(a, mt);
    }

    /**
     * Method that handles the request message that will come from the counterpart.
     * 
     * @param request - message that triggers the protocol.
     * @return Agree or Failure message.
     * @throws NotUnderstoodException - default exception
     * @throws RefuseException - default exception
     */
    @Override
    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
        try {
            ACLMessage response = request.createReply();
            logger.info("Received message from " + request.getSender() + " with contentobject: " + request.getContentObject());
            if (request.getContentObject()!= null) {
                response.setPerformative(ACLMessage.AGREE);
            } else {
                response.setPerformative(ACLMessage.FAILURE);
            }
            return response;
        } catch (UnreadableException ex) {
                logger.error(ex);
                return null;
        }
    }

    /**
     * Method that prepares the message that terminates the Protocol.
     * 
     * @param request - message that triggers the protocol.
     * @param response - the agree or failure message sent in response.
     * @return Inform or Failure message.
     * @throws FailureException - Default exception.
     */
    @Override
    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
        try {
            SubSystem cpad = (SubSystem) request.getContentObject();
            ACLMessage notification = request.createReply();
//            if(deployDeserializedAgent(cpad)){
                int deploymentStatus = deployDeserializedAgent(cpad);
            if (deploymentStatus == 0)  // ok, new, created
            {
                notification.setPerformative(ACLMessage.INFORM);
                        notification.setContent(cpad.getUniqueId());
            } else if (deploymentStatus == 2)  // not that ok, existing, update requested
            {
                notification.setPerformative(ACLMessage.INFORM);
                        notification.setContent(cpad.getUniqueId());
            }else   // error during agent creation
            {
                notification.setPerformative(ACLMessage.FAILURE);
                        notification.setContent(cpad.getUniqueId());
            }
            return notification;
        } catch (UnreadableException ex) {
            logger.error(ex);
            return null;
        }
    }

    /**
     * Auxiliary method that deploys an agent with the received Cyber Physical
     * Agent Description parameter.
     * 
     * @param cpad - the description of the agent to be deployed.
     * OLD @return true if deployed, false otherwise.
     * @return 0 if agent deployed, 1 if error, 2 if agent existing and try to update its data
     */
//    private boolean deployDeserializedAgent(SubSystem cpad) {
    private int deployDeserializedAgent(SubSystem cpad) {
        CyberPhysicalAgent cpsa = null;
        switch (cpad.getSsType()) {
            case Constants.DF_RESOURCE:
                cpsa = new ResourceAgent(cpad.getPhysicalLocation(), cpad.getLogicalLocation(), cpad);
                break;
            case Constants.DF_TRANSPORT:
                cpsa = new TransportAgent(cpad.getPhysicalLocation(), cpad.getLogicalLocation(), cpad);
                break;
            case Constants.DF_PRODUCT:      // TODO never happens, to be removed
                cpsa = new ProductAgent(cpad.getPhysicalLocation(), cpad.getLogicalLocation(), cpad);
                break;
        }
        try {
//            AgentController controller = this.myAgent.getContainerController().acceptNewAgent(cpad.getName(), cpsa);
            AgentController controller = this.myAgent.getContainerController().acceptNewAgent(cpad.getUniqueId(), cpsa);
            logger.info("Agent created -> " + cpad.getUniqueId());
            controller.start();
            // return true;
            return 0;
        } catch (StaleProxyException ex) {
            System.err.println("The agent you are trying to create with id " + cpad.getUniqueId() + " is outdated. Trying to update it");
            logger.warn("The agent you are trying to create with id " + cpad.getUniqueId() + " is outdated. Trying to update it");

            // VaG - 07/12/2017
            // When the agent already exists, maybe the MSB crashed and restarted,
            // so let's try to update current agent data.
            logger.debug(getClass().getName() + " - agent exists, so try to update workstation data with current = [" + cpad + "]");

//            AID agentToBeUpdated = new AID(cpad.getUniqueId(), false);
//            ACLMessage updateMessage = WorkstationUpdateInitiator.buildMessage(myAgent, cpad);
//            updateMessage.addReceiver(agentToBeUpdated);
//            myAgent.addBehaviour(new WorkstationInternalUpdateInitiator(myAgent, updateMessage));
            ACLMessage updateMessage = WorkstationUpdateInitiator.buildMessage(myAgent, cpad);
            myAgent.addBehaviour(new WorkstationUpdateInitiator(myAgent, updateMessage));
            
            // return false;
            // return true;
            return 2;
        } catch (Exception e)
        {
            System.err.println("Error during agent creation with id " + cpad.getUniqueId() + " -> " + e.getLocalizedMessage());
            logger.error("Error during agent creation with id " + cpad.getUniqueId() + " -> " + e.getLocalizedMessage());
            // any other error
            return 1;
        }
    }
}
