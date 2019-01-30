package eu.openmos.agentcloud.agent.responders;

import eu.openmos.agentcloud.optimizer.PluggableOptimizerInterface;
import eu.openmos.model.EquipmentObservation;
import eu.openmos.model.Recipe;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PutRecipesResponder extends AchieveREResponder {

        private PluggableOptimizerInterface optimizationAlgorithm;
        Logger logger = LoggerFactory.getLogger(PutRecipesResponder.class);

        public PutRecipesResponder(
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
            Recipe[] recipes;
            try {
                recipes = (Recipe[])request.getContentObject();
            } catch (UnreadableException ex) {
                throw new RefuseException(ex.getLocalizedMessage());
            }
            logger.debug("responder -> recipes = " + recipes);

            boolean status = optimizationAlgorithm.putRecipes(recipes);
            
            ACLMessage reply = request.createReply();
            if (status)
            {
                logger.debug("responder -> status = " + status);
                reply.setPerformative(ACLMessage.INFORM);
                reply.setContent("Recipes successfully stored into the local database.");
            }
            else
            {
                logger.debug("responder -> status = " + status);
                reply.setPerformative(ACLMessage.FAILURE);
                reply.setContent("Error while saving recipes in the local database.");
            }            
            
            return reply;
        }
    }
