package eu.openmos.agentcloud.agent.responders;

import eu.openmos.agentcloud.optimizer.PluggableOptimizerInterface;
import eu.openmos.agentcloud.utilities.Constants;
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

public class ListEquipmentObservationRel2sResponder extends AchieveREResponder {

        private PluggableOptimizerInterface optimizationAlgorithm;
        Logger logger = LoggerFactory.getLogger(ListEquipmentObservationRel2sResponder.class);

        public ListEquipmentObservationRel2sResponder(
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
            // VaG - 10/08/2018
            // responder reuse
            List<EquipmentObservationRel2> listToReturn = new LinkedList<>();
            if (request.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_EQUIPMENT_OBSERVATION_REL2S))
                listToReturn = optimizationAlgorithm.listEquipmentObservationRel2s();
            else if (request.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_EQUIPMENT_OBSERVATION_REL2S_FOR_EQUIPMENT))
            {
                String equipmentId = request.getContent();
                if (equipmentId == null)
                {
                    // failure
                    ACLMessage reply = request.createReply();
                    reply.setPerformative(ACLMessage.FAILURE);
                    reply.setContent("Case of ONTO_LIST_EQUIPMENT_OBSERVATION_REL2S_FOR_EQUIPMENT but equipmentId null - failing");
                    return reply;                    
                }
                listToReturn = optimizationAlgorithm.listEquipmentObservationRel2s(equipmentId);
            }
            else if (request.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_EQUIPMENT_OBSERVATION_REL2S_FOR_SYSTEM))
                listToReturn = optimizationAlgorithm.listSystemObservationRel2s();
            else 
            {
                // failure
                ACLMessage reply = request.createReply();
                reply.setPerformative(ACLMessage.FAILURE);
                reply.setContent("Ontology " + request.getOntology() + " unknown for responder ListEquipmentObservationRel2sResponder - failing");
                return reply;
            }
            
            // list is not serializable, so need conversion
            EquipmentObservationRel2[] equipmentObservationRel2s = listToReturn.stream().toArray(EquipmentObservationRel2[]::new);
            ArrayList<EquipmentObservationRel2> al = new ArrayList(listToReturn);
            
            
            ACLMessage reply = request.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            try {
                reply.setContentObject(al);
                logger.debug(getClass().getName() + " - handleRequest - " + equipmentObservationRel2s);
                for (int j = 0; j < equipmentObservationRel2s.length; j++)
                    logger.debug("j = " + j + " -> " + equipmentObservationRel2s[j]);
            } catch (IOException ex) {
                throw new RefuseException(ex.getLocalizedMessage());                
            }
            return reply;
        }
    }
