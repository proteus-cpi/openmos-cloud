package eu.openmos.agentcloud.agent.responders;

import eu.openmos.agentcloud.optimizer.PluggableOptimizerInterface;
import eu.openmos.model.EquipmentObservationRel2;
import eu.openmos.model.OrderInstance;
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

public class ListOrderInstancesResponder extends AchieveREResponder {

        private PluggableOptimizerInterface optimizationAlgorithm;
        Logger logger = LoggerFactory.getLogger(ListOrderInstancesResponder.class);

        public ListOrderInstancesResponder(
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
            List<OrderInstance> listToReturn = optimizationAlgorithm.listOrderInstances();
            
            if (listToReturn == null)
            {
                ACLMessage reply = request.createReply();
                reply.setPerformative(ACLMessage.FAILURE);
                reply.setContent("no order instances found");
                return reply;
            }
            
            // list is not serializable, so need conversion
            OrderInstance[] orderInstances = listToReturn.stream().toArray(OrderInstance[]::new);
            ArrayList<OrderInstance> al = new ArrayList(listToReturn);
            
            
            ACLMessage reply = request.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            try {
                reply.setContentObject(al);
                logger.debug(getClass().getName() + " - handleRequest - " + orderInstances);
                for (int j = 0; j < orderInstances.length; j++)
                    logger.debug("j = " + j + " -> " + orderInstances[j]);
            } catch (IOException ex) {
                throw new RefuseException(ex.getLocalizedMessage());                
            }
            return reply;
        }
    }
