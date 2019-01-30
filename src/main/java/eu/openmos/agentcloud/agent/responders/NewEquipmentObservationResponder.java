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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewEquipmentObservationResponder extends AchieveREResponder {

        private PluggableOptimizerInterface optimizationAlgorithm;
        Logger logger = LoggerFactory.getLogger(NewEquipmentObservationResponder.class);

        public NewEquipmentObservationResponder(
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
            EquipmentObservation equipmentObservation;
            try {
                equipmentObservation = (EquipmentObservation)request.getContentObject();
            } catch (UnreadableException ex) {
                throw new RefuseException(ex.getLocalizedMessage());
            }
            logger.debug("responder -> equipmentObservation = " + equipmentObservation);

            boolean status = optimizationAlgorithm.newEquipmentObservation(equipmentObservation);
            
            ACLMessage reply = request.createReply();
            if (status)
            {
                reply.setPerformative(ACLMessage.INFORM);
                try {
                    reply.setContentObject(equipmentObservation);
                    reply.setContent("EquipmentObservation successfully inserted into the local database.");
                } catch (IOException ex) {
                    throw new RefuseException(ex.getLocalizedMessage());                
                }
            }
            else
            {
                reply.setPerformative(ACLMessage.FAILURE);
                reply.setContent("Error during EquipmentObservation insertion into the local database.");
            }            
            
            return reply;
        }
    }
