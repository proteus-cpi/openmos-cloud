package eu.openmos.agentcloud.agent.responders;

import eu.openmos.agentcloud.optimizer.PluggableOptimizerInterface;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.EquipmentAdjustment;
import eu.openmos.model.EquipmentAssessment;
import eu.openmos.model.EquipmentObservationRel2;
import eu.openmos.model.PhysicalAdjustment;
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

public class ListPhysicalAdjustmentsResponder extends AchieveREResponder {

        private PluggableOptimizerInterface optimizationAlgorithm;
        Logger logger = LoggerFactory.getLogger(ListPhysicalAdjustmentsResponder.class);

        public ListPhysicalAdjustmentsResponder(
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
//            List<PhysicalAdjustment> listToReturn = optimizationAlgorithm.listPhysicalAdjustments();
            // VaG - 10/08/2018
            // responder reuse
            List<PhysicalAdjustment> listToReturn = new LinkedList<>();
            if (request.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_PHYSICAL_ADJUSTMENTS))
                listToReturn = optimizationAlgorithm.listPhysicalAdjustments();
            else if (request.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_PHYSICAL_ADJUSTMENTS_FOR_EQUIPMENT))
            {
                String equipmentId = request.getContent();
                if (equipmentId == null)
                {
                    // failure
                    ACLMessage reply = request.createReply();
                    reply.setPerformative(ACLMessage.FAILURE);
                    reply.setContent("Case of ONTO_LIST_PHYSICAL_ADJUSTMENTS_FOR_EQUIPMENT but equipmentId null - failing");
                    return reply;                    
                }
                listToReturn = optimizationAlgorithm.listPhysicalAdjustments(equipmentId);
            }
            else 
            {
                // failure
                ACLMessage reply = request.createReply();
                reply.setPerformative(ACLMessage.FAILURE);
                reply.setContent("Ontology " + request.getOntology() + " unknown for responder ListPhysicalAdjustmentsResponder - failing");
                return reply;
            }
            
            
            // list is not serializable, so need conversion
            PhysicalAdjustment[] physicalAdjustments = listToReturn.stream().toArray(PhysicalAdjustment[]::new);
            ArrayList<PhysicalAdjustment> al = new ArrayList(listToReturn);
            
            
            ACLMessage reply = request.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            try {
                reply.setContentObject(al);
                logger.debug(getClass().getName() + " - handleRequest - " + physicalAdjustments);
                for (int j = 0; j < physicalAdjustments.length; j++)
                    logger.debug("j = " + j + " -> " + physicalAdjustments[j]);
            } catch (IOException ex) {
                throw new RefuseException(ex.getLocalizedMessage());                
            }
            return reply;
        }
    }
