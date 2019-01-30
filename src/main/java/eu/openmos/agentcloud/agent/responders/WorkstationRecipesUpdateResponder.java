package eu.openmos.agentcloud.agent.responders;

import eu.openmos.agentcloud.optimizer.PluggableOptimizerInterface;
import eu.openmos.model.SubSystemRecipes;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Notifies the optimizer whenever a workstation needs to update its recipes list.
 * 
 * @author valerio.gentile
 */
public class WorkstationRecipesUpdateResponder extends AchieveREResponder {

        private PluggableOptimizerInterface optimizationAlgorithm;
        Logger logger = LoggerFactory.getLogger(WorkstationRecipesUpdateResponder.class);

        public WorkstationRecipesUpdateResponder(
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
            SubSystemRecipes subSystemRecipes;
            try {
                subSystemRecipes = (SubSystemRecipes)request.getContentObject();
            } catch (UnreadableException ex) {
                throw new RefuseException(ex.getLocalizedMessage());
            }
            optimizationAlgorithm.updateWorkstationRecipes(subSystemRecipes);
            
            logger.debug(getClass().getName() + " - handleRequest - " + request.getContent());
            ACLMessage reply = request.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            return reply;
        }
    }
