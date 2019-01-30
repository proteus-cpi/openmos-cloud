package eu.openmos.agentcloud.agent.responders;

import eu.openmos.agentcloud.optimizer.PluggableOptimizerInterface;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.EquipmentObservationRel2;
import eu.openmos.model.ProcessAssessment;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListProcessAssessmentsResponder extends AchieveREResponder {

        private PluggableOptimizerInterface optimizationAlgorithm;
        Logger logger = LoggerFactory.getLogger(ListProcessAssessmentsResponder.class);

        public ListProcessAssessmentsResponder(
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
//            List<ProcessAssessment> listToReturn = optimizationAlgorithm.listProcessAssessments();
            // VaG - 10/08/2018
            // responder reuse
            List<ProcessAssessment> listToReturn = new LinkedList<>();
            if (request.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_PROCESS_ASSESSMENTS))
                listToReturn = optimizationAlgorithm.listProcessAssessments();
            else if (request.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_PROCESS_ASSESSMENTS_FOR_RECIPE))
            {
                String recipeId = request.getContent();
                if (recipeId == null)
                {
                    // failure
                    ACLMessage reply = request.createReply();
                    reply.setPerformative(ACLMessage.FAILURE);
                    reply.setContent("Case of ONTO_LIST_PROCESS_ASSESSMENTS_FOR_RECIPE but recipeId null - failing");
                    return reply;                    
                }
//                listToReturn = optimizationAlgorithm.listProcessAssessments(recipeId);
                listToReturn = optimizationAlgorithm.listProcessAssessmentsForRecipe(recipeId);
            }
            else if (request.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_PROCESS_ASSESSMENTS_FOR_SKILL))
            {
                String skillId = request.getContent();
                if (skillId == null)
                {
                    // failure
                    ACLMessage reply = request.createReply();
                    reply.setPerformative(ACLMessage.FAILURE);
                    reply.setContent("Case of ONTO_LIST_PROCESS_ASSESSMENTS_FOR_SKILL but skillId null - failing");
                    return reply;                    
                }
                listToReturn = optimizationAlgorithm.listProcessAssessmentsForSkill(skillId);
            }
            else 
            {
                // failure
                ACLMessage reply = request.createReply();
                reply.setPerformative(ACLMessage.FAILURE);
                reply.setContent("Ontology " + request.getOntology() + " unknown for responder ListProcessAssessmentsResponder - failing");
                return reply;
            }
            
            // list is not serializable, so need conversion
            ProcessAssessment[] processAssessments = listToReturn.stream().toArray(ProcessAssessment[]::new);
            ArrayList<ProcessAssessment> al = new ArrayList(listToReturn);
            
            
            ACLMessage reply = request.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            try {
                reply.setContentObject(al);
                logger.debug(getClass().getName() + " - handleRequest - " + processAssessments);
                for (int j = 0; j < processAssessments.length; j++)
                    logger.debug("j = " + j + " -> " + processAssessments[j]);
            } catch (IOException ex) {
                throw new RefuseException(ex.getLocalizedMessage());                
            }
            return reply;
        }
    }
