package eu.openmos.agentcloud.agent.responders;

import eu.openmos.agentcloud.optimizer.PluggableOptimizerInterface;
import eu.openmos.model.EquipmentObservation;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListEquipmentObservationsResponder extends AchieveREResponder {

        private PluggableOptimizerInterface optimizationAlgorithm;
        Logger logger = LoggerFactory.getLogger(ListEquipmentObservationsResponder.class);

        public ListEquipmentObservationsResponder(
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
            List<EquipmentObservation> listToReturn = optimizationAlgorithm.listEquipmentObservations();
            
            // list is not serializable, so need conversion
            EquipmentObservation[] equipmentObservations = listToReturn.stream().toArray(EquipmentObservation[]::new);
            ArrayList<EquipmentObservation> al = new ArrayList(listToReturn);
            
            
            ACLMessage reply = request.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            try {
                // reply.setContentObject(equipmentObservations);
                reply.setContentObject(al);
                logger.debug(getClass().getName() + " - handleRequest - " + equipmentObservations);
                for (int j = 0; j < equipmentObservations.length; j++)
                    logger.debug("j = " + j + " -> " + equipmentObservations[j]);
                // reply.setContent("EquipmentObservation list succesfully retrieved from local database.");
            } catch (IOException ex) {
                throw new RefuseException(ex.getLocalizedMessage());                
            }
            return reply;
        }
    }
