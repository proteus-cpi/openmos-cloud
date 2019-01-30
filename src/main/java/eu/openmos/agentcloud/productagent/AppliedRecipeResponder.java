/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.productagent;

// import eu.openmos.agentcloud.data.recipe.Recipe;
import eu.openmos.model.RawProductData;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;
import org.apache.log4j.Logger;

/**
 * Receives notifications from the resource when it finishes applying the desired
 * skill to the product. When the product is notified it notifies the optimizer 
 * of the changes occurred.
 * 
 * Content: The content of this message must be a serialized object of type
 * AgentData.
 * Counterpart: Resource Agent
 * Ontology: ONTO_APPLIED_RECIPE.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 *     
 */
public class AppliedRecipeResponder extends AchieveREResponder {
    /**
    * Object that writes logs into the file.
    */
    private static final Logger logger = Logger.getLogger(AppliedRecipeResponder.class.getName());

    /**
    * Default AchieveREResponder constructor.
    * 
    * @param a - the agent that will hold this behaviour.
    * @param mt - the template for the messages to which this behaviour will
    * listen to.
    */
    public AppliedRecipeResponder(Agent a, MessageTemplate mt) {
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
//    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
//        ACLMessage response = request.createReply();
//        response.setPerformative(ACLMessage.INFORM);
//        
//        // AgentData data = AgentData.fromString(request.getContent());
//        RawProductData data;        
//        try
//        {
//            data = RawProductData.fromString(request.getContent());            
//        }
//        catch (ParseException pe)
//        {
//            throw new RefuseException(pe.getMessage());
//        }
//
//        // old code
////        ((ProductAgent) myAgent).appliedRecipes.add(data.getRecipe());
////        ((ProductAgent) myAgent).description.getSkillRequirements().removeAll(data.getRecipe().getSkillRequirements());
//
//       // List<Recipe> recipes = data.getRecipes();
//     //   ((ProductAgent) myAgent).appliedRecipes.addAll(recipes);
//     //   for (Recipe recipe : data.getRecipes())
//         //   ((ProductAgent) myAgent).description.getSkillRequirements().removeAll(recipe.getSkillRequirements());
//
//        myAgent.addBehaviour(new NotifyOptimizerOfUpdatedData(myAgent, NotifyOptimizerOfUpdatedData.buildMessage(myAgent, request.getContent())));
//        return response;
//    }
    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
        ACLMessage response = request.createReply();
        response.setPerformative(ACLMessage.INFORM);
        
        // AgentData data = AgentData.fromString(request.getContent());
        RawProductData data;        
        try {            
            data = (RawProductData)request.getContentObject();
        } catch (UnreadableException ex) {
            throw new RefuseException(ex.getLocalizedMessage());
        }

        // old code
//        ((ProductAgent) myAgent).appliedRecipes.add(data.getRecipe());
//        ((ProductAgent) myAgent).description.getSkillRequirements().removeAll(data.getRecipe().getSkillRequirements());

       // List<Recipe> recipes = data.getRecipes();
     //   ((ProductAgent) myAgent).appliedRecipes.addAll(recipes);
     //   for (Recipe recipe : data.getRecipes())
         //   ((ProductAgent) myAgent).description.getSkillRequirements().removeAll(recipe.getSkillRequirements());

        myAgent.addBehaviour(new NotifyOptimizerOfUpdatedData(myAgent, NotifyOptimizerOfUpdatedData.buildMessage(myAgent, data)));
        return response;
    }
    
}
