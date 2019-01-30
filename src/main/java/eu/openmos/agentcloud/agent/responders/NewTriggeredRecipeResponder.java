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
import org.apache.log4j.Logger;


/**
 *
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class NewTriggeredRecipeResponder extends AchieveREResponder {

        private PluggableOptimizerInterface optimizationAlgorithm;
        Logger logger = Logger.getLogger(NewTriggeredRecipeResponder.class);

        public NewTriggeredRecipeResponder(
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
            TriggeredRecipe triggeredRecipe;
            try {
                triggeredRecipe = (TriggeredRecipe)request.getContentObject();
            } catch (UnreadableException ex) {
                throw new RefuseException(ex.getLocalizedMessage());
            }
            logger.debug("responder -> triggeredRecipe = " + triggeredRecipe);

            boolean status = optimizationAlgorithm.newTriggeredRecipe(triggeredRecipe);
            
            ACLMessage reply = request.createReply();
            if (status)
            {
                reply.setPerformative(ACLMessage.INFORM);
                try {
                    reply.setContentObject(triggeredRecipe);
                    reply.setContent("TriggeredRecipe successfully inserted into the local database.");
                } catch (IOException ex) {
                    throw new RefuseException(ex.getLocalizedMessage());                
                }
            }
            else
            {
                reply.setPerformative(ACLMessage.FAILURE);
                reply.setContent("Error during TriggeredRecipe insertion into the local database.");
            }            
            
            return reply;
        }
    }
