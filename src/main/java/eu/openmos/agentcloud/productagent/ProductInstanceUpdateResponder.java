package eu.openmos.agentcloud.productagent;

import eu.openmos.agentcloud.optimizer.PluggableOptimizerInterface;
import eu.openmos.model.ProductInstance;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class ProductInstanceUpdateResponder  extends AchieveREResponder {

        private PluggableOptimizerInterface optimizationAlgorithm;
        Logger logger = Logger.getLogger(ProductInstanceUpdateResponder.class);

        public ProductInstanceUpdateResponder(
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
            ProductInstance p;
            try {
                p = (ProductInstance)request.getContentObject();
            } catch (UnreadableException ex) {
                throw new RefuseException(ex.getLocalizedMessage());
            }
            optimizationAlgorithm.updateProduct(p);

            // After updating database we need to update internal memory object.
            AID agentToBeUpdated = new AID(p.getUniqueId(), false);
            ACLMessage updateMessage = ProductInstanceInternalUpdateInitiator.buildMessage(agentToBeUpdated, p);
            myAgent.addBehaviour(new ProductInstanceInternalUpdateInitiator(myAgent, updateMessage));
            
            logger.debug(getClass().getName() + " - handleRequest");
            ACLMessage reply = request.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            try {
                reply.setContentObject(p);
            } catch (IOException ex) {
                logger.error(ex.getLocalizedMessage());
            }
            return reply;
        }
    }

