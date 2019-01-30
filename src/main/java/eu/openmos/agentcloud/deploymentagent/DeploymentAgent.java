/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.deploymentagent;

import eu.openmos.agentcloud.toolagent.ToolAgent;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.apache.log4j.Logger;

/**
 * Agent responsible for deploying new orders and resources in the cloud 
 * according to the information it receives from the Cloud Interface Agent.
 * 
 * @author Pedro Lima <pedro.monteiro@uninova.pt>
 * @author Andre Rocha <andre.rocha@uninova.pt>
 */
public class DeploymentAgent extends ToolAgent {
    /**
    * Object that writes logs into the file.
    */
    private static final Logger logger = Logger.getLogger(DeploymentAgent.class);

    /**
     * Setup method for the Deployment Agent. Registers the agent in the DF.
     * Launches the behaviours that will allow the agent to deploy new resources
     * and orders.
     */
    @Override
    protected void setup() {
        super.setup();
        
        DFInteraction.RegisterInDF(this, this.getLocalName(), Constants.DF_DEPLOYMENT);                
        addBehaviour(new DeployNewAgentResponder(this, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.DEPLOY_NEW_AGENT), MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));        
        addBehaviour(new DeployNewOrderResponder(this, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_DEPLOY_NEW_ORDER), MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));         
        addBehaviour(new UpdateOrderResponder(this, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_UPDATE_ORDER), MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));        
        addBehaviour(new DeployNewProductInstanceResponder(this, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_DEPLOY_NEW_PRODUCT_INSTANCE), MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));         
    }

    /**
     * Default takedown method of the agent.
     */
    @Override
    protected void takeDown() {
        super.takeDown();
    }

}
