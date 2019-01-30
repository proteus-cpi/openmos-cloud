package eu.openmos.agentcloud.agent.responders;

import eu.openmos.agentcloud.optimizer.PluggableOptimizerInterface;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.EquipmentAssessment;
import eu.openmos.model.EquipmentObservationRel2;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListEquipmentAssessmentsResponder extends AchieveREResponder {

        private PluggableOptimizerInterface optimizationAlgorithm;
        Logger logger = LoggerFactory.getLogger(ListEquipmentAssessmentsResponder.class);

        public ListEquipmentAssessmentsResponder(
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
//            List<EquipmentAssessment> listToReturn = optimizationAlgorithm.listEquipmentAssessments();
            // VaG - 10/08/2018
            // responder reuse
            List<EquipmentAssessment> listToReturn = new LinkedList<>();
            if (request.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_EQUIPMENT_ASSESSMENTS))
                listToReturn = optimizationAlgorithm.listEquipmentAssessments();
            else if (request.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_EQUIPMENT_ASSESSMENTS_FOR_EQUIPMENT))
            {
                String equipmentId = request.getContent();
                if (equipmentId == null)
                {
                    // failure
                    ACLMessage reply = request.createReply();
                    reply.setPerformative(ACLMessage.FAILURE);
                    reply.setContent("Case of ONTO_LIST_EQUIPMENT_ASSESSMENTS_FOR_EQUIPMENT but equipmentId null - failing");
                    return reply;                    
                }
                listToReturn = optimizationAlgorithm.listEquipmentAssessments(equipmentId);
            }
            else if (request.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_EQUIPMENT_ASSESSMENTS_FOR_SYSTEM))
                listToReturn = optimizationAlgorithm.listSystemAssessments();
            else 
            {
                // failure
                ACLMessage reply = request.createReply();
                reply.setPerformative(ACLMessage.FAILURE);
                reply.setContent("Ontology " + request.getOntology() + " unknown for responder ListEquipmentAssessmentsResponder - failing");
                return reply;
            }
                        
            // list is not serializable, so need conversion
            EquipmentAssessment[] equipmentAssessments = listToReturn.stream().toArray(EquipmentAssessment[]::new);
            ArrayList<EquipmentAssessment> al = new ArrayList(listToReturn);
            
            
            ACLMessage reply = request.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            try {
                reply.setContentObject(al);
                logger.debug(getClass().getName() + " - handleRequest - " + equipmentAssessments);
                for (int j = 0; j < equipmentAssessments.length; j++)
                    logger.debug("j = " + j + " -> " + equipmentAssessments[j]);
            } catch (IOException ex) {
                throw new RefuseException(ex.getLocalizedMessage());                
            }
            return reply;
        }
    }
