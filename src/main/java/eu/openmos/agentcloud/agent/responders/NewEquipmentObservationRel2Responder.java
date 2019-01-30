package eu.openmos.agentcloud.agent.responders;

import eu.openmos.agentcloud.optimizer.PluggableOptimizerInterface;
import eu.openmos.model.EquipmentObservationRel2;
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

public class NewEquipmentObservationRel2Responder extends AchieveREResponder {

        private PluggableOptimizerInterface optimizationAlgorithm;
        Logger logger = LoggerFactory.getLogger(NewEquipmentObservationRel2Responder.class);

        public NewEquipmentObservationRel2Responder(
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
            EquipmentObservationRel2 equipmentObservationRel2;
            try {
                equipmentObservationRel2 = (EquipmentObservationRel2)request.getContentObject();
            } catch (UnreadableException ex) {
                throw new RefuseException(ex.getLocalizedMessage());
            }
            logger.debug("responder -> equipmentObservationRel2 = " + equipmentObservationRel2);

            boolean status = optimizationAlgorithm.newEquipmentObservationRel2(equipmentObservationRel2);
            
            ACLMessage reply = request.createReply();
            if (status)
            {
                reply.setPerformative(ACLMessage.INFORM);
                try {
                    reply.setContentObject(equipmentObservationRel2);
                    reply.setContent("EquipmentObservationRel2 successfully inserted into the local database.");
                } catch (IOException ex) {
                    throw new RefuseException(ex.getLocalizedMessage());                
                }
            }
            else
            {
                reply.setPerformative(ACLMessage.FAILURE);
                reply.setContent("Error during EquipmentObservationRel2 insertion into the local database.");
            }            
            
            return reply;
        }
    }
