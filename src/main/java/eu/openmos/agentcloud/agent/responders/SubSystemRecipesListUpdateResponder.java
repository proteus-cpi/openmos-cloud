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
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author valerio.gentile
 */
public class SubSystemRecipesListUpdateResponder extends AchieveREResponder {
   private static final org.apache.log4j.Logger logger = Logger.getLogger(SubSystemRecipesListUpdateResponder.class);

    public SubSystemRecipesListUpdateResponder(Agent a, MessageTemplate mt) {
        super(a, mt);
    }

    @Override
    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            ACLMessage reply = request.createReply();
        Recipe recipes[] = null;
        try {
            recipes = (Recipe[]) request.getContentObject();
            
            List<Recipe> recipesList = Arrays.asList(recipes);

            List<Recipe> initialRecipes = ((CyberPhysicalAgent) myAgent).getCpad().getRecipes();

            logger.debug("recipes list before merge process: " + initialRecipes);

            for (Recipe r : recipesList)
            {
                int i = 0;
                boolean found = false;
                for (Recipe initialRecipe : initialRecipes)
                {
                    if (initialRecipe.getUniqueId().equalsIgnoreCase(r.getUniqueId()))
                    {
                        initialRecipes.set(i, r);
                        found = true;
                        break;
                    }
                    i++;
                }
                if (!found)
                    initialRecipes.add(r);
            }
            logger.debug("recipes list before in-memory update: " + initialRecipes);
            
            ((CyberPhysicalAgent) myAgent).getCpad().setRecipes(initialRecipes);
            
            logger.debug("recipes list  after update: " + ((CyberPhysicalAgent) myAgent).getCpad().getRecipes());
            
            // TODO invoke the initiator for updating subsystem recipes into the db
            reply.setPerformative(ACLMessage.INFORM);
            try {
                reply.setContentObject(recipes);
            } catch (IOException ex) {
                logger.error(ex.getLocalizedMessage());
            }
        } catch (UnreadableException ex) {
            logger.error("SubSystemRecipesListUpdateResponder: " + ex);

            reply.setPerformative(ACLMessage.FAILURE);
            reply.setContent(ex.getLocalizedMessage());
        }
/*        
        ACLMessage reply = request.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        return reply;
*/
        return reply;            
    }
    
}
