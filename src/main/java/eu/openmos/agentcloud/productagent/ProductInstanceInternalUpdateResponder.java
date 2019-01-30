package eu.openmos.agentcloud.productagent;

import eu.openmos.agentcloud.productagent.ProductAgent;
import eu.openmos.model.ProductInstance;
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
public class ProductInstanceInternalUpdateResponder extends AchieveREResponder {

        Logger logger = Logger.getLogger(ProductInstanceInternalUpdateResponder.class);

        public ProductInstanceInternalUpdateResponder(
                Agent a, 
                MessageTemplate mt
        ) 
        {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            ProductInstance p;
            try {
                p = (ProductInstance)request.getContentObject();
            } catch (UnreadableException ex) {
                throw new RefuseException(ex.getLocalizedMessage());
            }

            // After updating database let's update internal memory object.
            ((ProductAgent) myAgent).setProductInstance(p);
            
            logger.debug(getClass().getName() + " - handleRequest - agent updated its internal object: " + p);
            ACLMessage reply = request.createReply();
            try {
                reply.setContentObject(p);
            } catch (IOException ex) {
                logger.error(ex.getLocalizedMessage());
            }
            reply.setPerformative(ACLMessage.INFORM);
            return reply;
        }
    }
