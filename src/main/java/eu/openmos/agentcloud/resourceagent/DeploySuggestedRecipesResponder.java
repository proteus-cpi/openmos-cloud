/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.resourceagent;

// import eu.openmos.agentcloud.data.recipe.Recipe;
import eu.openmos.model.Recipe;
import eu.openmos.agentcloud.optimizer.productionoptimizeragent.DeployRecipesRequest;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.servicebus.MSBHelper;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 * Receives updated recipes suggestions from the optimizer and deploys them in 
 * the device using the Manufacturing Service Bus services.
 * 
 * Content: Serialized form of a DeployRecipesRequest object.
 * Counterpart: Optimizer Agent
 * Ontology: ONTO_DEPLOY_SUGGEST_RECIPES.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 */
public class DeploySuggestedRecipesResponder extends AchieveREResponder {
    /**
    * Object that writes logs into the file.
    */
    private static final Logger logger = Logger.getLogger(DeploySuggestedRecipesResponder.class.getName());   

    /**
     * Default AchieveREResponder constructor.
     * 
     * @param a - the agent that will hold this behaviour.
     * @param mt - the template for the messages to which this behaviour will
     * listen to.
     */
    public DeploySuggestedRecipesResponder(Agent a, MessageTemplate mt) {
        super(a, mt);
    }

    /**
     * Method that handles the request message that will come from the counterpart.
     * It deploys the recipes in the Manufacturing Service Bus.
     * 
     * @param request - message that triggers the protocol.
     * @return Agree or Failure message.
     * @throws NotUnderstoodException - default exception
     * @throws RefuseException - default exception
     */
    @Override
    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
        ACLMessage reply = request.createReply();       
        List<Recipe> suggestedRecipes;
        try {
//            suggestedRecipes= DeployRecipesRequest.fromString(request.getContent()).getRecipesToBeDeployed(myAgent.getAID());
            suggestedRecipes = ((DeployRecipesRequest)request.getContentObject()).getRecipesToBeDeployed(myAgent.getAID());
        } catch (UnreadableException ex) {
            throw new RefuseException(ex.getMessage());
        }
        

        boolean recipeDeployed = MSBHelper.updateRecipes(myAgent.getLocalName(), Constants.RECIPES_SUGGESTION, suggestedRecipes);
        
        if(!recipeDeployed){
            logger.info("New recipe could not be deployed in " + myAgent.getLocalName() + ".");
            reply.setPerformative(ACLMessage.FAILURE);
        }else{
            logger.info("New recipe deployed in " + myAgent.getLocalName() + ".");            
            reply.setPerformative(ACLMessage.INFORM);
        }
        return reply;
    }
    
}
