package eu.openmos.agentcloud.agent.responders;

import eu.openmos.agentcloud.optimizer.PluggableOptimizerInterface;
import eu.openmos.model.OrderInstance;
import eu.openmos.model.ProductInstance;
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

public class ListProductInstancesNotYetProducedResponder extends AchieveREResponder {

        private PluggableOptimizerInterface optimizationAlgorithm;
        Logger logger = LoggerFactory.getLogger(ListProductInstancesNotYetProducedResponder.class);

        public ListProductInstancesNotYetProducedResponder(
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
            List<ProductInstance> listToReturn = optimizationAlgorithm.listProductInstancesNotYetProduced();
            
            if (listToReturn == null)
                listToReturn = new LinkedList<>();
            
            // list is not serializable, so need conversion
            ProductInstance[] productInstances = listToReturn.stream().toArray(ProductInstance[]::new);
            ArrayList<ProductInstance> al = new ArrayList(listToReturn);
            
            
            ACLMessage reply = request.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            try {
                reply.setContentObject(al);
                logger.debug(getClass().getName() + " - handleRequest - " + productInstances);
                for (int j = 0; j < productInstances.length; j++)
                    logger.debug("j = " + j + " -> " + productInstances[j]);
            } catch (IOException ex) {
                throw new RefuseException(ex.getLocalizedMessage());                
            }
            return reply;
        }
    }
