package eu.openmos.agentcloud.agent.responders;

import eu.openmos.agentcloud.cyberphysicalagent.CyberPhysicalAgent;
import eu.openmos.model.Recipe;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author valerio.gentile
 */
public class SubSystemRecipeUpdateResponder extends AchieveREResponder {
   private static final org.apache.log4j.Logger logger = Logger.getLogger(SubSystemRecipeUpdateResponder.class);

    public SubSystemRecipeUpdateResponder(Agent a, MessageTemplate mt) {
        super(a, mt);
    }

    @Override
    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
        Recipe recipe = null;
        try {
            recipe = (Recipe) request.getContentObject();
            // List<Recipe> recipesList = Arrays.asList(recipes);
            
            List<Recipe> initialRecipes = ((CyberPhysicalAgent) myAgent).getCpad().getRecipes();

            logger.debug("recipes list before update: " + initialRecipes);
            
            int i = 0;
            boolean found = false;
            for (Recipe r : initialRecipes)
            {
                if (r.getUniqueId().equalsIgnoreCase(recipe.getUniqueId()))
                {
                    initialRecipes.set(i, recipe);
                    found = true;
                    break;
                }
                i++;
            }
            if (!found)
                initialRecipes.add(recipe);
            ((CyberPhysicalAgent) myAgent).getCpad().setRecipes(initialRecipes);

            logger.debug("recipes list  after update: " + ((CyberPhysicalAgent) myAgent).getCpad().getRecipes());
        } catch (UnreadableException ex) {
            logger.error("SubSystemRecipesListUpdateResponder: " + ex);
        }
        
        ACLMessage reply = request.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        return reply;
    }
    
}
