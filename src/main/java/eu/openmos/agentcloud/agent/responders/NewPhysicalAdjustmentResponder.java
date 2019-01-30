package eu.openmos.agentcloud.agent.responders;

import eu.openmos.agentcloud.optimizer.PluggableOptimizerInterface;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewPhysicalAdjustmentResponder extends AchieveREResponder {

        private PluggableOptimizerInterface optimizationAlgorithm;
        Logger logger = LoggerFactory.getLogger(NewPhysicalAdjustmentResponder.class);

        public NewPhysicalAdjustmentResponder(
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
            PhysicalAdjustment physicalAdjustment;
            try {
                physicalAdjustment = (PhysicalAdjustment)request.getContentObject();
            } catch (UnreadableException ex) {
                throw new RefuseException(ex.getLocalizedMessage());
            }
            logger.debug("responder -> physicalAdjustment = " + physicalAdjustment);

            boolean status = optimizationAlgorithm.newPhysicalAdjustment(physicalAdjustment);
            
            ACLMessage reply = request.createReply();
            if (status)
            {
                reply.setPerformative(ACLMessage.INFORM);
                try {
                    reply.setContentObject(physicalAdjustment);
                    reply.setContent("PhysicalAdjustment successfully inserted into the local database.");
                } catch (IOException ex) {
                    throw new RefuseException(ex.getLocalizedMessage());                
                }
            }
            else
            {
                reply.setPerformative(ACLMessage.FAILURE);
                reply.setContent("Error during PhysicalAdjustment insertion into the local database.");
            }            
            
            return reply;
        }
    }
