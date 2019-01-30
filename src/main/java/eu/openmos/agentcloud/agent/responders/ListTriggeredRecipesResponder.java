package eu.openmos.agentcloud.agent.responders;

import eu.openmos.agentcloud.optimizer.PluggableOptimizerInterface;
import eu.openmos.model.TriggeredRecipe;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//import static jdk.nashorn.internal.objects.NativeDebug.getClass;
import org.apache.log4j.Logger;

/**
 *
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class ListTriggeredRecipesResponder extends AchieveREResponder {

        private PluggableOptimizerInterface optimizationAlgorithm;
        Logger logger = Logger.getLogger(ListTriggeredRecipesResponder.class);

        public ListTriggeredRecipesResponder(
                Agent a, 
                PluggableOptimizerInterface optimizationAlgorithm, 
                MessageTemplate mt
        ) 
        {
            super(a, mt);
            this.optimizationAlgorithm = optimizationAlgorithm;
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            String recipeId;
            try {
                recipeId = (String)request.getContentObject();
            } catch (UnreadableException ex) {
                throw new RefuseException(ex.getLocalizedMessage());
            }
            logger.debug("responder -> recipeId = " + recipeId);
        
            List<TriggeredRecipe> listToReturn = optimizationAlgorithm.listTriggeredRecipes(recipeId);
            
            // list is not serializable, so need conversion
            TriggeredRecipe[] triggeredRecipes = listToReturn.stream().toArray(TriggeredRecipe[]::new);
            ArrayList<TriggeredRecipe> al = new ArrayList(listToReturn);
            
            
            ACLMessage reply = request.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            try {
                reply.setContentObject(al);
                logger.debug(getClass().getName() + " - handleRequest - " + triggeredRecipes);
                for (int j = 0; j < triggeredRecipes.length; j++)
                    logger.debug("j = " + j + " -> " + triggeredRecipes[j]);
            } catch (IOException ex) {
                throw new RefuseException(ex.getLocalizedMessage());                
            }
            return reply;
        }
    }

